package com.thunisoft.mediax.core.pseudostreaming.flv;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

import org.apache.commons.codec.Decoder;
import org.apache.commons.codec.DecoderException;

import com.thunisoft.mediax.core.amf.AMF0Decoder;
import com.thunisoft.mediax.core.amf.AMFArray;
import com.thunisoft.mediax.core.flv.FlvMetaData;
import com.thunisoft.mediax.core.pseudostreaming.AbstractStreamer;
import com.thunisoft.mediax.core.utils.ByteBufferUtils;
import com.thunisoft.mediax.core.vfs.RandomAccessChannel;

/**
 * flv 流化
 * 
 * @since V1.0 2014-3-3
 * @author chenxh
 */
public class FlvStreamer extends AbstractStreamer {


    public FlvStreamer(RandomAccessChannel channel) throws FileNotFoundException {
        super(channel);
    }


    @Override
    public void transfer(double startAt, WritableByteChannel outChannel) throws IOException {
        channel.position(0);
        if (startAt > 0) {
            write(outChannel, startAt);
        } else {
            write(outChannel);
        }

    }

    private void write(WritableByteChannel outChannel) throws IOException {
        channel.transferTo(0, channel.length(), outChannel);

    }

    public void write(WritableByteChannel outChannel, double startAt) throws IOException {
        ByteBuffer b = findFlvHead(channel);

        // flv head
        outChannel.write(b);
        outChannel.write(ByteBuffer.wrap(ByteBufferUtils.wrapUInt32(b.position())));

        // flv data
        long position = findMetaData(channel).getNearestPosition((long)startAt);
        
        channel.transferTo(position, channel.length() - position, outChannel);
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
    


    private ByteBuffer nextTag(RandomAccessChannel ch) throws IOException {
        final long currentPosition = ch.position();
        boolean isHeadOfFile = (currentPosition == 0L);

        if (!isHeadOfFile) {
            // pre tag size
            skipBytes(ch, LENGTH_TAGSIZE);

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
    private static final int LENGTH_TAGHEAD = 11;
    private static final int LENGTH_TAGSIZE = 4;


    public static final int TP_SCRIPT = 18;
    public static final int TP_AUDIO = 8;
    public static final int TP_VIDEO = 9;
}
