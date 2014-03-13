package com.thunisoft.mediax.core.pseudostreaming.flv.aac;

import org.apache.commons.lang.time.DateFormatUtils;

import com.thunisoft.mediax.core.mpeg.AudioObjectType;
import com.thunisoft.mediax.core.mpeg.AudioSamplingFrequency;

public class AacConfigTag extends AacAudioTag {
    /**
     * @SEE {@link AudioObjectType}
     */
    private int audioObjectType;
    
    /**
     * @see AudioSamplingFrequency
     */
    private int sampleFrequencyIndex;

    /**
     * 声道
     */
    private int channel;

    private byte[] audioData;

    public AacConfigTag(int packetType, int audioObjectType, int sampleFrequencyIndex,
            int channel, byte[] decordConfig) {
        super(packetType);
        this.audioObjectType = audioObjectType;
        this.sampleFrequencyIndex = sampleFrequencyIndex;
        this.channel = channel;
        this.audioData = decordConfig;
    }

    private AudioObjectType getAudioObjectDescript() {
        return AudioObjectType.valueOf(audioObjectType);
    }

    private int getFrequency() {
        return AudioSamplingFrequency.frequencyOf(sampleFrequencyIndex);
    }
    
    
    public int getAudioObjectType() {
        return audioObjectType;
    }

    public void setAudioObjectType(int audioObjectType) {
        this.audioObjectType = audioObjectType;
    }

    public int getSampleFrequencyIndex() {
        return sampleFrequencyIndex;
    }

    public void setSampleFrequencyIndex(int sampleFrequencyIndex) {
        this.sampleFrequencyIndex = sampleFrequencyIndex;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public byte[] getAudioData() {
        return audioData;
    }

    public void setAudioData(byte[] decordConfig) {
        this.audioData = decordConfig;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        
        b.append("AacHeader-{");   
        b.append("type:").append(tagType);     
        b.append(", timestamp:").append(DateFormatUtils.format(timestamp, "HH:mm:ss.SSS"));
        b.append(", streamId:").append(streamId);
        b.append(", datasize:").append(dataSize);
        b.append(", audioObjectType:").append(getAudioObjectDescript());
        b.append(", frequency:").append(getFrequency());
        b.append(", channel:").append(channel);

        b.append("}");
        
        return b.toString();
    }


}
