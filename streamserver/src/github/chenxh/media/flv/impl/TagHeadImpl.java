package github.chenxh.media.flv.impl;

import java.io.EOFException;
import java.io.IOException;

import github.chenxh.media.UnsignedDataInput;
import github.chenxh.media.flv.ITagTrunk;
import github.chenxh.media.flv.ITagData;
import github.chenxh.media.flv.ITagDataVistor;
import github.chenxh.media.flv.ITagHead;

public class TagHeadImpl implements ITagHead {
    private static final int HEAD_SIZE = 11;
    private int tagType;
    private int dataSize;
    public long timestamp;

    // always 0
    private int streamId;

    
    protected TagHeadImpl(){}
    
    public TagHeadImpl(int tagType,
            int dataSize,
            long timestamp,
            int streamId) {
        super();
        init(tagType, dataSize, timestamp, streamId);
    }

    /**
     * Flv �ļ��У���Щ���ݱ�����  11 �ֽ���
     * @param tagType    U8
     * @param dataSize   U24
     * @param timestamp  U24 + U8������ U8 Ϊ��չλ
     * @param streamId   U8, һֱ���� 0
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
            case ITagTrunk.VIDEO:
                return decoder.readVideoData(this, dataInput);
            case ITagTrunk.AUDIO:
                return decoder.readAudioData(this, dataInput);
            case ITagTrunk.SCRIPT_DATA:
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
        return getHeadSize() + getDataSize();
    }
    
    
    @Override
    public long getHeadSize() {
        return HEAD_SIZE;
    }

    @Override
    public long getDataSize() {
        return dataSize;
    }
    
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        
        b.append("{");
        b.append("[type:").append(getType()).append("]");
        b.append(", [dataSize:").append(getDataSize()).append("]");
        b.append(", [timestamp:").append(getTimestamp()).append("]");
        b.append(", [streamId:").append(getStreamId()).append("]");
        b.append("}");
        
        return b.toString();
    }
}
