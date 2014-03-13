package com.thunisoft.mediax.core.pseudostreaming.flv.h264;

/**
 * Avc Packet Type
 * 
 * <pre>
 * 0: AVC sequence header
 * 1: AVC NALU
 * 2: AVC end of sequence (lower level NALU
 *         sequence ender is not required or supported)
 * @since V1.0  2014-3-13
 * @author chenxh
 */
public enum AvcPacketType {
    Header(0, "sequence header"), 
    NALU(1, "NALU"), 
    End(2, "AVC end of sequence (lower level NALU sequence ender is not required or supported)"), ;

    private int origin;
    private String desript;

    private AvcPacketType(int value, String descript) {
        this.origin = value;
        this.desript = descript;
    }

    public int intValue(){
        return origin;
    }
    
    /**
     * @param type
     * @return
     * @since V1.0 2014-3-13
     * @author chenxh
     * @throws EnumConstantNotPresentException if No {@link AvcPacketType} found! 
     */
    public static AvcPacketType valueOf(int type) {
        for (AvcPacketType item : values()) {
            if (item.origin == type) {
                return item;
            }
        }
       
        throw new EnumConstantNotPresentException(AvcPacketType.class, "type[" + type + "]");
    }
    
    public static String stringValue(int type) {
        for (AvcPacketType item : values()) {
            if (item.origin == type) {
                return item.desript;
            }
        }
        
        return "unknown avc packet type [" + type + "]";
    }
    
    public static boolean isSequenceHeader(int type) {
        return Header.origin == type;
    }
    
    public String getDescript() {
        return "[" + origin + "]-" +desript;
    }
}
