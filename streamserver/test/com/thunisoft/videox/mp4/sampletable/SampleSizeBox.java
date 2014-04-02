package com.thunisoft.videox.mp4.sampletable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.thunisoft.videox.mp4.Box;
import com.thunisoft.videox.mp4.FullBox;

public class SampleSizeBox extends FullBox {
    public final static String CONTAINER = "moov.trak.mdia.minf.stbl";
    public final static String TYPE = "stsz";

    /**
     *  If all samples have the same size, this
     *   field is set with that constant size;
     *   otherwise it is 0
     */
    private long constantSize;
    
    private List<Long> sampleSizes = Collections.emptyList();
    
    public SampleSizeBox(Box copy) {
        super(copy);
    }
    
    public void constantSize(long constantSize) {
        this.constantSize = constantSize;
    }
    
    public long constantSize() {
        return constantSize;
    }
    
    public void setSampleCount(long count) {
        sampleSizes = new LinkedList<Long>();
    }

    public void addSampleSize(long sampleSize) {
        sampleSizes.add(sampleSize);
    }
    
    public long getSampleSize(int sampleIndex) {
        return constantSize == 0 ? sampleSizes.get(sampleIndex):constantSize;
    }

    @Override
    public String toString() {
        StringBuffer b = new StringBuffer();
        
        b.append(TYPE);
        b.append("{");
        b.append("count:").append(sampleSizes.size());
        b.append(", constantSize:").append(constantSize);
        b.append(", size:").append(sampleSizes.toString());
        b.append("}");
        
        return b.toString();
    }
}
