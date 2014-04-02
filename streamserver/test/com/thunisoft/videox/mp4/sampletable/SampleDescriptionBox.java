package com.thunisoft.videox.mp4.sampletable;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thunisoft.videox.mp4.AudioSampleEntry;
import com.thunisoft.videox.mp4.Box;
import com.thunisoft.videox.mp4.FullBox;
import com.thunisoft.videox.mp4.VisualSampleEntry;

public class SampleDescriptionBox extends FullBox implements VisualSampleEntry {
    public final static String CONTAINER = "moov.trak.mdia.minf.stbl";
    public final static String TYPE = "stsd";

    private List<SampleDescription> descripts = Collections.emptyList();

    public SampleDescriptionBox(Box copy) {
        super(copy);
    }
    
    public void setDescriptCont(long count) {
        descripts = new ArrayList<SampleDescription>((int)count);
    }
    
    public SampleDescription createSampleDescript(long length, String _4ccType){
        
        SampleDescription descript = new SampleDescription((int)length, _4ccType);
        descripts.add(descript);
        
        return descript;
    }
    
    public ByteBuffer getVideoConfig() {
        ByteBuffer config = null;
        for (SampleDescription descript : descripts) {
            String type = descript.type;

            // video
            if (VisualSampleEntry.TYPES.contains(type)) {
                // 还不清楚其他 视频格式是怎样的。
                config = descript.compressorConfigs.get("avcC");
            }

            if (null != config) {
                return config;
            }
        }
        
        return null;
    }
    
    public ByteBuffer getAudioConfig() {
        ByteBuffer config = null;
        for (SampleDescription descript : descripts) {
            String type = descript.type;

            // video
            if (AudioSampleEntry.TYPES.contains(type)) {
                // 还不清楚其他 视频格式是怎样的。
                config = descript.compressorConfigs.get("esds");
            }

            if (null != config) {
                return config;
            }
        }
        
        return null;
    }
    
    public int getAudioChannelCount() {
        for (SampleDescription descript : descripts) {
            String type = descript.type;

            // video
            if (AudioSampleEntry.TYPES.contains(type)
                    && descript.channelCount > 0) {
                // 还不清楚其他 视频格式是怎样的。
                return descript.channelCount;
            }
        }
        
        return 2;
    }
    
    public int getAudioSampleSize() {
        for (SampleDescription descript : descripts) {
            String type = descript.type;

            // video
            if (AudioSampleEntry.TYPES.contains(type)
                    && descript.sampleSize > 0) {
                // 还不清楚其他 视频格式是怎样的。
                return descript.sampleSize;
            }
        }
        
        return 0;
    }

    public double getAudioSampleRate() {
        for (SampleDescription descript : descripts) {
            String type = descript.type;

            // video
            if (AudioSampleEntry.TYPES.contains(type)
                    && descript.sampleRate > 0) {
                // 还不清楚其他 视频格式是怎样的。
                return descript.sampleRate;
            }
        }
        
        return 0;
    }
    
    public static final class SampleDescription {
        private int length;
        private String type;
        
        private int videoHeight;
        private int videoWidth;
        
        private int channelCount;
        private int sampleSize;
        private double sampleRate;
        
        private Map<String, ByteBuffer> compressorConfigs = new HashMap<String, ByteBuffer>();
        
        public SampleDescription(int length, String type) {
            super();
            this.length = length;
            this.type = type;
        }
        
        @Override
        public String toString() {
            return "{length:" + length + ", type:" + type + ",configs:" + compressorConfigs.keySet() + "}";
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
        
        public void addConfig(String name, ByteBuffer content) {
            compressorConfigs.put(name, content);
        }

        public int getVideoHeight() {
            return videoHeight;
        }

        public void setVideoHeight(int videoHeight) {
            this.videoHeight = videoHeight;
        }

        public int getVideoWidth() {
            return videoWidth;
        }

        public void setVideoWidth(int videoWidth) {
            this.videoWidth = videoWidth;
        }

        public int getChannelCount() {
            return channelCount;
        }

        public void setChannelCount(int channelCount) {
            this.channelCount = channelCount;
        }

        public int getSampleSize() {
            return sampleSize;
        }

        public void setSampleSize(int sampleSize) {
            this.sampleSize = sampleSize;
        }

        public double getSampleRate() {
            return sampleRate;
        }

        public void setSampleRate(double sampleRate) {
            this.sampleRate = sampleRate;
        }
    }
    
    @Override
    public String toString() {
        StringBuffer b = new StringBuffer();
        
        b.append(TYPE);
        b.append("{");
        b.append("count:").append(descripts.size());
        b.append(", descrips:").append(descripts.toString());
        b.append("}");
        return b.toString();
    }
}
