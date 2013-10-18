package github.chenxh.media.flv.script.metadata;

import github.chenxh.media.flv.FlvSignature;
import github.chenxh.media.flv.ITagData;
import github.chenxh.media.flv.script.EcmaArray;
import github.chenxh.media.flv.script.EcmaObject;
import github.chenxh.media.flv.script.StrictArray;
import github.chenxh.media.flv.script.StrictArray.ITypeConverter;

import java.sql.Timestamp;

import org.apache.commons.lang.ArrayUtils;



/**
 * Flv 文件描述信息
 * @author chenxh
 *
 */
public class FlvMetaData implements ITagData {
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

    private FlvSignature signature;
    private EcmaArray rawValue;

    /**
     * 可能为空
     */
    private byte[] rawBytes;
    
    private FlvMetaData(FlvSignature signature, EcmaArray object) {
        this.signature = signature;
        this.rawValue = object;
    }

    public static FlvMetaData parseEcmaArray(FlvSignature signature, EcmaArray object){
        return new FlvMetaData(signature, object);
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

    private EcmaObject getKeyFramesObject() {
        return getEcmaObject(P_KEYFRAMES);
    }

    public long[] getFilePositions(){
        if (hasKeyframes()) {
            return KeyFrames.getFilepositions(getKeyFramesObject());
        } else {
            return ArrayUtils.EMPTY_LONG_ARRAY;
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
        
        double[] times = getTimes();
        long[] positions = getFilePositions();
        for (int i = 0; i < positions.length && i < times.length; i++) {
            if (startAt <= times[i]) {
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
    
    public double[] getTimes(){
        if (hasKeyframes()) {
            return KeyFrames.getTimes(getKeyFramesObject());
        } else {
            return ArrayUtils.EMPTY_DOUBLE_ARRAY;
        }
    }

    public boolean hasCuePoints() {
        return getBoolean(P_HAS_CUEPOINTS)
        && null != getStrictArray(P_CUEPOINTS);
    }
    
    public CuePoint[] getCuePoints(){
        if (hasCuePoints()) {
            StrictArray cuePoints = getStrictArray(P_CUEPOINTS);

            return cuePoints.toArray(new ITypeConverter<CuePoint>(){
                @Override
                public CuePoint convert(Object rawValue) {
                    EcmaArray value = (EcmaArray)rawValue;

                    return new CuePoint(value);
                }
            }, CuePoint.class);
            
        } else {
            return CuePoint.EMPTY_ARRAY;
        }
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

    // -----------------------------------------------------
    // 基本方法
    // -----------------------------------------------------
    public void set(String key, Object value) {
        rawValue.set(key, value);
    }
    
    public boolean getBoolean(String key) {
        return rawValue.getBoolean(key);
    }
    
    public String getString(String key) {
        return rawValue.getString(key);
    }
    
    public Number getNumber(String key) {
        return rawValue.getNumber(key);
    }
    
    public int getInt(String key) {
        return rawValue.getInt(key);
    }

    public double getDouble(String key) {
        return rawValue.getDouble(key);
    }
    
    public long getLong(String key) {
        return rawValue.getLong(key);
    }

    public Timestamp getTimestamp(String key) {
        return rawValue.getTimestamp(key);
    }
    public StrictArray getStrictArray(String key) {
        return rawValue.getStrictArray(key);
    }
    
    public EcmaArray getEcmaArray(String key) {
        return rawValue.getEcmaArray(key);
    }
    
    public EcmaObject getEcmaObject(String key) {
        return rawValue.getEcmaObject(key);
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

        // 媒体信息提示点
        CuePoint[] cuePonits = getCuePoints();
        b.append(" hasCuePoints=").append(hasCuePoints()).append("\r\n");
        for (int i = 0; i < cuePonits.length; i++) {
            b.append("   [").append(cuePonits[i]).append("]\r\n");
        }
        
        return b.toString();
    }

    public FlvSignature getSignature() {
        return signature;
    }

    public byte[] getRawBytes() {
        return rawBytes;
    }

    public void setRawBytes(byte[] rawBytes) {
        this.rawBytes = rawBytes;
    }

    public EcmaArray getRawObject() {
        return rawValue;
    }

    public void setSignature(FlvSignature signature) {
        this.signature = signature;
    }
}
