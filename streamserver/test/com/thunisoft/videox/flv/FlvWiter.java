package com.thunisoft.videox.flv;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import org.apache.commons.codec.EncoderException;

import com.thunisoft.mediax.core.codec.amf.AMF0Codec;
import com.thunisoft.mediax.core.codec.amf.AMFArray;
import com.thunisoft.videox.flv.format.AudioFormat;
import com.thunisoft.videox.flv.format.VideoFormat;

public class FlvWiter extends DataOutputStream {
    private ByteBuffer tagHead = ByteBuffer.allocate(FlvTag.TAG_HEADER_BYTE_COUNT);


    private final AudioFormat audioFormat;

    public FlvWiter(OutputStream out) {
        super(out);

        audioFormat = new AudioFormat();
        audioFormat.soundFormat(AudioFormat.SOUND_FORMAT_AAC);
    }

    public void writeFlvHead(FLVHead head) throws IOException {
        ByteBuffer data = head.getData();

        writeByteBuffer(data);

        writePreTagSize(0);
    }

    public void writeByteBuffer(ByteBuffer data) throws IOException {
        byte[] buffer = new byte[sizeOf(data)];
        data.get(buffer);

        write(buffer);
    }



    public AudioFormat getAudioFormat() {
        return audioFormat;
    }

    public void writeMetaData(AMFArray metadata) throws IOException {
        try {
            ByteBuffer data = AMF0Codec.encode(new Object[] {
                    "onMetaData", metadata});

            int datasize = sizeOf(data);

            writeTagHead(FlvTag.TAG_TYPE_SCRIPTDATAOBJECT, 0, datasize);

            writeByteBuffer(data);

            writePreTagSize(datasize + FlvTag.TAG_HEADER_BYTE_COUNT);
        } catch (EncoderException e) {
            throw new IllegalArgumentException("编码失败!");
        }
    }

    public void writeAacConfig(ByteBuffer aacConfig) throws IOException {
        int datasize = sizeOf(aacConfig) + 2;
        
        // tag head
        writeTagHead(FlvTag.TAG_TYPE_AUDIO, 0, datasize);
        
        // data
        writeByte(audioFormat.soundFormatByte());
        writeByte(AudioFormat.SOUND_TYPE_SEQUENCE_HEADER);
        writeByteBuffer(aacConfig);
        
        // pre tag size
        writePreTagSize(datasize + FlvTag.TAG_HEADER_BYTE_COUNT);
    }
    
    public void writeAacPacket(long timestamp, ByteBuffer aacConfig) throws IOException {
        int datasize = sizeOf(aacConfig) + 2;
        
        // tag head
        writeTagHead(FlvTag.TAG_TYPE_AUDIO, timestamp, datasize);
        
        // data
        writeByte(audioFormat.soundFormatByte());
        writeByte(AudioFormat.SOUND_TYPE_RAW);
        writeByteBuffer(aacConfig);
        
        // pre tag size
        writePreTagSize(datasize + FlvTag.TAG_HEADER_BYTE_COUNT);
    }
    
    public void writeAvcConfig(ByteBuffer avcConfig) throws IOException {
        writeAvcPacket(0, 0, true, VideoFormat.AVC_PACKET_TYPE_SEQUENCE_HEADER, avcConfig);
    }

    public void writeAvcPacket(long timestamp, long compositeTime, boolean isKeyFrame,
            ByteBuffer avcPacket) throws IOException {
        writeAvcPacket(timestamp, compositeTime, isKeyFrame, VideoFormat.AVC_PACKET_TYPE_NALU,
                avcPacket);
    }

    private void writeAvcPacket(long timestamp, long compositeTime, boolean isKeyFrame,
            int packetType, ByteBuffer avcPacket) throws IOException {
        int datasize = sizeOf(avcPacket) + 5;

        writeTagHead(FlvTag.TAG_TYPE_VIDEO, timestamp, datasize);

        // UI8: FrameType + CodecID
        int firstByte = 0;
        if (isKeyFrame) {
            firstByte =
                    ((VideoFormat.FRAME_TYPE_KEYFRAME << 4) & 0xF0)
                            | (VideoFormat.CODEC_ID_AVC & 0x0F);
        } else {
            firstByte =
                    ((VideoFormat.FRAME_TYPE_INTER << 4) & 0xF0)
                            | (VideoFormat.CODEC_ID_AVC & 0x0F);
        }
        writeByte(firstByte);

        // avc packet type
        writeByte(packetType);

        // composite time SI24
        // 提前解码时间，
        writeByte((int) ((compositeTime >> 16) & 0xFF));
        writeByte((int) ((compositeTime >> 8) & 0xFF));
        writeByte((int) ((compositeTime >> 0) & 0xFF));

        // avc packet
        writeByteBuffer(avcPacket);


        writePreTagSize(datasize + FlvTag.TAG_HEADER_BYTE_COUNT);
    }

    private void writeTagHead(final int type, final long timestamp, int datasize)
            throws IOException {
        ByteBuffer headBuffer = getTagHeadBuffer();

        // type, data size
        headBuffer.putInt(((type << 24) & 0xFF000000) | (datasize & 0x00FFFFFF));

        // timestamp
        headBuffer.put((byte) ((timestamp >> 16) & 0xFF));
        headBuffer.put((byte) ((timestamp >> 8) & 0xFF));
        headBuffer.put((byte) ((timestamp >> 0) & 0xFF));
        headBuffer.put((byte) ((timestamp >> 24) & 0xFF));

        // stream id
        headBuffer.put((byte) 0);
        headBuffer.put((byte) 0);
        headBuffer.put((byte) 0);

        // tag head
        headBuffer.flip();
        writeByteBuffer(headBuffer);
    }

    private int sizeOf(ByteBuffer tagData) {
        return tagData.limit() - tagData.position();
    }

    private void writePreTagSize(long size) throws IOException {
        writeByte((byte) (size >> 24));
        writeByte((byte) (size >> 16));
        writeByte((byte) (size >> 8));
        writeByte((byte) (size >> 0));
    }

    private ByteBuffer getTagHeadBuffer() {
        tagHead.clear();

        return tagHead;
    }
}
