package com.thunisoft.mediax.core.codec.flv.tag;

import org.apache.commons.lang.time.DateFormatUtils;

import com.thunisoft.mediax.core.codec.flv.FlvConsts;



public class VideoTag extends AbstractFrameTag {

    private int frameType;
    private int codecId;

    public VideoTag() {
        super(FlvConsts.TAGTYPE_VIDEO);
    }

    public VideoTag(long dataSize, long timestamp, long streamId) {
        super(FlvConsts.TAGTYPE_VIDEO, dataSize, timestamp, streamId);
    }

    public int getFrameType() {
        return frameType;
    }

    public void setFrameType(int frameType) {
        this.frameType = frameType;
    }

    public int getCodecId() {
        return codecId;
    }

    public void setCodecId(int codecId) {
        this.codecId = codecId;
    }

    public String getFrameDescript() {
        String descript;
        switch (this.frameType) {
            case 1:
                descript ="keyFrame";
                break;
            case 2:
                descript ="interFrame";
                break;
            case 3:
                descript ="disposableInterFrame";
                break;
            case 4:
                descript ="generatedKeyFrame";
                break;
            case 5:
                descript ="videoInfo/commandFrame";
                break;
            default:
                descript = "unknown[" + this.frameType + "]";
                break;
        }
        return descript;
    }

    public String getCodecDescript() {
        String descript;
        switch (this.codecId) {
            case 1:
                descript = "JPEG (currently unused)";
                break;
            case 2:
                descript = "Sorenson H.263";
                break;
            case 3:
                descript = "Screen video";
                break;
            case 4:
                descript = "On2 VP6";
                break;
            case 5:
                descript = "On2 VP6 with alpha channel";
                break;
            case 6:
                descript = "Screen video version 2";
                break;
            case 7:
                descript = "AVC";
                break;
            default:
                descript = "unknown[" + this.codecId + "]";
                break;
        }
    
        return descript;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        
        String frameType = getFrameDescript();
        String codec = getCodecDescript();
        
        b.append("Tag-{type:").append(tagType);
        b.append(", timestamp:").append(DateFormatUtils.format(timestamp, "HH:mm:ss.SSS"));
        b.append(", streamId:").append(streamId);
        b.append(", datasize:").append(dataSize);
        b.append(", frameType:").append(frameType);
        b.append(", codec:").append(codec);
        
    
        b.append("}");
    
        return b.toString();
    }

}
