package com.thunisoft.videox.mp4.sampletable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.thunisoft.videox.mp4.Box;
import com.thunisoft.videox.mp4.FullBox;

public class SyncSampleBox extends FullBox {
    public final static String CONTAINER = "moov.trak.mdia.minf.stbl";
    public final static String TYPE = "stss";
    
    private long syncCount;
    private List<Long> syncTable = Collections.emptyList();
    
    public SyncSampleBox(Box copy) {
        super(copy);
    }

    public void setSyncCount(long syncCount) {
        this.syncCount = syncCount;
        syncTable = new LinkedList<Long>();
    }
    
    public void addSyncSample(long sampleIndex) {
        syncTable.add(sampleIndex);
    }

    public boolean isSyncFrame(long sampleIndex){
        return syncTable.contains(sampleIndex);
    }

    @Override
    public String toString() {
        StringBuffer b = new StringBuffer();
        
        b.append(TYPE);
        b.append("{");
        b.append("count:").append(syncCount);
        b.append(", table:").append(syncTable);
        b.append("}");
        
        return b.toString();
    }
    
    
}
