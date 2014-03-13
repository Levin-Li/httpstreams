package com.thunisoft.mediax.core.pseudostreaming.flv;

import java.sql.Timestamp;

import org.apache.commons.lang.ArrayUtils;

import com.thunisoft.mediax.core.amf.AMFArray;
import com.thunisoft.mediax.core.amf.AMFObject;

public class FlvMetaData {

    public static final String P_VIDEOSIZE = "videosize";
    public static final String P_DURATION = "duration";
    public static final String P_AUDIOSAMPLERATE = "audiosamplerate";
    public static final String P_FRAMERATE = "framerate";
    public static final String P_HEIGHT = "height";
    public static final String P_WIDTH = "width";
    public static final String P_HAS_METADATA = "hasMetadata";
    public static final String P_HAS_AUDIO = "hasAudio";
    public static final String P_HAS_VIDEO = "hasVideo";
    public static final String P_CUEPOINTS = "cuePoints";
    public static final String P_HAS_CUEPOINTS = "hasCuePoints";
    public static final String P_KEYFRAMES = "keyframes";
    public static final String P_HAS_KEYFRAMES = "hasKeyframes";
    public static final String P_METADATA_CREATOR = "metadatacreator";

    private AMFArray metadata;

    
    public FlvMetaData(AMFArray metadata) {
        this.metadata = metadata;
    }

    // ----------------------------------------------------
    // metaData 常用的属性
    // ----------------------------------------------------
    
    public String getMetaDataCreator() {
        return getString(P_METADATA_CREATOR);
    }
    
    public boolean hasKeyframes() {
        return getBoolean(P_HAS_KEYFRAMES)
         && null != getKeyFramesObject();
    }

    private Object getKeyFramesObject() {
        AMFObject keyFrames = metadata.getAMFObject(P_KEYFRAMES);
        return keyFrames;
    }

    public long[] getFilePositions(){
        if (hasKeyframes()) {
            Object[] positions = metadata.getAMFObject(P_KEYFRAMES).getArray("filepositions");
            
            long[] p = new long[positions.length];
            for (int i = 0; i < p.length; i++) {
                p[i] = ((Number)positions[i]).longValue();
            }
            return p;
        } else {
            return ArrayUtils.EMPTY_LONG_ARRAY;
        }
    }

    public double[] getTimes(){
        if (hasKeyframes()) {
            Object[] positions = metadata.getAMFObject(P_KEYFRAMES).getArray("times");
            
            double[] p = new double[positions.length];
            for (int i = 0; i < p.length; i++) {
                p[i] = ((Number)positions[i]).doubleValue();
            }
            return p;
        } else {
            return ArrayUtils.EMPTY_DOUBLE_ARRAY;
        }
    }
    /**
     * 根据时间戳，获得起始帧的未知
     * 
     * @param times
     * @return -1 if has't key frames or startAt is max then the last key frame
     */
    public long getFilePosition(double startAt) {
        long targetPosition = -1;
        if (!hasKeyframes()) {
            return targetPosition;
        }
        
        long[] positions = getFilePositions();
        for (int i = 0; i < positions.length && i < positions.length; i++) {
            if (startAt > positions[i]) {
                targetPosition = positions[i];
            } else {
                break;
            }
        }

        return targetPosition;
    }
    
    public long getNearestPosition(long position) {
        long nearestPosition = -1;
        if (!hasKeyframes()) {
            return nearestPosition;
        }
        
        long[] positions = getFilePositions();
        for (int i = 0; i < positions.length; i++) {
            if (positions[i] > position) {
                break;
            }
            nearestPosition = positions[i];
        }

        return nearestPosition;
    }
    

    
    public boolean hasVideo() {
        return getBoolean(P_HAS_VIDEO);
    }
    
    public boolean hasAudio() {
        return getBoolean(P_HAS_AUDIO);
    }
    
    public boolean hasMetadata() {
        return getBoolean(P_HAS_METADATA);
    }
    
    public int getWidth() {
        return getInt(P_WIDTH);
    }
    
    public int getHeight() {
        return getInt(P_HEIGHT);
    }
    
    public int getFramerate() {
        return getInt(P_FRAMERATE);
    }
    
    public int getAudiosamplerate() {
        return getInt(P_AUDIOSAMPLERATE);
    }
    
    public double getDuration() {
        return getDouble(P_DURATION);
    }

    public double getVideoSize() {
        return getDouble(P_VIDEOSIZE);
    }

    public boolean getBoolean(String key) {
        return metadata.getBoolean(key);
    }
    
    public String getString(String key) {
        return metadata.getString(key);
    }
    
    public int getInt(String key) {
        return metadata.getInt(key);
    }

    public double getDouble(String key) {
        return metadata.getDouble(key);
    }
    
    public long getLong(String key) {
        return metadata.getLong(key);
    }

    public Timestamp getTimestamp(String key) {
        return metadata.getTimestamp(key);
    }
    
    public AMFArray getEcmaArray(String key) {
        return metadata.getAMFArray(key);
    }
    
    public AMFObject getEcmaObject(String key) {
        return metadata.getAMFObject(key);
    }

    // -----------------------------------------------
    // object 基本方法
    // -----------------------------------------------
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        
        b.append("onMetaData").append("\r\n");
        b.append(" metadatacreator=").append(getMetaDataCreator()).append("\r\n");
        b.append(" hasVideo=").append(hasVideo()).append("\r\n");
        b.append(" hasAudio=").append(hasAudio()).append("\r\n");
        b.append(" hasMetadata=").append(hasMetadata()).append("\r\n");
        b.append(" width=").append(getWidth()).append("\r\n");
        b.append(" height=").append(getHeight()).append("\r\n");
        b.append(" framerate=").append(getFramerate()).append("\r\n");
        b.append(" audiosamplerate=").append(getAudiosamplerate()).append("\r\n");
        b.append(" duration=").append(getDuration()).append("\r\n");
        
        // 关键帧索引
        long[] filepositions = getFilePositions();
        double[] times = getTimes();
        b.append(" hasKeyframes=").append(hasKeyframes()).append("\r\n");
        b.append(" keyframes=").append("\r\n");
        for (int i = 0; i < filepositions.length && i < times.length; i++) {
            b.append("   [").append(filepositions[i]).append(",").append(times[i]).append("]\r\n");
        }

        return b.toString();
    }

    public AMFArray getMetadata() {
        return metadata;
    }


}
