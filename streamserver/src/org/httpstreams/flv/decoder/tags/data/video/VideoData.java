package org.httpstreams.flv.decoder.tags.data.video;

import java.io.IOException;

import org.httpstreams.flv.StructureInputStream;
import org.httpstreams.flv.decoder.tags.data.TagData;

public class VideoData implements TagData {

    private int frameType;
    private int codecId;

    private long datasize;

    public void read(StructureInputStream inStream, long length)
            throws IOException {
        this.datasize = length;

        int videohead = inStream.read();
        frameType = videohead >> 4;
        codecId = videohead & 0xf;

        long videoDatalenght = length - 1;
        inStream.skip(videoDatalenght);
    }

    public boolean isKeyFrame() {
        return frameType == FrameType.KeyFrame.code;
    }
    
    public FrameType getFrameType () {
        for (FrameType type : FrameType.values()) {
            if (type.code == frameType) {
                return type;
            }
        }
        
        throw new IllegalArgumentException("未知的帧类型[" + frameType + "]");
    }
    
    
    public VCodecType getVCodecType () {
        for (VCodecType type : VCodecType.values()) {
            if (type.code == codecId) {
                return type;
            }
        }
        
        throw new IllegalArgumentException("未知的解码器[" + codecId + "]");
    }

    public static enum FrameType {
        KeyFrame(1, "for AVC, a seekable frame"), 
        InnerFrame(2, "for AVC, a non-seekable frame"),
        DisposableInterFrame(3, "H.263 only"),
        GeneratedKeyFrame(4, "reserved for server user only"), 
        Others(5, "video info or command frame");
        private int code;
        private String descirpt;
        FrameType(int code, String descript) {
            this.code = code;
            this.descirpt = descript;
        }
        public final String getDescirpt() {
            return descirpt;
        }

    }

    public static enum VCodecType {
        JPEG(1, "currently unused"),
        H_263(2, "Sorenson H.263"),
        Screen_Video(3, "Screen Video"),
        On2_VP6(4, "On2 VP6"),
        On2_VP6_With_Alpha_Channel(5, "On2 VP6 with alpha channel"),
        Screen_Video_V2(6, "Screen video version 2"),
        AVC(7, "AVC")
        ;
        private int code;
        private String descirpt;
        VCodecType(int code, String descript) {
            this.code = code;
            this.descirpt = descript;
        }
        public final String getDescirpt() {
            return descirpt;
        }
    }
    
    @Override
    public String toString() {
        return "Video " + getFrameType() + "[" + getVCodecType() + "], size=[" + datasize + "]";
    }

}
