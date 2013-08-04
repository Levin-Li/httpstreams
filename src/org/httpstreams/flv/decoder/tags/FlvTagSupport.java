package org.httpstreams.flv.decoder.tags;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.httpstreams.flv.StructureInputStream;
import org.httpstreams.flv.decoder.Flv;
import org.httpstreams.flv.decoder.FlvTag;
import org.httpstreams.flv.decoder.tags.data.TagData;
import org.httpstreams.flv.decoder.tags.data.UnknownTypeData;
import org.httpstreams.flv.decoder.tags.data.audio.AudioData;
import org.httpstreams.flv.decoder.tags.data.scripts.ScriptData;
import org.httpstreams.flv.decoder.tags.data.video.VideoData;


public class FlvTagSupport implements FlvTag {
    private final ScriptData scriptData = new ScriptData();
    private final VideoData videoData = new VideoData();
    private final AudioData audioData = new AudioData();
    private final UnknownTypeData unknownData = new UnknownTypeData();
    
    final private Log logger = LogFactory.getLog(FlvTagSupport.class);

    private Flv header;
    private TagData data = null;

    /**
     * UI8
     * 
     * 8: audio
     * 9: video
     * 18:script data
     * all others: reserved
     */
    private int type;
    
    /**
     * UI24 
     * 
     */
    private int dataSize;
    
    /**
     * UI24(Timestamp) + UI8 (TimestampExtended)
     */
    private long timestamp;
    
    /**
     * UI24,
     * 
     * always 0
     */
    private int streamId;
    /**
     * @param header
     * @param inStream
     */
    public FlvTagSupport(Flv header) {
        super();
        this.header = header;
    }

    @Override
    public long getDataSize() {
        return dataSize;
    }

    @Override
    public Flv getFlv() {
        return header;
    }

    @Override
    public int getStreamId() {
        return streamId;
    }

    @Override
    public long getTagSize() {
        return dataSize + HEAD_SIZE;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public FlvTag next(StructureInputStream inStream) {
        try {
            // 跳过上一个 Tag 的数据包
            release(inStream);

            // 读取头部信息
            readHead(inStream);
            
            // 读取包内容
            readData(inStream, getDataSize());
            return this;
        } catch (IOException e) {
            logger.error("读取 Tag 信息失败!", e);
            throw new NoSuchElementException("读取 Tag 信息失败!");
        }
    }

    private void readHead(StructureInputStream inStream) throws IOException {
        // Tag Type
        this.type = inStream.read();
        if (-1 == type) {
            return;
        }
        
        // Tag DataSize
        this.dataSize = inStream.readUI24();
        
        // timestamp
        long lowTimestamp = inStream.readUI24();
        int largeTimestamp = inStream.readUI8();
        this.timestamp = lowTimestamp | (largeTimestamp << 24);
        this.streamId = inStream.readUI24();
    }
    

    private void readData(StructureInputStream inStream, long packetSize) throws IOException {
        if (isScript()) {
            data = scriptData;
        } else if (isVideo()) {
            data = videoData;
        } else if (isAudio()) {
            data = audioData;
        } else {
            data = unknownData;
            unknownData.setTagtype(type);
        }

        data.read(inStream, packetSize);
    }

    private void release (StructureInputStream inStream) throws IOException {
        data = null;
        dataSize = 0;
        type = FlvTag.TAG_TYPE_UNKNOWN;
        
        data = null;
    }

    @Override
    public TagData getData() throws IOException {
        return data;
    }

    public boolean isScript() {
        return FlvTag.TAG_TYPE_SCRIPT == type;
    }

    public boolean isVideo() {
        return FlvTag.TAG_TYPE_VIDEO == type;
    }

    public boolean isAudio() {
        return FlvTag.TAG_TYPE_AUDIO == type;
    }
    
    @Override
    public boolean isOtherTypes() {
        return !isScript() && !isAudio() && !isVideo();
    }
    
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        
        b.append("Tag:{");
        b.append("type:").append(type);
        b.append(", tagSize: ").append(getTagSize());
        b.append(", dataSize: ").append(dataSize);
        b.append(", timestamp:").append(timestamp);
        b.append(", streamId: ").append(streamId);
        b.append("}");
        
        return b.toString();
    }

}
