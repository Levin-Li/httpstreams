package github.chenxh.media.flv.script;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class StrictArray extends ArrayList<Object> {
    
    private static final long serialVersionUID = 1L;

    public StrictArray() {
        super();
    }
    
    public StrictArray(int arraySize) {
        super(arraySize);
    }

    @SuppressWarnings("unchecked")
    public <T> T[] toArray(ITypeConverter<T> converter, Class<T> type){
        T values[] = (T[])Array.newInstance(type, size());
        
        for (int i = 0; i < values.length; i++) {
            Object value = get(i);
            values[i] = converter.convert(value);
        }

        return values;
    }
    
    public int[] toIntArray(){
        int values[] = new int[size()];
        
        for (int i = 0; i < values.length; i++) {
            Object value = get(i);
            if (value instanceof Number) {
                values[i] = ((Number)get(i)).intValue();
            }
        }
        
        return values;
    }
    
    public double[] toDoubleArray(){
        double values[] = new double[size()];
        
        for (int i = 0; i < values.length; i++) {
            Object value = get(i);
            if (value instanceof Number) {
                values[i] = ((Number)get(i)).doubleValue();
            }
        }
        
        return values;
    }
    
    public long[] toLongArray(){
        long values[] = new long[size()];
        
        for (int i = 0; i < values.length; i++) {
            Object value = get(i);
            if (value instanceof Number) {
                values[i] = ((Number)get(i)).longValue();
            }
        }

        return values;
    }
    
    /**
     * 把未知类型转换成新的类型
     * @author chenxh
     *
     * @param <T> 新的类型
     */
    public static interface ITypeConverter<T> {
        T convert(Object rawValue);
    }
}
