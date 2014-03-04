package com.thunisoft.mediax.core.mp4;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * description
 * <p>
 * unsigned int(32) size; 
 * unsigned int(32) type = boxtype; 
 * if (size==1) {
 * unsigned int(64) largesize; } else if (size==0) { // box extends to end of
 * file } if (boxtype==‘uuid’) { unsigned int(8)[16] usertype = extended_type; }
 * 
 * @author chenxiuheng@gmail.com
 * @since V1.0
 * @Date 2014-3-4
 */
public class Box {
    /**
     * UI32
     */
    private long boxSize;
    private long boxHeadSize;
    private long filePosition;

    /** UI32 */
    private String type;

    /** int(8)[16] if type == 'uuid' **/
    private String extendType;

    public Box(long boxSize, long boxHeadSize, String type, String extendType) {
        this.boxSize = boxSize;
        this.boxHeadSize = boxHeadSize;
        this.type = type;

        if (!BoxType.isUUID(type) && null != extendType) {
            throw new IllegalArgumentException("extend Type if for uuid Box");
        }

        if (BoxType.isUUID(type) && lengthOf(extendType) != 8) {
            throw new IllegalArgumentException("extend Type if for uuid Box");
        }
    }

    private static int lengthOf(String s) {
        return null == s ? 0 : s.length();
    }

    public Box(long boxSize, long boxHeadSize, BoxType type, String extendType) {
        this(boxSize, boxHeadSize, type.value(), extendType);
    }

    public void parseData(ByteBuffer data, Mp4Reader reader) throws IOException {

    }


    public long getBoxSize() {
        return boxSize;
    }

    public String getType() {
        return type;
    }

    public String getExtendType() {
        return extendType;
    }
    

    @Override
    public String toString() {
        return "[" + boxSize + "]" + (BoxType.isUUID(type) ? extendType : type);
    }

    public long getBoxHeadSize() {
        return boxHeadSize;
    }

    public void setBoxHeadSize(long boxHeadSize) {
        this.boxHeadSize = boxHeadSize;
    }

    public long getFilePosition() {
        return filePosition;
    }

    public void setFilePosition(long filePosition) {
        this.filePosition = filePosition;
    }

    public void setBoxSize(long boxSize) {
        this.boxSize = boxSize;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setExtendType(String extendType) {
        this.extendType = extendType;
    }
}
