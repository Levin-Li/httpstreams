package com.thunisoft.mediax.core.pseudostreaming.flv;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;

import org.apache.commons.codec.Decoder;
import org.apache.commons.codec.DecoderException;
import org.slf4j.LoggerFactory;

import com.thunisoft.mediax.core.amf.AMF0Decoder;
import com.thunisoft.mediax.core.amf.AMFArray;
import com.thunisoft.mediax.core.pseudostreaming.flv.aac.AacPacketDecoder;
import com.thunisoft.mediax.core.pseudostreaming.flv.h264.AvcPacketDecoder;
import com.thunisoft.mediax.core.pseudostreaming.flv.tag.AudioTag;
import com.thunisoft.mediax.core.pseudostreaming.flv.tag.FlvHeader;
import com.thunisoft.mediax.core.pseudostreaming.flv.tag.MetaDataTag;
import com.thunisoft.mediax.core.pseudostreaming.flv.tag.Tag;
import com.thunisoft.mediax.core.pseudostreaming.flv.tag.VideoTag;
import com.thunisoft.mediax.core.utils.ByteUtils;
import com.thunisoft.mediax.core.vfs.RandomAccessChannel;
import com.thunisoft.mediax.core.vfs.local.RandomAccessFileChannelImpl;

public class FlvDecoder implements Decoder {
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(FlvDecoder.class);

    @Override
    public TagIterator decode(Object source) throws DecoderException {

        try {
            if (source instanceof File) {
                return decode(new RandomAccessFileChannelImpl((File) source));
            }
        } catch (Exception e) {
            logger.warn("解码失败，原因：" + e.getMessage(), e);
        }

        throw new DecoderException("can't decode: " + source);
    }


    public TagIterator decode(RandomAccessChannel rch) throws DecoderException, IOException {
        return new TagIterator(this, rch);
    }

    public static class TagIterator implements Iterator<ByteBuffer> {
        private FlvDecoder decoder;
        private RandomAccessChannel channel;

        private ByteBuffer preTagSize = ByteBuffer.allocate(4);
        private FlvHeader flvHead;

        private TagIterator(FlvDecoder decoder, RandomAccessChannel channel) throws IOException {
            super();
            this.decoder = decoder;
            this.channel = channel;

            channel.position(0);
            flvHead = decoder.decodeFileHeader(decoder.readFileHeader(channel));
        }

        @Override
        public boolean hasNext() {
            try {
                if (channel.isOpen()) {
                    return channel.position() + (LENGTH_TAGSIZE + LENGTH_TAGHEAD) < channel
                                                                                           .length();
                } else {
                    return false;
                }
            } catch (IOException e) {
                return false;
            }
        }

        @Override
        public ByteBuffer next() {
            try {
                int preSize = preTagSize();
                logger.debug("preTagSize: {}", preSize);

                // next data
                logger.debug("tagPosition: {}", channel.position());
                ByteBuffer tagData = decoder.readTag(channel);

                return tagData;
            } catch (IOException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }

        private int preTagSize() throws IOException {
            preTagSize.clear();

            // pre tag size
            ByteUtils.readFull(channel, preTagSize);
            preTagSize.flip();

            return preTagSize.getInt();
        }

        public Tag nextTag() throws DecoderException {
            ByteBuffer buffer = next();

            return parse(buffer);
        }

        public Tag parse(ByteBuffer frameTag) throws DecoderException {
            return decoder.decodeTag(frameTag);
        };

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove()");
        }

        public FlvHeader flvHead() {
            return flvHead;
        }

    }

    private MetaDataTag decodeMetadata(ByteBuffer scriptData) throws DecoderException {
        scriptData.position(LENGTH_TAGHEAD);

        Object[] items = decodeAMF0(scriptData);
        if (items.length < 2 || !"onMetaData".equals(items[0])) {
            throw new IllegalArgumentException("不是一个 metadata tag");
        }

        return new MetaDataTag((AMFArray) items[1]);
    }


    private Object[] decodeAMF0(ByteBuffer tagData) throws DecoderException {
        AMF0Decoder decoder = new AMF0Decoder();

        return decoder.decode(tagData);
    }


