package com.thunisoft.mediax.core.amf;

/**
 * AMF 基本类型
 * 
 * @author chenxh
 *
 */
public final class AMFType {

    public static final int DT_NUMBER = 0;
    public static final int DT_BOOLEAN = 1;
    public static final int DT_STRING = 2;
    public static final int DT_OBJECT = 3;
    public static final int DT_MOVIE_CLIP = 4;
    public static final int DT_NULL = 5;
    public static final int DT_UNDEFINED = 6;
    public static final int DT_REFERENCE = 7;
    public static final int DT_ECMA_ARRAY = 8;
    public static final int DT_END= 9;
    public static final int DT_ARRAY = 10;
    public static final int DT_DATETIME = 11;
    public static final int DT_LONGSTRIING = 12;
    
    public static byte[] EOF = new byte[] {
            0, 0, 9};
    
    private AMFType(){}
    
    public static Void END = Void.getInstance();
}
