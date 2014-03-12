package com.thunisoft.mediax.core.amf;



import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DynamicObject extends ArrayList<Entry> {
    private static final Logger logger = LoggerFactory.getLogger(DynamicObject.class);

    /**   */
    private static final long serialVersionUID = 1L;

    public DynamicObject() {
        super();
    }

    public DynamicObject(int initialCapacity) {
        super(initialCapacity);
    }


    public DynamicObject(Map<String, Object> v) {
        for (java.util.Map.Entry<String, Object> e : v.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    public void put(String name, Object value) {
        add(new Entry(name, value));
    }

    public boolean getBoolean(String key) {
        Object b = get(key, false);
        
        if (b instanceof Boolean) {
            return (Boolean)b;
        } else {
            logger.warn("value[{}] of key[{}] is not boolean object", b, key);
            return "true".equals(String.valueOf(b));
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) get(key, null);
    }

    public Object get(String key, Object defaultValue) {
        for (Entry entry : this) {
            String name = entry.getName();
            if (null != name && name.equals(key)) {
                return entry.getValue();
            }
        }

        return defaultValue;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();

        b.append("AMFArray {");

        int entryIndex = 0;
        for (Entry entry : this) {
            if (entryIndex > 0) {
                b.append(", ");
            }

            b.append(entry.getName());
            b.append(":");

            Object value = entry.getValue();
            if (null == value) {
                b.append("null");
            } else if (value instanceof String) {
                b.append("\"").append(value).append("\"");
            } else if (value instanceof Object[]) {
                b.append(Arrays.toString((Object[]) value));
            } else {
                b.append(value);
            }

            entryIndex += 1;
        }
        b.append("}");

        return b.toString();
    }

    public long getLong(String key) {
        return getNumber(key).longValue();
    }

    private Number getNumber(String key) {
        long defaultValue = 0L;

        Object value = get(key, defaultValue);
        if (value instanceof Number) {
            return (Number) value;
        } else {
            logger.warn("value[{}] of key[{}] is not boolean Number", value, key);
            try {
                return Double.parseDouble(String.valueOf(value));
            } catch (Exception e) {
               return defaultValue;
            }
        }
    }

    public double getDouble(String key) {
        return getNumber(key).doubleValue();
    }

    public int getInt(String key) {
        return getNumber(key).intValue();
    }

    public Timestamp getTimestamp(String key) {
        return get(key);
    }

    public String getString(String key) {
        return get(key);
    }

    public AMFObject getAMFObject(String key) {
        return get(key);
    }

    public AMFArray getAMFArray(String key) {
        return get(key);
    }

    public Object[] getArray(String key) {
        return (Object[]) get(key, new Object[0]);
    }

}
