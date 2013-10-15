package github.chenxh.media.flv;


public interface ITagHead {

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
    public abstract long getTagHeadSize();

    /**
     * data size in the tag
     * 
     * @return
     */
    public abstract long getBodySize();

    /**
     * type of tag
     * @return
     */
    public abstract int getType();

}