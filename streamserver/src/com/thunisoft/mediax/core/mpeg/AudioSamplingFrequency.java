package com.thunisoft.mediax.core.mpeg;

public enum AudioSamplingFrequency {
       $0 (0 , 96000),
       $1 (1 , 88200),
       $2 (2 , 64000),
       $3 (3 , 48000),
       $4 (4 , 44100),
       $5 (5 , 32000),
       $6 (6 , 24000),
       $7 (7 , 22050),
       $8 (8 , 16000),
       $9 (9 , 12000),
       $10(10, 11025),
       $11(11, 8000 ),
       $12(12, 7350 ),
       //$13(13, Reserved
       //$14(14, Reserved
       //$15(15, frequency is written explictly
    
    ;

    private int frequency;

    private AudioSamplingFrequency(int index, int frequencies) {
        this.frequency = frequencies;
    }

    /**
     * 
     * @param index
     * @return -1 if index ont in [0, 12]
     * @since V1.0 2014-3-13
     * @author chenxh
     */
    public static int frequencyOf(int index) {
        AudioSamplingFrequency[] values = values();
        return (0<= index && index < values.length) ? values[index].frequency : -1;
    }
}
