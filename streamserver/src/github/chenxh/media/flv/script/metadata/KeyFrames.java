package github.chenxh.media.flv.script.metadata;

import github.chenxh.media.flv.script.EcmaArray;
import github.chenxh.media.flv.script.StrictArray;

import org.apache.commons.lang.ArrayUtils;

public class KeyFrames {

    /**
     * 追加一个关键帧
     * 
     * @param keyFrams
     * @param position
     * @param time
     */
    public static void appendKeyFrame(EcmaArray keyFrams, long position, double time) {
        ensureNotNull(keyFrams);

        StrictArray times = keyFrams.getStrictArray("times");
        StrictArray positions = keyFrams.getStrictArray("filepositions");
        
        if (null == times) {
            times = new StrictArray();
            positions = new StrictArray();
            keyFrams.put("times", times);
            keyFrams.put("filepositions", positions);
        }
        
        // 追加到最后
        positions.add(position);
        times.add(time);
    }

    public static double[] getTimes(EcmaArray keyFrams) {
        ensureNotNull(keyFrams);

        StrictArray times = keyFrams.getStrictArray("times");
        return null != times ? times.toDoubleArray()
                : ArrayUtils.EMPTY_DOUBLE_ARRAY;
    }

    public static long[] getFilepositions(EcmaArray keyFrams) {
        ensureNotNull(keyFrams);

        StrictArray positions = keyFrams.getStrictArray("filepositions");

        return null != positions ? positions.toLongArray()
                : ArrayUtils.EMPTY_LONG_ARRAY;
    }
    
    private static void ensureNotNull(EcmaArray object) {
        if (null == object) {
            throw new NullPointerException("EcmaArray Should not be Null");
        }
    }
    
    
    private EcmaArray object = new EcmaArray();

    public double[] getTimes() {
        return getTimes(object);
    }
    
    public long[] getFilepositions() {
        return getFilepositions(object);
    }

    public void appendKeyFrame(long position, double time) {
        appendKeyFrame(object, position, time);
    }
  

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();

        long[] filepositions = getFilepositions();
        double[] times = getTimes();
        b.append(" keyframes=").append("\r\n");
        for (int i = 0; i < filepositions.length && i < times.length; i++) {
            b.append("   [").append(filepositions[i]).append(",").append(times[i]).append("]\r\n");
        }
        
        return b.toString();
    }
    
}
