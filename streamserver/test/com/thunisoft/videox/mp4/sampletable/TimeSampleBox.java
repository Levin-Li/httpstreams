package com.thunisoft.videox.mp4.sampletable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.thunisoft.videox.mp4.Box;
import com.thunisoft.videox.mp4.FullBox;

public class TimeSampleBox extends FullBox {
    public final static String CONTAINER = "moov.trak.mdia.minf.stbl";
    public final static String TYPE = "stts";

    private List<STTSRecord> entries = Collections.emptyList();

    public TimeSampleBox(Box copy) {
        super(copy);
    }
    
    public void setSTTSCount(long count) {
        entries = new ArrayList<STTSRecord>((int)count);
    }
    
    public void addSTTSRecord(long sampleCount, long sampleDelta) {
        entries.add(new STTSRecord(sampleCount, sampleDelta));
    }


    
    public long getSampleDelta() {
        if (entries.isEmpty()) {
            return -1;
        } else {
            return entries.get(0).sampleDelta;
        }
    }
    
    public static class STTSRecord {
        private long sampleCount;
        private long sampleDelta;

        /**
         * @param sampleCount
         * @param sampleDelta
         */
        private STTSRecord(long sampleCount, long sampleDelta) {
            super();
            this.sampleCount = sampleCount;
            this.sampleDelta = sampleDelta;
        }
        public long sampleCount() {
            return sampleCount;
        }
        public void sampleCount(long sampleCount) {
            this.sampleCount = sampleCount;
        }
        public long sampleDelta() {
            return sampleDelta;
        }
        public void sampleDelta(long sampleDelta) {
            this.sampleDelta = sampleDelta;
        }

        @Override
        public String toString() {
            StringBuilder b = new StringBuilder();

            b.append("{count:").append(sampleCount);
            b.append(", delta:").append(sampleDelta);
            b.append("}");

            return b.toString();
        }
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();

        b.append(type());
        b.append("{");
        b.append(entries.toString());
        b.append("}");

        return b.toString();
    }
}
