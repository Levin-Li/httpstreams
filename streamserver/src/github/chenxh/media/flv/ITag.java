package github.chenxh.media.flv;

public interface ITag extends ITagHead {
    public static final int HEAD = -1;
    public static final int VIDEO = 9;
    public static final int AUDIO = 8;
    public static final int SCRIPT_DATA = 18;
    public static final int OTHERS = -1;
    
    /**
     * pre tag size
     * @return
     */
    public long getPreTagSize();
    
    /**
     * ±êÇ©ÄÚÈÝ
     * 
     * @return
     */
    public ITagData getData();
}
