package com.thunisoft.mediax.core.pseudostreaming.flv;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.FileHandler;

import org.apache.commons.codec.Decoder;
import org.apache.commons.codec.DecoderException;

import com.thunisoft.mediax.core.amf.AMF0Decoder;
import com.thunisoft.mediax.core.amf.AMFArray;
import com.thunisoft.mediax.core.pseudostreaming.flv.tag.AudioTag;
import com.thunisoft.mediax.core.pseudostreaming.flv.tag.FlvHeader;
import com.thunisoft.mediax.core.pseudostreaming.flv.tag.MetaDataTag;
import com.thunisoft.mediax.core.pseudostreaming.flv.tag.Tag;
import com.thunisoft.mediax.core.utils.ByteBufferUtils;
import com.thunisoft.mediax.core.vfs.RandomAccessChannel;

public class FlvDecoder implements Decoder {

    @Override
    public Object decode(Object source) throws DecoderException {
        return null;
    }


    public Object decode(RandomAccessChannel rch) throws DecoderException {
        return null;
    }

    public static class TagIterator implements Iterator<ByteBuffer> {
        private FlvDecoder decoder;
        private RandomAccessChannel channel;

        private long position;

        private FlvHeader flvHead;

        private TagIterator(FlvDecoder decoder, RandomAccessChannel channel) throws IOException {
            super();
            this.decoder = decoder;
            this.channel = channel;

            channel.position(0);
            flvHead = decoder.decodeFileHeader(decoder.nextTag(channel));

            position = channel.position();
        }

        @Override
        public boolean hasNext() {
            try {
                return channel.isOpen() && position < channel.length();
            } catch (IOException e) {
                return false;
            }
        }

        @Override
        public ByteBuffer next() {
            try {
                channel.position(position);
                ByteBuffer tag = decoder.nextTag(channel);
                position = channel.position();
                return tag;
            } catch (IOException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }

        public Tag parse(ByteBuffer frameTag) throws IOException {
            int tagType = decoder.tagType(frameTag);

            Tag tag = null;
            switch (tagType) {
                case TP_VIDEO:

                    break;
                case TP_AUDIO:
                    break;
                case TP_SCRIPT:
                    return decoder.decodeMetadata(frameTag);
                default:
                    break;
            }

            return tag;
        };

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove()");
        }

        public FlvHeader flvHead() {
            return flvHead;
        }

    }

    private MetaDataTag decodeMetadata(ByteBuffer scriptData) throws IOException {
        try {
            scriptData.position(LENGTH_TAGHEAD);

            Object[] items = decodeAMF0(scriptData);
            if (items.length < 2 || "onMetaData".equals(items[0])) {
                throw new IOException("不是一个 metadata tag");
            }

            return new MetaDataTag((AMFArray) items[1]);

        } catch (DecoderException e) {
            throw new IOException("Is Not A Supported Flv File");
        }
    }

    private Object[] decodeAMF0(ByteBuffer tagData) throws DecoderException {
        Decoder decoder = new AMF0Decoder();

        List<Object> items = new LinkedList<Object>();
        while (tagData.remaining() > 0) {
            items.add(decoder.decode(tagData));
        }

        return items.toArray();
    }


    private FlvHeader decodeFileHeader(ByteBuffer bytes) {
        String typeFlag = ByteBufferUtils.read3cc(bytes);
        if (!"flv".equalsIgnoreCase(typeFlag)) {
            throw new IllegalArgumentException("It's not flv file");
        }

        int version = ByteBufferUtils.readUInt8(bytes);
        int flags = ByteBufferUtils.readUInt8(bytes);
        boolean hasAudio = (flags & 0xF0) > 0;
        boolean hasVideo = (flags & 0x0F) > 0;

        return new FlvHeader(version, hasVideo, hasAudio, bytes.slice());
    }

    private AudioTag decodeAudioTag(ByteBuffer bytes) {
        // tag head
        int type = ByteBufferUtils.readUInt8(bytes);
        int dataSize = ByteBufferUtils.readUInt24(bytes);
        long timestamp = ByteBufferUtils.readUInt32(bytes);
        int streamId = ByteBufferUtils.readUInt24(bytes);
        
        // tag data
        
        
        return new AudioTag();
    }

    private ByteBuffer nextTag(RandomAccessChannel ch) throws IOException {
        final long currentPosition = ch.position();
        boolean isHeadOfFile = (currentPosition == 0L);

        if (!isHeadOfFile) {
            // pre tag size
            long newPosition = ch.position() + (long) LENGTH_TAGSIZE;
            ch.position(newPosition);

            // next tag
            return readTag(ch);
        } else {
            return readFileHeader(ch);
        }
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


    private int tagType(ByteBuffer tag) {
        int position = tag.position();

        try {
            return ByteBufferUtils.readUInt8(tag);
        } finally {
            tag.position(position);
        }
    }

    private static final int LENGTH_TAGHEAD = 11;
    private static final int LENGTH_TAGSIZE = 4;


    public static final int TP_SCRIPT = 18;
    public static final int TP_AUDIO = 8;
    public static final int TP_VIDEO = 9;
}
