package com.thunisoft.videox.mp4.sampletable;

import java.util.ArrayList;
import java.util.List;

import com.thunisoft.videox.mp4.Box;
import com.thunisoft.videox.mp4.FullBox;

public class CompositionOffsetBox extends FullBox {
    public final static String CONTAINER = "moov.trak.mdia.minf.stbl";
    public final static String TYPE = "ctts";
    
    private List<CompositionOffset> offsets = null;
    
    public CompositionOffsetBox(Box copy) {
        super(copy);
    }

    public void setCompositionOffsetCount(long count) {
        offsets = new ArrayList<CompositionOffsetBox.CompositionOffset>((int)count);
    }

    public void addCompositionOffset(long sampleCount, long offset){
        offsets.add(new CompositionOffset(sampleCount, offset));
    }

    public long getCompositionOffset(long sampleIndex) {
        
        long passedSamples = 0;
        for (int i = 0; i < offsets.size(); i++) {
            CompositionOffset offset = offsets.get(i);
            
            long sampleIndexEnd = passedSamples + offset.sampleCount;
            if (sampleIndexEnd > sampleIndex) {
                return offset.sampleOffset;
            } else {
                passedSamples = sampleIndexEnd;
            }
        }

        return 0;
    }
    
    public static class CompositionOffset {
        private long sampleCount;
        private long sampleOffset;
        
        public CompositionOffset(long sampleCount, long sampleOffset) {
            super();
            this.sampleCount = sampleCount;
            this.sampleOffset = sampleOffset;
        }

        public long getSampleCount() {
            return sampleCount;
        }

        public long getSampleOffset() {
            return sampleOffset;
        }
        
        @Override
        public String toString() {
            return "{count:" + sampleCount + ", offset:" + sampleOffset + "}";
        }
    }
    
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        
        b.append(type());
        b.append("{");
        b.append("count:").append(offsets.size());
        b.append(", offsets:").append(offsets);
        return b.toString();
    }
}
