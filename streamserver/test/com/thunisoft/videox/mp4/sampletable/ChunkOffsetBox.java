package com.thunisoft.videox.mp4.sampletable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.thunisoft.videox.mp4.Box;
import com.thunisoft.videox.mp4.FullBox;

public class ChunkOffsetBox extends FullBox {
    public final static String CONTAINER = "moov.trak.mdia.minf.stbl";
    public final static String STCO = "stco";
    public final static String CO64 = "co64";

    private List<Long> offsets = Collections.emptyList();
    
    public ChunkOffsetBox(Box copy) {
        super(copy);
    }
    
    public void setOffsetCount(long count) {
        offsets = new ArrayList<Long>((int)count);
    }
    
    public void addOffset(long offset) {
        offsets.add(offset);
    }
    
    public int offsetCount() {
        return offsets.size();
    }
    
    /**
     * 
     * @param chunkIndex
     * @return
     * @since V1.0 2014-4-1
     * @author chenxh
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (<tt>index &lt; 0 || index &gt;= size()</tt>)
     */
    public long getOffset(int chunkIndex) {
        return offsets.get(chunkIndex);
    }
    
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        
        b.append(type());
        b.append("{");
        b.append("count:").append(offsets.size());
        b.append(", chunk offset:").append(offsets.toString());
        b.append("}");
        
        return b.toString();
    }
}
