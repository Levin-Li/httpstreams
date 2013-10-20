package github.chenxh.media.flv.script.metadata;

import github.chenxh.media.flv.script.EcmaObject;
import github.chenxh.media.flv.script.StrictArray;

import org.apache.commons.lang.ArrayUtils;

public class KeyFrames {

    /**
     * ׷��һ���ؼ�֡
     * 
     * @param keyFrams
     * @param position
     * @param time
     */
    public static void appendKeyFrame(EcmaObject keyFrams, long position, double time) {
        ensureNotNull(keyFrams);

        StrictArray times = keyFrams.getStrictArray("times");
        StrictArray positions = keyFrams.getStrictArray("filepositions");

        if (null == times) {
            times = new StrictArray();
            positions = new StrictArray();
            keyFrams.set("times", times);
            keyFrams.set("filepositions", positions);
        }
        
        // ׷�ӵ����
        positions.add(position);
        times.add(time);
    }

    public static double[] getTimes(EcmaObject keyFrams) {
        ensureNotNull(keyFrams);

        StrictArray times = keyFrams.getStrictArray("times");
        return null != times ? times.toDoubleArray()
                : ArrayUtils.EMPTY_DOUBLE_ARRAY;
    }

    public static long[] getFilepositions(EcmaObject keyFrams) {
        ensureNotNull(keyFrams);

        StrictArray positions = keyFrams.getStrictArray("filepositions");

        return null != positions ? positions.toLongArray()
                : ArrayUtils.EMPTY_LONG_ARRAY;
    }
    
    private static void ensureNotNull(Object object) {
        if (null == object) {
            throw new NullPointerException("EcmaArray Should not be Null");
        }
    }
    
    
    private EcmaObject object = new EcmaObject();

    public double[] getTimes() {
        return getTimes(object);
    }
    
    public long[] getFilepositions() {
        return getFilepositions(object);
    }

    /**
     * �ƶ��ļ�λ��
     * 
     * @param relativeBytes ��Ҫ�ƶ������λ��
     */
    public void moveFilePosition(int relativeBytes){
        StrictArray filePositions = object.getStrictArray("filepositions");
        for (int i = 0; i < filePositions.size(); i++) {
            long newPosition = relativeBytes + ((Number)filePositions.get(i)).longValue();
            filePositions.set(i, newPosition);
        }
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
    
    public void updateKeyFrames(FlvMetaData metaData) {
        metaData.set(FlvMetaData.P_HAS_KEYFRAMES, true);
        metaData.set(FlvMetaData.P_KEYFRAMES, this.object);
    }
    
    
}
