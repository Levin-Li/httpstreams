package github.chenxh.media.flv.impl;

import java.io.EOFException;
import java.io.IOException;

import github.chenxh.media.UnsignedDataInput;
import github.chenxh.media.flv.ITag;
import github.chenxh.media.flv.ITagData;
import github.chenxh.media.flv.ITagDataVistor;
import github.chenxh.media.flv.ITagHead;

public class FlvTagHead implements ITagHead {
    private static final int HEAD_SIZE = 11;
    private int tagType;
    private int dataSize;
    private long timestamp;

    // always 0
    private int streamId;

    
    protected FlvTagHead(){}
    
    public FlvTagHead(int tagType,
            int dataSize,
            long timestamp,
            int streamId) {
        super();
        init(tagType, dataSize, timestamp, streamId);
    }

    /**
     * Flv 文件中，这些内容保存在  11 字节中
     * @param tagType    U8
     * @param dataSize   U24
     * @param timestamp  U24 + U8，其中 U8 为扩展位
     * @param streamId   U8, 一直都是 0
     */
    protected void init(int tagType, int dataSize, long timestamp, int streamId) {
        this.tagType = tagType;
        this.dataSize = dataSize;
        this.timestamp = timestamp;
        this.streamId = streamId;
    }

    /**
     * 
     * @param decoder
     * @param dataInput
     * @throws IOException 
     * @throws EOFException 
     */
    public ITagData readData(ITagDataVistor decoder, UnsignedDataInput dataInput) throws EOFException, IOException {
        switch (getType()) {
            case ITag.VIDEO:
                return decoder.readVideoData(this, dataInput);
            case ITag.AUDIO:
                return decoder.readAudioData(this, dataInput);
            case ITag.SCRIPT_DATA:
                return decoder.readScriptData(this, dataInput);
            default:
                return decoder.readOtherData(this, dataInput);
        }
    }
    
    // -----------------------------------------------------
    // getter/setter Method
    // -----------------------------------------------------
    public int getStreamId() {
        return streamId;
    }

    public long getTimestamp() {
        return timestamp;
    }
    
    // ------------------------------------------------------
    // implements of interface Tag
    // ------------------------------------------------------
    
    @Override
    public int getType() {
        return tagType;
    }
    
    @Override
    public long size() {
        return getTagHeadSize() + getBodySize();
    }
    
    
    @Override
    public long getTagHeadSize() {
        return HEAD_SIZE;
    }

    @Override
    public long getBodySize() {
        return dataSize;
    }
    
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        
        b.append("{");
        b.append("[type:").append(getType()).append("]");
        b.append(", [dataSize:").append(getBodySize()).append("]");
        b.append(", [timestamp:").append(getTimestamp()).append("]");
        b.append(", [streamId:").append(getStreamId()).append("]");
        b.append("}");
        
        return b.toString();
    }
}
