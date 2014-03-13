package com.thunisoft.mediax.core.pseudostreaming.flv.aac;

import org.apache.commons.lang.time.DateFormatUtils;

import com.thunisoft.mediax.core.pseudostreaming.flv.tag.AudioTag;

public class AacAudioTag extends AudioTag {
    private int packetType;
    
    public AacAudioTag(int packetType) {
        this.packetType = packetType;
    }
    
    public String getPacketDescript() {
        return AACPacketType.stringValue(packetType);
    }
    
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        
        b.append("Aac-{type:").append(tagType);
        b.append(", timestamp:").append(DateFormatUtils.format(timestamp, "HH:mm:ss.SSS"));
        b.append(", streamId:").append(streamId);
        b.append(", datasize:").append(dataSize);
        b.append(", packet:").append(getPacketDescript());
        b.append("}");

        return b.toString();
    }

    public int getPacketType() {
        return packetType;
    }
    
}
