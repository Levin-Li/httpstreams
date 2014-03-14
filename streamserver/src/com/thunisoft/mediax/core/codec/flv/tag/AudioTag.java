package com.thunisoft.mediax.core.codec.flv.tag;

import org.apache.commons.lang.time.DateFormatUtils;

import com.thunisoft.mediax.core.codec.flv.FlvConsts;

public class AudioTag extends AbstractFrameTag implements Tag {
    /**
     * UB[4]
     * <pre/>
     * 0 = Linear PCM, platform endian
     * 1 = ADPCM
     * 2 = MP3
     * 3 = Linear PCM, little endian
     * 4 = Nellymoser 16-kHz mono
     * 5 = Nellymoser 8-kHz mono
     * 6 = Nellymoser
     * 7 = G.711 A-law logarithmic PCM
     * 8 = G.711 mu-law logarithmic PCM
     * 9 = reserved
     * 10 = AAC
     * 11 = Speex
     * 14 = MP3 8-Khz
     * 15 = Device-specific sound
     */
    private int soundFormat;

    /**
     * UB[2]
     * 0 = 5.5-kHz
     * 1 = 11-kHz
     * 2 = 22-kHz
     * 3 = 44-kHz
     */
    private int soundRate;
    
    /**
     * UB[1]
     * 0 = snd8Bit
     * 1 = snd16Bit
     */
    private int soundSize;
    
    /**
     * UB[1]
     * 0 = sndMono
     * 1 = sndStereo
     */
    private int soundType;

    public AudioTag() {
        super(FlvConsts.TAGTYPE_AUDIO);
    }

    public AudioTag(int type, long dataSize, long timestamp, long streamId) {
        super(type, dataSize, timestamp, streamId);
    }

    public int getSoundFormat() {
        return soundFormat;
    }

    public void setSoundFormat(int soundFormat) {
        this.soundFormat = soundFormat;
    }

    public int getSoundRate() {
        return soundRate;
    }

    public void setSoundRate(int soundRage) {
        this.soundRate = soundRage;
    }

    public int getSoundSize() {
        return soundSize;
    }

    public void setSoundSize(int soundSize) {
        this.soundSize = soundSize;
    }

    public int getSoundType() {
        return soundType;
    }

    public void setSoundType(int soundType) {
        this.soundType = soundType;
    }
    
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        
        b.append("Tag-{type:").append(tagType);
        b.append(", timestamp:").append(DateFormatUtils.format(timestamp, "HH:mm:ss.SSS"));
        b.append(", streamId:").append(streamId);
        b.append(", datasize:").append(dataSize);
        b.append(", soundFormat:").append(soundFormat);
        b.append(", soundRate:").append(soundRate);
        b.append(", soundSize:").append(soundSize);
        b.append(", soundType:").append(soundType);
        b.append("}");

        return b.toString();
    }
}
