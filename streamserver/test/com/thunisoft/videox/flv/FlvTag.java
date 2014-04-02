package com.thunisoft.videox.flv;


public class FlvTag {
    public static final int TAG_TYPE_AUDIO = 8;
    public static final int TAG_TYPE_VIDEO = 9;
    public static final int TAG_TYPE_SCRIPTDATAOBJECT = 18;
    
    public static final int TAG_FLAG_ENCRYPTED = 0x20;
    public static final int TAG_TYPE_ENCRYPTED_AUDIO = TAG_TYPE_AUDIO + TAG_FLAG_ENCRYPTED;
    public static final int TAG_TYPE_ENCRYPTED_VIDEO = TAG_TYPE_VIDEO + TAG_FLAG_ENCRYPTED;
    public static final int TAG_TYPE_ENCRYPTED_SCRIPTDATAOBJECT = TAG_TYPE_SCRIPTDATAOBJECT + TAG_FLAG_ENCRYPTED;

    // but these are good here...
    public static final int TAG_HEADER_BYTE_COUNT = 11;
    public static final int PREV_TAG_BYTE_COUNT = 4;

    /**
     * @see FlvTag#TAG_TYPE_AUDIO 音频
     * @see FlvTag#TAG_TYPE_VIDEO 视频
     * @see FlvTag#TAG_TYPE_SCRIPTDATAOBJECT 脚本
     * @see FlvTag#TAG_FLAG_ENCRYPTED 加密
     * @see FlvTag#TAG_TYPE_ENCRYPTED_AUDIO 加密音频
     * @see FlvTag#TAG_TYPE_ENCRYPTED_VIDEO 加密视频
     * @see FlvTag#TAG_TYPE_ENCRYPTED_SCRIPTDATAOBJECT 加密脚本
     */
    protected int tagType;

    protected long dataSize;

    protected long timestamp;
    
    /** always 0 */
    protected long streamId;

    protected FlvTag(int type) {
        this.tagType = type;
    }
    
    public int tagType() {
        return tagType;
    }
    public void tagType(int tagType) {
        this.tagType = tagType;
    }
    public long dataSize() {
        return dataSize;
    }
    public void dataSize(long dataSize) {
        this.dataSize = dataSize;
    }
    public long timestamp() {
        return timestamp;
    }
    public void timestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public long streamId() {
        return streamId;
    }
    public void streamId(long streamId) {
        this.streamId = streamId;
    }
    
    
}
