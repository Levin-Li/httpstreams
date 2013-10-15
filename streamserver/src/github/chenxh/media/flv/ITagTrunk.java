package github.chenxh.media.flv;

import github.chenxh.media.IDataTrunk;

public interface ITagTrunk extends IDataTrunk {
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
    
    public long getTimestamp();
}
