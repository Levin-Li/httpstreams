package org.httpstreams.flv.decoder;

import java.io.IOException;

public interface Flv {

    public abstract int getVersion();

    public abstract boolean hasVideo();

    public abstract boolean hasAudio();

    /**
     * FLV 文件的头部长度
     * @return
     */
    public abstract long getHeadLength();
    
    /**
     * 下一个  Tag 文件
     * @return  null is not Tag
     * @throws IOException 
     */
    public FlvTagIterator getTagIterator() throws IOException;
    
    public FlvTagIterator seek (double timestamp) throws IOException;

}