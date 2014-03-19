package com.thunisoft.mediax.core.codec.flv.tag;

import org.apache.commons.lang.time.DateFormatUtils;

public abstract class AbstractFrameTag implements Tag {
    protected int tagType;
    protected long dataSize;
    protected long timestamp;
    protected long streamId;

    public AbstractFrameTag(int type) {
        this.tagType = type;
    }

    public AbstractFrameTag(int type, long dataSize, long timestamp, long streamId) {
        super();
        this.tagType = type;
        this.streamId = streamId;
        this.timestamp = timestamp;
        this.dataSize = dataSize;
    }

    public long getStreamId() {
        return streamId;
    }

    public void setStreamId(long streamId) {
        this.streamId = streamId;
    }

    public int getTagType() {
        return tagType;
    }

    public void setTagType(int type) {
        this.tagType = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getDataSize() {
        return dataSize;
    }

    public void setDataSize(long dataSize) {
        this.dataSize = dataSize;
    }

    
    @Override
    public boolean isKey() {
        return false;
    }

    
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        
        b.append("Tag-{type:").append(tagType);
        b.append(", timestamp:").append(DateFormatUtils.format(timestamp, "HH:mm:ss.SSS"));
        b.append(", streamId:").append(streamId);
        b.append(", datasize:").append(dataSize);
        b.append("}");

        return b.toString();
    }
}
