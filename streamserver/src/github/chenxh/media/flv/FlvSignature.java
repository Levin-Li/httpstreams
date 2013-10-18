package github.chenxh.media.flv;

import github.chenxh.media.IDataTrunk;



/**
 * flv 文件的头
 * @author chenxh
 *
 */
public class FlvSignature implements IDataTrunk {
    public static final int MIN_HEAD_SIZE = 9;
    
    private String signature = "FLV";
    private int version = 1;

    /**
     * U[5], Must be 0;
     * U[1], Audio tags are present
     * U[1], Must be 0
     * U[1], Video tags are present
     */
    private int typeFlags = 5;


    /***
     * offsett n bytes from start of file to start of body
     * (that is, size of header)
     */
    final private long dataOffSet;

    /**
     * flv 文件头部信息
     * @param signature  3byte
     * @param version
     * @param typeFlag
     */
    public FlvSignature(String signature, int version, int typeFlag, int headerSize) {
        this.signature = signature;
        this.version = version;
        this.typeFlags = typeFlag;
        this.dataOffSet = headerSize;
    }
    
    public boolean hasAudioTags() {
        return 1 == ((typeFlags >> 2) & 0x01);
    }

    public boolean hasVideoTags(){
        return 1 == ((typeFlags >> 0) & 0x01);
    }
    
    public int getVersion() {
        return version;
    }

    public int getTypeFlags() {
        return typeFlags;
    }

    // -------------------------------------------------------------
    // base properties
    // -------------------------------------------------------------
    public String toString() {
        StringBuilder b = new StringBuilder();
        
        // 类型和版本
        b.append(signature);
        b.append(" ").append(version);
        
        // 是否有视频
        b.append(" ");
        b.append(hasVideoTags()?"hasVideo":"noVideo");
        
        // 是否有音频
        b.append(" ");
        b.append(hasAudioTags()?"hasAudio":"noAudio");

        // 标签大小
        b.append(" ");
        b.append("size=").append(getDataSize()).append("+").append(getDataSize());
        
        return b.toString();
    }
    
    // --------------------------------------------------------------
    // implement interface of Tag
    // --------------------------------------------------------------
    @Override
    public long getHeadSize() {
        return MIN_HEAD_SIZE;
    }
    
    @Override
    public long size() {
        return dataOffSet;
    }

    @Override
    public long getDataSize() {
        return size() - getHeadSize();
    }

    @Override
    public int getTagType() {
        return ITagTrunk.HEAD;
    }
}
