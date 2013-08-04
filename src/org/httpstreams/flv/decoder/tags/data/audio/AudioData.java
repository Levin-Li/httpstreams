package org.httpstreams.flv.decoder.tags.data.audio;

import java.io.IOException;

import org.httpstreams.flv.StructureInputStream;
import org.httpstreams.flv.decoder.tags.data.TagData;

public class AudioData implements TagData {

    /**
     * 音频格式
     * @see SoundFormat
     */
    private int soundFormat;
    
    /**
     * 声音采样率
     * @see SoundRate
     */
    private int soundRate;
    
    /**
     * Size of each sample. This parameter only pertains to uncompressed
     * formats. Compressed formats always decode to 16 bits internally. 0 =
     * snd8Bit 1 = snd16Bit
     */
    private int soundSize;
    /**
     * Mono or stereo sound 
     * For Nellymoser: always 0 
     * For AAC: always 1
     */
    private int soundType;
    
    /**
     * 声音数据包的大小
     */
    private long dataSize;
    
    @Override
    public void read(StructureInputStream inStream, long packetSize)
            throws IOException {
        int audioHead = inStream.read(); 
        
        soundFormat = audioHead >> 4;
        soundRate = (audioHead >> 2) & 3;
        soundSize = (audioHead >> 1) & 1;
        soundType = (audioHead & 1);
        
        dataSize = packetSize; 
        
        long videoDatalenght = packetSize - 1;
        inStream.skip(videoDatalenght);
    }

    /**
     * 1、Formats 7, 8, 14, and 15 are reserved for internal use
     * 2、AAC is supported in Flash Player 9,0,115,0 and higher.
     * 3、Speex is supported in Flash Player 10 and higher.
     *
     */
    public static enum SoundFormat {
        _0(0, "Linear PCM, platform endian"),
        _1(1, "ADPCM"),
        _2(2, "MP3"),
        _3(3, "Linear PCM, little endian"),
        _4(4, "Nellymoser 16-kHz mono"),
        _5(5, "Nellymoser 8-kHz mono"),
        _6(6, "Nellymoser"),
        _7(7, "G.711 A-law logarithmic PCM"),
        _8(8, "G.711 mu-law logarithmic PCM"),
        _9(9, "reserved"),
        _10(10, "AAC"),
        _11(11, "Speex"),
        _14(14, "MP3 8-Khz"),
        _15(15, "Device-specific sound")
        ;
        final private int code;
        final private String descript;
        /**
         * @param code
         * @param descript
         */
        private SoundFormat(int code, String descript) {
            this.code = code;
            this.descript = descript;
        }

        @Override
        public String toString() {
            return descript;
        }
        
        public static SoundFormat findByCode(int code) {
            for (SoundFormat format : values()) {
                if (format.code == code) {
                    return format;
                }
            }
            
            return null;
        }
    }
    
    /**
     * For AAC: always 3
     */
    public static enum SoundRate {
        _0(0, "5.5-kHz"),
        _1(1, "11-kHz"),
        _2(2, "22-kHz"),
        _3(3, "44-kHz"),
        ;
        final private int code;
        final private String descript;
        /**
         * @param code
         * @param descript
         */
        private SoundRate(int code, String descript) {
            this.code = code;
            this.descript = descript;
        }
        
        @Override
        public String toString() {
            return descript;
        }
        
        public static SoundRate findByCode(int code) {
            for (SoundRate format : values()) {
                if (format.code == code) {
                    return format;
                }
            }
         
            return null;
        }
    }
    
    @Override
    public String toString() {
        SoundFormat format = SoundFormat.findByCode(soundFormat);
        SoundRate rate = SoundRate.findByCode(soundRate);
        
        String size = 0 == soundSize ? "snd8Bit":"snd16Bit";
        String type = 0 == soundType ? "sndMono":"sndStereo";
        return "Audio " + format + ", "+ rate + ", " + type + ", " + size + "; size=[" + dataSize + "]";
    }
}
