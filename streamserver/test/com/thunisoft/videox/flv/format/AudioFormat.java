package com.thunisoft.videox.flv.format;

import com.thunisoft.videox.flv.FlvTagData;

public class AudioFormat implements FlvTagData {

    public static final int SOUND_FORMAT_LINEAR = 0;
    public static final int SOUND_FORMAT_ADPCM = 1;
    public static final int SOUND_FORMAT_MP3 = 2;
    public static final int SOUND_FORMAT_LINEAR_LE = 3;
    public static final int SOUND_FORMAT_NELLYMOSER_16K = 4;
    public static final int SOUND_FORMAT_NELLYMOSER_8K = 5;
    public static final int SOUND_FORMAT_NELLYMOSER = 6;
    public static final int SOUND_FORMAT_G711A = 7;
    public static final int SOUND_FORMAT_G711U = 8;
    public static final int SOUND_FORMAT_AAC = 10;
    public static final int SOUND_FORMAT_SPEEX = 11;
    public static final int SOUND_FORMAT_MP3_8K = 14;
    public static final int SOUND_FORMAT_DEVICE_SPECIFIC = 15;

    public static final double SOUND_RATE_5K = 5512.5;
    public static final double SOUND_RATE_11K = 11025;
    public static final double SOUND_RATE_22K = 22050;
    public static final double SOUND_RATE_44K = 44100;

    public static final int SOUND_SIZE_8BITS = 8;
    public static final int SOUND_SIZE_16BITS = 16;

    public static final int SOUND_TYPE_SEQUENCE_HEADER = 0;
    public static final int SOUND_TYPE_RAW = 1;

    public static final int SOUND_CHANNELS_MONO = 1;
    public static final int SOUND_CHANNELS_STEREO = 2;


    private int soundFormatByte;

    private boolean isAACSequenceHeader;

    public int soundFormatByte() {
        return soundFormatByte;
    }

    public void soundFormatByte(int value) {
        soundFormatByte = value;
    }

    public int soundFormat() {
        return (soundFormatByte >> 4) & 0x0f;
    }

    public void soundFormat(int value) {
        soundFormatByte &= 0x0f; // clear upper 4 bits
        soundFormatByte |= (value << 4) & 0xf0;

        if (value == SOUND_FORMAT_AAC) {
            soundRate(SOUND_RATE_44K);
            soundChannels(SOUND_CHANNELS_STEREO);
            isAACSequenceHeader = false; // reasonable default
        }
    }

    public double soundRate() {
        switch ((soundFormatByte >> 2) & 0x03) {
            case 0:
                return SOUND_RATE_5K;
            case 1:
                return SOUND_RATE_11K;
            case 2:
                return SOUND_RATE_22K;
            case 3:
                return SOUND_RATE_44K;
        }

        throw new Error("get soundRate() a two-bit number wasn't 0, 1, 2, or 3. impossible.");
    }

    public void soundRate(double value) {
        int setting;

        if (Math.abs(value - SOUND_RATE_5K) < 1) {
            setting = 0;
        } else if (Math.abs(value - SOUND_RATE_11K) < 1) {
            setting = 1;
        } else if (Math.abs(value - SOUND_RATE_22K) < 1) {
            setting = 2;
        } else if (Math.abs(value - SOUND_RATE_44K) < 1) {
            setting = 3;
        } else {
            throw new IllegalArgumentException(
                    "set soundRate valid values 5512.5, 11025, 22050, 44100");
        }

        soundFormatByte &= 0xf3; // clear upper two bits of lower 4 bits
        soundFormatByte |= (setting << 2);
    }

    public int soundSize() {
        if (((soundFormatByte >> 1) & 0x01) > 0) {
            return SOUND_SIZE_16BITS;
        } else {
            return SOUND_SIZE_8BITS;
        }
    }

    public void soundSize(int value) {
        switch (value) {
            case SOUND_SIZE_8BITS:
                soundFormatByte &= 0xfd; // clear second bit up
                break;
            case SOUND_SIZE_16BITS:
                soundFormatByte |= 0x02; // set second bit up
                break;
            default:
                throw new IllegalArgumentException("set soundSize valid values 8, 16");
        }
    }

    public int soundChannels() {
        if ((soundFormatByte & 0x01) > 0) {
            return SOUND_CHANNELS_STEREO;
        } else {
            return SOUND_CHANNELS_MONO;
        }
    }

    public void soundChannels(int value) {
        switch (value) {
            case SOUND_CHANNELS_MONO:
                soundFormatByte &= 0xfe; // clear lowest bit
                break;
            case SOUND_CHANNELS_STEREO:
                soundFormatByte |= 0x01; // set lowest bit
                break;
            default:
                throw new IllegalArgumentException("set soundChannels valid values 1, 2");
        }
    }

    public boolean isAACSequenceHeader() {
        return isAACSequenceHeader;
    }

    public void isAACSequenceHeader(boolean isAACSequenceHeader) {
        this.isAACSequenceHeader = isAACSequenceHeader;
    }
}
