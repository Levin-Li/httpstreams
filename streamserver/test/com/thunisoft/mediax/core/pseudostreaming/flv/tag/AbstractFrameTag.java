package com.thunisoft.mediax.core.pseudostreaming.flv.tag;

import org.apache.commons.lang.time.DateFormatUtils;

public abstract class AbstractFrameTag implements Tag {
    protected int type;
    protected long dataSize;
    protected long timestamp;
    protected long streamId;

    public AbstractFrameTag(int type) {
        this.type = type;
    }

    public AbstractFrameTag(int type, long dataSize, long timestamp, long streamId) {
        super();
        this.type = type;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
    public String toString() {
        StringBuilder b = new StringBuilder();
        
        b.append("Tag-{type:").append(type);
        b.append(", timestamp:").append(DateFormatUtils.format(timestamp, "HH:mm:ss.SSS"));
        b.append(", streamId:").append(streamId);
        b.append(", datasize:").append(dataSize);
        b.append("}");

        return b.toString();
    }
}
