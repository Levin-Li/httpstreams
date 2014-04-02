package com.thunisoft.videox.flv.format;


public class VideoFormat {
    public static final int FRAME_TYPE_KEYFRAME = 1;
    public static final int FRAME_TYPE_INTER = 2;
    public static final int FRAME_TYPE_DISPOSABLE_INTER = 3;
    public static final int FRAME_TYPE_GENERATED_KEYFRAME = 4;
    public static final int FRAME_TYPE_INFO = 5;
    
    public static final int CODEC_ID_JPEG = 1;
    public static final int CODEC_ID_SORENSON = 2;
    public static final int CODEC_ID_SCREEN = 3;
    public static final int CODEC_ID_VP6 = 4;
    public static final int CODEC_ID_VP6_ALPHA = 5;
    public static final int CODEC_ID_SCREEN_V2 = 6;
    public static final int CODEC_ID_AVC = 7;

    public static final int AVC_PACKET_TYPE_SEQUENCE_HEADER = 0;
    public static final int AVC_PACKET_TYPE_NALU = 1;
    public static final int AVC_PACKET_TYPE_END_OF_SEQUENCE = 2;
    
    public static final int INFO_PACKET_SEEK_START = 0;
    public static final int INFO_PACKET_SEEK_END = 1;
    
    /**
     * @see #FRAME_TYPE_INFO video or info frame. 作为H264解码的关键
     * @see #FRAME_TYPE_KEYFRAME key frame
     * @see #FRAME_TYPE_INTER inter frame
     * @see #FRAME_TYPE_DISPOSABLE_INTER disposable inner frame(H263)
     * @see #FRAME_TYPE_GENERATED_KEYFRAME generated keyframe 
     */
    private int frameType;


    private final int codeId = CODEC_ID_AVC;

    /**
     * @see #AVC_PACKET_TYPE_SEQUENCE_HEADER
     * @see #AVC_PACKET_TYPE_NALU
     * @see #AVC_PACKET_TYPE_END_OF_SEQUENCE
     */
    private int packetType;
    
    /**
     * if AVCPacketType == 1
     *   Composition time offset
     * else
     *   0
     */
    private long compositionTime;
    
    /**
     * @see #frameType
     * @param frameType
     * @since V1.0 2014-3-31
     * @author chenxh
     */
    public void frameType(int frameType) {
        this.frameType = frameType;
    }

    public int frameType() {
        return frameType;
    }
    
    public int codeId() {
        return codeId;
    }

    public int packetType() {
        return packetType;
    }

    public void packetType(int packetType) {
        this.packetType = packetType;
    }

    public long compositionTime() {
        return compositionTime;
    }

    public void compositionTime(long compositionTime) {
        this.compositionTime = compositionTime;
    }
    
}
