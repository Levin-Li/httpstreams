package github.chenxh.media.flv.script;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public abstract class AbstractDynamicObject {
    protected final int defaultLength;
    protected final Map<String, Object> entry;
    protected final LinkedList<String> keyList;
    
    public static class Entry {
        private String key;
        private Object value;
        
        // °üÄÚ¿ÉÐ´
        void init(String key, Object value) {
            this.key = key;
            this.value = value;
        }
    
        public String getKey() {
            return key;
        }
        public Object getValue() {
            return value;
        }
        
    }
    
    protected AbstractDynamicObject() {
        this(8);
    }

    protected AbstractDynamicObject(int size) {
        defaultLength = size;

        entry = new HashMap<String, Object>(defaultLength);
        keyList = new LinkedList<String>();
    }
    
    public void put(String key, Object value) {
        if (entry.containsKey(key)) {
            entry.put(key, value);
        } else {
            keyList.add(key);
            entry.put(key, value);
        }
    }

    public Object get(String key) {
        return entry.get(key);
    }

    public boolean getBoolean(String key) {
        Object value = get(key);
        
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else {
            return false;
        }
    }

    public String getString(String key) {
        Object value = get(key);
        
        if (value instanceof String) {
            return (String) value;
        } else {
            return null;
        }
    }

    public Number getNumber(String key) {
        Object value = get(key);
    
        if (value instanceof Number) {
            return (Number) value;
        } else {
            return null;
        }
    }

    public double getDouble(String key) {
        Number value = getNumber(key);
        
        return null != value?value.doubleValue():0;
    }

    public long getLong(String key) {
        Number value = getNumber(key);
        
        return null != value?value.longValue():0;
    }

    public int getInt(String key) {
        Number value = getNumber(key);
        
        return null != value?value.intValue():0;
    }

    public Timestamp getTimestamp(String key) {
        Object value = get(key);
    
        if (value instanceof Timestamp) {
            return (Timestamp) value;
        } else {
            return null;
        }
    }

    public StrictArray getStrictArray(String key) {
        Object value = get(key);
    
        if (value instanceof StrictArray) {
            return (StrictArray) value;
        } else {
            return null;
        }
    }

    public EcmaObject getEcmaObject(String key) {
        Object value = get(key);
    
        if (value instanceof EcmaObject) {
            return (EcmaObject) value;
        } else {
            return null;
        }
    }
    
    public EcmaArray getEcmaArray(String key) {
        Object value = get(key);
    
        if (value instanceof EcmaArray) {
            return (EcmaArray) value;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        
        boolean isFirst = true;
        b.append("{");
        for (String key : keyList) {
            if (isFirst) {
                isFirst = false;
            } else {
                b.append(",");
            }
    
            b.append(key);
            b.append(":");
            b.append(entry.get(key));
        }
        b.append("}");
    
        return b.toString();
    }



    public Iterator<Entry> iterator() {
        final Iterator<String> keyItem = keyList.iterator();
        
        final Entry entry = new Entry();
        return new Iterator<Entry>() {
            
            @Override
            public boolean hasNext() {
                return keyItem.hasNext();
            }
    
            @Override
            public Entry next() {
                String key = keyItem.next();
                Object value = AbstractDynamicObject.this.entry.get(key);
    
                entry.init(key, value);
                return entry;
            }
    
            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove");
                
            }
        };
    }

    protected int size() {
        return keyList.size();
    }

}