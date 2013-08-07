package org.httpstreams.flv.decoder;

import java.io.IOException;

import org.httpstreams.flv.StructureInputStream;
import org.httpstreams.flv.decoder.tags.data.TagData;


public interface FlvTag {
    public static int HEAD_SIZE = 11;
    public final static int TAG_TYPE_AUDIO = 8;
    public final static int TAG_TYPE_VIDEO = 9;
    public final static int TAG_TYPE_SCRIPT = 18;
    public static final int TAG_TYPE_UNKNOWN = -1;
    /**
     * �� Tag �Ĵ�С
     * @return
     */
    public long getTagSize ();
    
    /**
     * ���� FLV �ļ�, tagSize = datasize + 11 �����
     * 
     * @return ���ݰ���С
     */
    public long getDataSize();
    
    /**
     * 
     * @return ��ǰʱ���
     */
    public long getTimestamp ();
    
    /**
     * @return streamId
     */
    public int getStreamId ();
    
    /**
     * �ļ�ͷ����Ϣ
     * 
     * @return flv file header
     */
    public Flv getFlv();
    
    public boolean isScript();

    public boolean isVideo();

    public boolean isAudio();
    
    public boolean isOtherTypes();
    
    public FlvTag next(StructureInputStream inStream);
    
    public TagData getData() throws IOException;

    
}
