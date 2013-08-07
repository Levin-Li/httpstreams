package org.httpstreams.flv.decoder;

import java.io.IOException;
import java.io.InputStream;

public interface Flv {

    public abstract int getVersion();

    public abstract boolean hasVideo();

    public abstract boolean hasAudio();

    /**
     * FLV �ļ���ͷ������
     * @return
     */
    public abstract long getHeadLength();
    
    /**
     * ��һ��  Tag �ļ�
     * @return  null is not Tag
     * @throws IOException 
     */
    public FlvTagIterator getTagIterator() throws IOException;
    
    public InputStream seek (double timestamp) throws IOException;

}