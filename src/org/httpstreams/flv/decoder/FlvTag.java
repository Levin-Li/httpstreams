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
     * 本 Tag 的大小
     * @return
     */
    public long getTagSize ();
    
    /**
     * 对于 FLV 文件, tagSize = datasize + 11 恒成立
     * 
     * @return 数据包大小
     */
    public long getDataSize();
    
    /**
     * 
     * @return 当前时间戳
     */
    public long getTimestamp ();
    
    /**
     * @return streamId
     */
    public int getStreamId ();
    
    /**
     * 文件头部信息
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
