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
        return getString("metadatacreator");
    }
    
    public boolean hasKeyframes() {
        return getBoolean("hasKeyframes")
         && null != getKeyFramesObject();
    }

    private EcmaObject getKeyFramesObject() {
        return getEcmaObject("keyframes");
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
        return getBoolean("hasCuePoints")
        && null != getStrictArray("cuePoints");
    }
    
    public CuePoint[] getCuePoints(){
        if (hasCuePoints()) {
            StrictArray cuePoints = getStrictArray("cuePoints");

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
        return getBoolean("hasVideo");
    }
    
    public boolean hasAudio() {
        return getBoolean("hasAudio");
    }
    
    public boolean hasMetadata() {
        return getBoolean("hasMetadata");
    }
    
    public int getWidth() {
        return getInt("width");
    }
    
    public int getHeight() {
        return getInt("height");
    }
    
    public int getFramerate() {
        return getInt("framerate");
    }
    
    public int getAudiosamplerate() {
        return getInt("audiosamplerate");
    }
    
    public double getDuration() {
        return getDouble("duration");
    }

    public double getVideoSize() {
        return getDouble("videosize");
    }

    // -----------------------------------------------------
    // 基本方法
    // -----------------------------------------------------
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

    public EcmaArray getRawValue() {
        return rawValue;
    }
}
