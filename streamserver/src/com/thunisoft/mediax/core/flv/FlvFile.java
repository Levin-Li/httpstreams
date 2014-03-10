package com.thunisoft.mediax.core.flv;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.apache.commons.codec.Decoder;
import org.apache.commons.codec.DecoderException;

import com.thunisoft.mediax.core.amf.AMF0Decoder;
import com.thunisoft.mediax.core.amf.AMFArray;
import com.thunisoft.mediax.core.utils.ByteBufferUtils;
import com.thunisoft.mediax.core.vfs.RandomAccessChannel;
import com.thunisoft.mediax.core.vfs.local.RandomAccessFileChannelImpl;

public class FlvFile implements Closeable {
    private static final int LENGTH_TAGHEAD = 11;
    private static final int LENGTH_TAGSIZE = 4;

    final private RandomAccessChannel fch;

    public static final int TP_SCRIPT = 18;
    public static final int TP_AUDIO = 8;
    public static final int TP_VIDEO = 9;

    final private ByteBuffer flvHead;
    final private FlvMetaData metadata;

    public FlvFile(File file) throws IOException {
        this(new RandomAccessFileChannelImpl(file));
    }

    public FlvFile(RandomAccessChannel channel) throws IOException {
        fch = channel;

        flvHead = findFlvHead(fch);
        metadata = findMetaData(fch);
    }

    private ByteBuffer findFlvHead(RandomAccessChannel fch) throws IOException {
        long lastPosition = fch.position();

        try {
            fch.position(0);
            return nextTag(fch); // head

        } finally {
            fch.position(lastPosition);
        }
    }


    private FlvMetaData findMetaData(RandomAccessChannel fch) throws IOException {
        long lastPosition = fch.position();

        try {
            fch.position(0);

            nextTag(fch); // head
            ByteBuffer scriptData = nextTag(fch); // first data tag

            if (!isScript(scriptData)) {
                throw new IOException("Is Not A Supported Flv File");
            }

            AMFArray metadata = asMetadata(scriptData);
            if (null == metadata) {
                throw new IOException("Is Not A Supported Flv File");
            }

            return new FlvMetaData(metadata);

        } catch (DecoderException e) {
            throw new IOException("Is Not A Supported Flv File");
        } finally {
            fch.position(lastPosition);
        }
    }

    private AMFArray asMetadata(ByteBuffer tagData) throws DecoderException {
        Decoder decoder = new AMF0Decoder();

        // tag head
        tagData.position(LENGTH_TAGHEAD);

        // string
        String name = (String) decoder.decode(tagData);
        if (!"onMetaData".equals(name)) {
            return null;
        }

        // type
        AMFArray metadata = (AMFArray) decoder.decode(tagData);
        return metadata;
    }

    @Override
    public void close() throws IOException {
        fch.close();
    }


    private ByteBuffer nextTag(RandomAccessChannel ch) throws IOException {
        final long currentPosition = ch.position();
        boolean isHeadOfFile = (currentPosition == 0L);

        if (!isHeadOfFile) {
            // pre tag size
            skipBytes(fch, LENGTH_TAGSIZE);

            // next tag
            return readTag(ch);
        } else {
            return readFileHeader(ch);
        }
    }

    private void skipBytes(RandomAccessChannel ch, final long bytes) throws IOException {
        long newPosition = ch.position() + bytes;

        ch.position(newPosition);
    }

    private ByteBuffer readFileHeader(RandomAccessChannel ch) throws IOException {
        long startPosition = ch.position();

        ByteBuffer bytes = ByteBuffer.allocate(9);
        ch.read(bytes);
        bytes.flip();
        String typeFlag = ByteBufferUtils.read3cc(bytes);
        if (!"flv".equalsIgnoreCase(typeFlag)) {
            throw new IOException("It's not flv file");
        }

        int version = ByteBufferUtils.readUInt8(bytes);
        int flags = ByteBufferUtils.readUInt8(bytes);
        long headSize = ByteBufferUtils.readUInt32(bytes);
        boolean hasAudio = (flags & 0xF0) > 0;
        boolean hasVideo = (flags & 0x0F) > 0;


        // head
        ch.position(startPosition);
        ByteBuffer content = ByteBuffer.allocate(ByteBufferUtils.long2Int(headSize));
        ch.read(content);

        content.flip();
        return content;
    }

    private ByteBuffer readTag(RandomAccessChannel ch) throws IOException {
        long startPosition = ch.position();

        // tag head
        ByteBuffer tagHead = ByteBuffer.allocate(LENGTH_TAGHEAD);
        ch.read(tagHead);
        tagHead.flip();
        int type = ByteBufferUtils.readUInt8(tagHead);
        int dataSize = ByteBufferUtils.readUInt24(tagHead);
        long timestamp = ByteBufferUtils.readUInt32(tagHead);
        int streamId = ByteBufferUtils.readUInt24(tagHead);

        // tag
        ch.position(startPosition);
        ByteBuffer content = ByteBuffer.allocate(dataSize);
        ch.read(content);

        content.flip();
        return content;
    }

    private boolean isScript(ByteBuffer tagData) {
        int position = tagData.position();

        try {
            int type = ByteBufferUtils.readUInt8(tagData);
            return TP_SCRIPT == type;
        } finally {
            tagData.position(position);
        }
    }

    public RandomAccessChannel seek(long startAt) throws IOException {
        long position = metadata.getNearestPosition(startAt);
        if (position <= 0) {
            nextTag(fch); // 跳过文件头， 从第一个 Tag开始播
            skipBytes(fch, LENGTH_TAGSIZE);
        } else {
            fch.position(position);// 直接跳过去
        }

        return fch;
    }

    public FlvMetaData getMetadata() throws IOException {
        return metadata;
    }

    public ByteBuffer getFlvHead() throws IOException {
        return flvHead;
    }

}
