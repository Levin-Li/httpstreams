package github.chenxh.media.flv.impl;

import github.chenxh.media.flv.ITagHead;

public class TagHeadImpl implements ITagHead {

    private static final int HEAD_SIZE = 11;

    private int tagType;

    private int dataSize;

    public long timestamp;

    private int streamId;

    public TagHeadImpl() {
        super();
    }

    /**
     * Flv �ļ��У���Щ���ݱ����� 11 �ֽ���
     * 
     * @param tagType U8
     * @param dataSize U24
     * @param timestamp U24 + U8������ U8 Ϊ��չλ
     * @param streamId U8, һֱ���� 0
     */
    public void init(int tagType, int dataSize, long timestamp, int streamId) {
        this.tagType = tagType;
        this.dataSize = dataSize;
        this.timestamp = timestamp;
        this.streamId = streamId;
    }

    public int getStreamId() {
        return streamId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public int getTagType() {
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
        b.append("[type:").append(getTagType()).append("]");
        b.append(", [dataSize:").append(getDataSize()).append("]");
        b.append(", [timestamp:").append(getTimestamp()).append("]");
        b.append(", [streamId:").append(getStreamId()).append("]");
        b.append("}");

        return b.toString();
    }

}