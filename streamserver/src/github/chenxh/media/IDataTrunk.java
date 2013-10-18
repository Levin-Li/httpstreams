package github.chenxh.media;

/**
 * 一个数据块
 * 
 * @author chenxh
 *
 */
public interface IDataTrunk {

    /**
     * Tag size
     * 
     * sum of data size and tag head size
     * @return
     */
    public abstract long size();

    /**
     * tag head size in the tag
     * 
     * @return
     */
    public abstract long getHeadSize();

    /**
     * data size in the tag
     * 
     * @return
     */
    public abstract long getDataSize();

    /**
     * type of tag
     * @return
     */
    public abstract int getTagType();
}