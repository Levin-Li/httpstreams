package com.thunisoft.mediax.core.pseudostreaming.flv.aac;

import com.thunisoft.mediax.core.pseudostreaming.flv.h264.AvcPacketType;

public enum AACPacketType {
    Header(0, "sequence header"),
    Raw(1, "raw");

    private int origin;
    private String desript;

    private AACPacketType(int origin, String desript) {
        this.origin = origin;
        this.desript = desript;
    }

    public int intValue(){
        return origin;
    }
    
    
    public static String stringValue(int type) {
        for (AACPacketType item : values()) {
            if (item.origin == type) {
                return item.desript;
            }
        }
        
        return "unknown aac packet type [" + type + "]";
    }

    /**
     * @param type
     * @return
     * @since V1.0 2014-3-13
     * @author chenxh
     * @throws EnumConstantNotPresentException if No {@link AvcPacketType} found! 
     */
    public static AACPacketType valueOf(int type) {
        for (AACPacketType item : values()) {
            if (item.origin == type) {
                return item;
            }
        }
       
        throw new EnumConstantNotPresentException(AvcPacketType.class, "type[" + type + "]");
    }
    
    public static boolean isSequenceHeader(int type) {
        return Header.origin == type;
    }
    
    public String getDescript() {
        return "[" + origin + "]-" +desript;
    }
}
