package com.thunisoft.mediax.core.pseudostreaming.flv.h264;


import org.apache.commons.lang.time.DateFormatUtils;

import com.thunisoft.mediax.core.pseudostreaming.flv.tag.Tag;
import com.thunisoft.mediax.core.pseudostreaming.flv.tag.VideoTag;


public class AvcVideoTag extends VideoTag implements Tag {
    private int packetType;
    private int compositionTime;



    public AvcVideoTag() {
    }

    public AvcVideoTag(long dataSize, long timestamp, long streamId) {
        super(dataSize, timestamp, streamId);
    }

    public int getPacketType() {
        return packetType;
    }

    public void setPacketType(int avcPacketType) {
        this.packetType = avcPacketType;
    }
    
    

    public int getCompositionTime() {
        return compositionTime;
    }

    public void setCompositionTime(int compositionTime) {
        this.compositionTime = compositionTime;
    }

    public String getPacketDescript() {
        return AvcPacketType.stringValue(packetType);
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        
        String frameType = getFrameDescript();
        String codec = getCodecDescript();
        
        b.append("Avc");
        b.append(AvcPacketType.isSequenceHeader(packetType)?"Header":"");
        b.append("-{type:").append(tagType);
        b.append(", timestamp:").append(DateFormatUtils.format(timestamp, "HH:mm:ss.SSS"));
        b.append(", streamId:").append(streamId);
        b.append(", datasize:").append(dataSize);
        b.append(", frameType:").append(frameType);
        b.append(", codec:").append(codec);
        b.append(", packet:").append(getPacketDescript());
        
        b.append("}");
    
        return b.toString();
    }
}
