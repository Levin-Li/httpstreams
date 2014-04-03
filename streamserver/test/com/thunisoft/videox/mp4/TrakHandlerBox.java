package com.thunisoft.videox.mp4;

/**
 * <p>
 * @author chenxiuheng@gmail.com
 * @since V1.0
 * @Date 2014-3-30
 */
public class TrakHandlerBox extends FullBox {
    final public static String CONTAINER = "moov.trak.mdia";
    final public static String TYPE = "hdlr";

    final public static String HANDLER_AUDIO = "soun";
    final public static String HANDLER_VIDEO = "vide";
    final public static String HANDLER_HINT = "hint";
    
    private String handlerType;
    private String descript;
    
    public TrakHandlerBox(Box copy) {
        super(copy);
    }
    
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder(TYPE);
        
        b.append("{").append(handlerType);
        b.append(", ").append(descript);
        b.append("}");
        return b.toString();
    }

    public String handlerType() {
        return handlerType;
    }

    public boolean isAudio() {
        return HANDLER_AUDIO.equals(handlerType);
    }
    
    public boolean isVideo() {
        return HANDLER_VIDEO.equals(handlerType);
    }

    public boolean isHint() {
        return HANDLER_HINT.equals(handlerType);
    }

    public void handlerType(String handlerType) {
        this.handlerType = handlerType;
    }



    public String descript() {
        return descript;
    }

    public void descript(String descript) {
        this.descript = descript;
    }
}
