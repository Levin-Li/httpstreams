package com.thunisoft.videox.mp4.sampletable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.thunisoft.videox.mp4.Box;
import com.thunisoft.videox.mp4.FullBox;

public class SampleChunkBox extends FullBox {
    public final static String CONTAINER = "moov.trak.mdia.minf.stbl";
    public final static String TYPE = "stsc";

    private List<STSCRecord> entries = Collections.emptyList();
    
    public SampleChunkBox(Box copy) {
        super(copy);
    }

    public void setSTSCEntryCount(long size) {
        entries = new ArrayList<STSCRecord>((int)size);
    }
    
    public void addEntry(long firstChunk, long samplesPerChunk, long sampleDescIndex) {
        entries.add(new STSCRecord(firstChunk, samplesPerChunk, sampleDescIndex));
    }

    public long sampleCountOfChunk(int chunkIndex) {
        if (entries.isEmpty()) {
            throw new IndexOutOfBoundsException("entries is empty");
        }
        
        int exceptFirstChunk = chunkIndex + 1;

        for (int i = entries.size()-1; i >= 0 ; i--) {
            STSCRecord record = entries.get(i);
            
            if (record.firstChunk <= exceptFirstChunk) {
                return record.samplesPerChunk;
            }
        }
        
        // 默认认为这个 chunk 没有 simple
        return 0;
    }
    
    public static class STSCRecord {
        private long firstChunk;
        private long samplesPerChunk;
        private long sampleDescIndex;
        
        /**
         * @param firstChunk
         * @param samplesPerChunk
         * @param sampleDescIndex
         */
        private STSCRecord(long firstChunk, long samplesPerChunk,
                long sampleDescIndex) {
            super();
            this.firstChunk = firstChunk;
            this.samplesPerChunk = samplesPerChunk;
            this.sampleDescIndex = sampleDescIndex;
        }
        public long firstChunk() {
            return firstChunk;
        }
        public void firstChunk(long firstChunk) {
            this.firstChunk = firstChunk;
        }
        public long samplesPerChunk() {
            return samplesPerChunk;
        }
        public void samplesPerChunk(long samplesPerChunk) {
            this.samplesPerChunk = samplesPerChunk;
        }
        public long sampleDescIndex() {
            return sampleDescIndex;
        }
        public void sampleDescIndex(long sampleDescIndex) {
            this.sampleDescIndex = sampleDescIndex;
        }
        
        @Override
        public String toString() {
            StringBuilder b = new StringBuilder();

            b.append("{");
            b.append("first:").append(firstChunk);
            b.append(", perChunk:").append(samplesPerChunk);
            b.append(", descIndex:").append(sampleDescIndex);
            b.append("}");

            return b.toString();
        }
    }
    
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder(TYPE);

        b.append("{");
        b.append("count:").append(entries.size());
        b.append(", entries:").append(entries.toString());
        b.append("}");
        
        return b.toString();
    }
}
