package com.thunisoft.mediax.core.pseudostreaming.flv.h264;



public class AvcConfigTag extends AvcVideoTag {

    private byte[] videoData;

    public AvcConfigTag(byte[] decordConfigs, int packetType, int compositionTime) {
        this.videoData = decordConfigs;
        
        setPacketType(packetType);
        setCompositionTime(compositionTime);
    }

    public byte[] avcDecoderConfigurationRecord() {
        return videoData;
    }
}