    private FlvHeader decodeFileHeader(ByteBuffer bytes) {
        String typeFlag = ByteUtils.read3cc(bytes);
        if (!"flv".equalsIgnoreCase(typeFlag)) {
            throw new IllegalArgumentException("It's not flv file");
        }

        int version = ByteUtils.readUInt8(bytes);
        int flags = ByteUtils.readUInt8(bytes);
        boolean hasAudio = (flags & 0xF0) > 0;
        boolean hasVideo = (flags & 0x0F) > 0;

        return new FlvHeader(version, hasVideo, hasAudio, bytes.slice());
    }

    private Tag decodeTag(ByteBuffer frameTag) throws DecoderException {
        int tagType = typeOf(frameTag);


        switch (tagType) {
            case Tag.VIDEO:
                return decodeVideoTag(frameTag);
            case Tag.AUDIO:
                return decodeAudioTag(frameTag);
            case Tag.SCRIPT:
                return decodeMetadata(frameTag);
            default:
                throw new DecoderException("unsupport tag type [" + tagType + "]");
        }
    }

    private AudioTag decodeAudioTag(ByteBuffer bytes) throws DecoderException {
        // tag head
        int type = ByteUtils.readUInt8(bytes);
        int dataSize = ByteUtils.readUInt24(bytes);
        long timestamp = ByteUtils.readUInt32(bytes);
        int streamId = ByteUtils.readUInt24(bytes);

        // tag data
        int soundConfig = ByteUtils.readUInt8(bytes);
        int soundFormat = (soundConfig >> 4) & 0xF;
        int soundRate = (soundConfig >> 2) & 0x3;
        int soundSize = (soundConfig >> 1) & 0x1;
        int soundType = (soundConfig >> 0) & 0x1;
        
        AudioTag tag;
        if (10 == soundFormat) { // aac
            tag = AacPacketDecoder.decodeAvcVideoTag(bytes);
        } else {
            tag = new AudioTag();
        }

        tag.setSoundFormat(soundFormat);
        tag.setSoundRate(soundRate);
        tag.setSoundSize(soundSize);
        tag.setSoundType(soundType);
        
        tag.setTagType(type);
        tag.setDataSize(dataSize);
        tag.setTimestamp(timestamp);
        tag.setStreamId(streamId);
        return tag;
    }


    private Tag decodeVideoTag(ByteBuffer bytes) throws DecoderException {
        // tag head
        int type = ByteUtils.readUInt8(bytes);
        int dataSize = ByteUtils.readUInt24(bytes);
        long timestamp = ByteUtils.readUInt32(bytes);
        int streamId = ByteUtils.readUInt24(bytes);

        // tag data
        int frameKey = ByteUtils.readUInt8(bytes);
        int frameType = (frameKey & 0xF0) >> 4;
        int codeId = (frameKey & 0x0F) >> 0;


        VideoTag tag;

        int frameSequence = -1;
        if (frameType == 5) { // video info/command frame, only UI8
            frameSequence = ByteUtils.readUInt8(bytes);

            tag = new VideoTag();
        } else if (codeId == 7) { // AVCVIDEOPACKET
            tag = AvcPacketDecoder.decodeAvcVideoTag(bytes);
        } else {
            tag = new VideoTag();
        }
        
        if (frameSequence == 0) {
            logger.warn("Start of client-side seeking video frame sequence");
        } else if (frameSequence == 1) {
            logger.warn("End of client-side seeking video frame sequence");
        }

        tag.setTagType(type);
        tag.setDataSize(dataSize);
        tag.setTimestamp(timestamp);
        tag.setStreamId(streamId);
        tag.setFrameType(frameType);
        tag.setCodecId(codeId);
        return tag;
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
        ch.readFull(content);

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

        // tag, tagSize = headsize + datasize
        ch.position(startPosition);
        int tagSize = LENGTH_TAGHEAD + dataSize;
        return ByteUtils.readFull(ch, tagSize);
    }


    private int typeOf(ByteBuffer tag) {
        int position = tag.position();

        try {
            return ByteUtils.readUInt8(tag);
        } finally {
            tag.position(position);
        }
    }

    private static final int LENGTH_TAGHEAD = 11;
    private static final int LENGTH_TAGSIZE = 4;


}
