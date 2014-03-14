package com.thunisoft.mediax.core.pseudostreaming.flv;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

import org.apache.commons.codec.DecoderException;

import com.thunisoft.mediax.core.codec.amf.AMF0Decoder;
import com.thunisoft.mediax.core.codec.amf.AMFArray;
import com.thunisoft.mediax.core.codec.flv.FlvMetaData;
import com.thunisoft.mediax.core.pseudostreaming.AbstractStreamer;
import com.thunisoft.mediax.core.utils.ByteUtils;
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
        channel.position(0);

        // flv head
        ByteBuffer fileHead = nextTag(channel); // head
        outChannel.write(fileHead);
        outChannel.write(ByteBuffer.wrap(ByteUtils.wrapUInt32(fileHead.position())));

        // flv data
        ByteBuffer scriptData = nextTag(channel); // first data tag
        long position = decodeMetadata(scriptData).getNearestPosition((long)startAt);
        
        channel.transferTo(position, channel.length() - position, outChannel);
    }




    private FlvMetaData decodeMetadata(ByteBuffer scriptData) throws IOException {
        try {

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
        }
    }

    private AMFArray asMetadata(ByteBuffer tagData) throws DecoderException {
        AMF0Decoder decoder = new AMF0Decoder();

        // tag head
        tagData.position(LENGTH_TAGHEAD);

        // string
        Object[] decodedRst = decoder.decode(tagData);
        String name = (String) decodedRst[0];
        if (!"onMetaData".equals(name)) {
            return null;
        }

        // type
        AMFArray metadata = (AMFArray) decodedRst[1];
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
        String typeFlag = ByteUtils.read3cc(bytes);
        if (!"flv".equalsIgnoreCase(typeFlag)) {
            throw new IOException("It's not flv file");
        }

        int version = ByteUtils.readUInt8(bytes);
        int flags = ByteUtils.readUInt8(bytes);
        long headSize = ByteUtils.readUInt32(bytes);
        boolean hasAudio = (flags & 0xF0) > 0;
        boolean hasVideo = (flags & 0x0F) > 0;


        // head
        ch.position(startPosition);
        ByteBuffer content = ByteBuffer.allocate(ByteUtils.long2Int(headSize));
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
        int type = ByteUtils.readUInt8(tagHead);
        int dataSize = ByteUtils.readUInt24(tagHead);
        long timestamp = ByteUtils.readUInt32(tagHead);
        int streamId = ByteUtils.readUInt24(tagHead);

        // tag
        ch.position(startPosition);
        ByteBuffer content = ByteBuffer.allocate(dataSize);
        ch.readFull(content);

        content.flip();
        return content;
    }

    private boolean isScript(ByteBuffer tagData) {
        int position = tagData.position();

        try {
            int type = ByteUtils.readUInt8(tagData);
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
