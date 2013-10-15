package github.chenxh.media.flv;



/**
 * flv 文件的头
 * @author chenxh
 *
 */
public class FlvSignature implements ITag {
    private static final long HEAD_SIZE = 9;
    
    private String signature = "FLV";
    private int version = 1;

    /**
     * U[5], Must be 0;
     * U[1], Audio tags are present
     * U[1], Must be 0
     * U[1], Video tags are present
     */
    private int typeFlag = 5;

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
        this.typeFlag = typeFlag;
        this.dataOffSet = headerSize;
    }
    
    public boolean hasAudioTags() {
        return 1 == ((typeFlag >> 2) & 0x01);
    }

    public boolean hasVideoTags(){
        return 1 == ((typeFlag >> 0) & 0x01);
    }
    
    public int getVersion() {
        return version;
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
        b.append("size=").append(getBodySize()).append("+").append(getBodySize());
        
        return b.toString();
    }
    
    // --------------------------------------------------------------
    // implement interface of Tag
    // --------------------------------------------------------------
    @Override
    public long getTagHeadSize() {
        return HEAD_SIZE;
    }
    
    @Override
    public long size() {
        return dataOffSet;
    }

    @Override
    public long getBodySize() {
        return size() - getTagHeadSize();
    }
    
    @Override
    public int getType() {
        return HEAD;
    }

    @Override
    public ITagData getData() {
        return null;
    }
    
    @Override
    public long getPreTagSize() {
        return 0;
    }
}
