package com.thunisoft.mediax.core.mpeg;

import com.thunisoft.mediax.core.pseudostreaming.flv.aac.AACPacketType;

public enum AudioObjectType {
        Null(0 , 0 , "Null"),
        AAC_MAIN(1 , 1 , "AAC Main"),
        AAC_LC(2 , 1 , "AAC LC (Low Complexity)"),
        AAC_SSR(3 , 1 , "AAC SSR (Scalable Sample Rate)"),
        AAC_LPT(4 , 1 , "AAC LTP (Long Term Prediction)"),
        SBR(5 , 5 , "SBR (Spectral Band Replication)"),
        SSC_SCALABLE(6 , 1 , "AAC Scalable"),
        TwinVQ(7 , 7 , "TwinVQ"),
        CELP(8 , 8 , "CELP (Code Excited Linear Prediction)"),
        HXVC(9 , 9 , "HXVC (Harmonic Vector eXcitation Coding)"),
        //(10, 10, "Reserved"),
        //(11, 11, "Reserved"),
        TTSI(12, 12, "TTSI (Text-To-Speech Interface)"),
        Main_Synthesis(13, 13, "Main Synthesis"),
        Wavetable_Synthesis(14, 13, "Wavetable Synthesis"),
        General_MIDI(15, 15, "General MIDI"),
        ASAF(16, 16, "Algorithmic Synthesis and Audio Effects"),
        //(17, 17, "ER (Error Resilient) AAC LC"),
        //(18, 18, "Reserved"),
        //(19, 19, "ER AAC LTP"),
        //(20, 20, "ER AAC Scalable"),
        //(21, 21, "ER TwinVQ"),
        //(22, 22, "ER BSAC (Bit-Sliced Arithmetic Coding)"),
        //(23, 23, "ER AAC LD (Low Delay)"),
        //(24, 24, "ER CELP"),
        //(25, 25, "ER HVXC"),
        //(26, 26, "ER HILN (Harmonic and Individual Lines plus Noise)"),
        //(27, 27, "ER Parametric"),
        SSC(28, 28, "SSC (SinuSoidal Coding)"),
        PS(29, 29, "PS (Parametric Stereo)"),
        MPEG_SURROUND(30, 30, "MPEG Surround"),
        //(31, 31, "(Escape value)"),
        Layer_1(32, 32, "Layer-1"),
        Layer_2(33, 33, "Layer-2"),
        Layer_3(34, 34, "Layer-3"),
        DST(35, 35, "DST (Direct Stream Transfer)"),
        ALS(36, 36, "ALS (Audio Lossless)"),
        SLS(37, 37, "SLS (Scalable LosslesS)"),
        SLS_NON_CORE(38, 38, "SLS non-core"),
        //(39, 39, "ER AAC ELD (Enhanced Low Delay)"),
        SMR_Simple(40, 40, "SMR (Symbolic Music Representation) Simple"),
        SMR_MAIN(41, 41, "SMR Main"),
        USAC_NO_SBR(42, 42, "USAC (Unified Speech and Audio Coding) (no SBR)"),
        SAOC(43, 43, "SAOC (Spatial Audio Object Coding)"),
        LD_MPEG_Surround(44, 44, "LD MPEG Surround"),
        USAC(45, 45, "USAC")
    ;
    private int origin;
    private int groupType;
    private String descript;
    private AudioObjectType(int origin, int groupType, String descript) {
        this.origin = origin;
        this.groupType = groupType;
        this.descript = descript;
    }

    public static AudioObjectType valueOf(int origin){
        AudioObjectType[] types = values();
        for (int i = 0; i < types.length; i++) {
            if (types[i].origin == origin) {
                return types[i];
            }
        }
        
        throw new EnumConstantNotPresentException(AudioObjectType.class, "type[" + origin + "]");
    }
    
    public static String stringValue(int type) {
        AudioObjectType[] types = values();
        for (int i = 0; i < types.length; i++) {
            if (types[i].origin == type) {
                return types[i].descript;
            }
        }
        
        return "unknown Audio Object Type [" + type + "]";
    }
    
    private AudioObjectType(int origin, String descript) {
        this.origin = origin;
        this.groupType = origin;
        this.descript = descript;
    }

    public int getOrigin() {
        return origin;
    }

    public int getGroupType() {
        return groupType;
    }

    public String getDescript() {
        return descript;
    }
    
    
}
