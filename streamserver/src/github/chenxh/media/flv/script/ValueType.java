package github.chenxh.media.flv.script;

/**
 * 脚本中的数据类型
 * 
 * @author chenxh
 *
 */
public final class ValueType {

    public static final int DT_NUMBER = 0;
    public static final int DT_BOOLEAN = 1;
    public static final int DT_STRING = 2;
    public static final int DT_OBJECT = 3;
    public static final int DT_MOVIE_CLIP = 4;
    public static final int DT_NULL = 5;
    public static final int DT_UNDEFINED = 6;
    public static final int DT_REFERENCE = 7;
    public static final int DT_ECMA_ARRAY = 8;
    public static final int DT_ARRAY = 10;
    public static final int DT_DATETIME = 11;
    public static final int DT_LONGSTRIING = 12;
    
    public static final Object NULL = new FinalValue("NULL");
    public static final Object END = new FinalValue("END");
    private ValueType(){}
    
    public static boolean isNull(Object value) {
        return NULL == value;
    }
    
    public static boolean isEnd(Object value) {
        return END == value;
    }
    
    private 
    final static class FinalValue {
        private String name;
        private FinalValue(String name){
            this.name = name;
        };
        
        @Override
        public String toString() {
            return name;
        }
    }
}
