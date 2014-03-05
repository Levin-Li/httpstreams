package com.thunisoft.mediax.core.amf;

import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimeZone;

import org.apache.commons.codec.Decoder;
import org.apache.commons.codec.DecoderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thunisoft.mediax.core.ByteBufferUtils;

/**
 * AMF 解码
 * 
 * <p>
 * @since V1.0  2014-3-3
 * @author chenxh
 */
public class AMF0Decoder implements Decoder {
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Object decode(Object source) throws DecoderException {
        if (source instanceof ByteBuffer) {
            return decode((ByteBuffer) source);
        } else if (source instanceof byte[]) {
            return decode(ByteBuffer.wrap((byte[]) source));
        } else {
            throw new IllegalArgumentException("unsupport Object[" + source + "]");
        }
    }

    public Object decode(ByteBuffer buffer) throws DecoderException {
        return readObject(buffer);
    }

    private Object readObject(ByteBuffer data) {
        int valueType = data.get();
        final Object value;
        switch (valueType) {
            case AMFType.DT_NUMBER:
                value = readNumber(data);
                break;
            case AMFType.DT_BOOLEAN:
                value = readBoolean(data);
                break;
            case AMFType.DT_STRING:
                value = readString(data);
                break;
            case AMFType.DT_OBJECT:
                value = readEcmaObject(data);
                break;
            case AMFType.DT_MOVIE_CLIP:
                value = readMovieClip(data);
                break;
            case AMFType.DT_NULL:
                value = readNull(data);
                break;
            case AMFType.DT_ECMA_ARRAY:
                value = readEcmaArray(data);
                break;
            case AMFType.DT_ARRAY:
                value = readArray(data);
                break;
            case AMFType.DT_DATETIME:
                value = readTimestamp(data);
                break;
            case AMFType.DT_LONGSTRIING:
                value = readLongString(data);
                break;
            case AMFType.DT_END:
                value = AMFType.END;
                break;
            case AMFType.DT_REFERENCE:
            case AMFType.DT_UNDEFINED:
            default:
                throw new UnsupportedAMFTypeException(valueType);
        }

        return value;
    }

    private AMFArray readEcmaArray(ByteBuffer data) {
        int size = data.getInt();
        return (AMFArray) setProperties(data, new AMFArray(size), size);
    }
    
    private AMFObject readEcmaObject(ByteBuffer data) {
        return (AMFObject) setProperties(data, new AMFObject(), Integer.MAX_VALUE);
    }
    
    private DynamicObject setProperties(ByteBuffer data, DynamicObject object, int maxElementNum) {
        String key = null;
        Object value = null;
        try {
            for (int i = 0; i < maxElementNum; i++) {
                if (data.remaining() <= 0)  {
                    break;
                }
                
                key = null;
                key = readString(data);
                value = readObject(data);

                // 009 结尾
                if (key.length() == 0 && AMFType.END == value) {
                    break;
                } else {
                    object.put(key, value);
                    logger.debug("put {}, {}", key, value);
                }
            }
        } catch (Exception e) {
            logger.debug("解析属性[" + key+ "] 发生错误，原因：" + e.getMessage(), e);
        }
        
        return object;
    }

    private double readNumber(ByteBuffer data) {
        if (data.remaining() >= 8) {
            long number = ByteBufferUtils.readUInt64(data);
            return Double.longBitsToDouble(number);
        } else {
            long v = 0;
            while(data.remaining() > 0) {
                v = v << 8 + ByteBufferUtils.readUInt8(data);
            }
            return v;
        }
        
    }

    private boolean readBoolean(ByteBuffer data) {
        return 0 != data.get();
    }

    private String readString(ByteBuffer data) {
        int length = (int) ByteBufferUtils.readUInt16(data);
        if (length <= 0) {
            return "";
        } else {
            byte[] buffer = new byte[Math.min(length, data.remaining())];
            data.get(buffer);
            return new String(buffer);
        }
    }


    private Object readMovieClip(ByteBuffer data) {
        throw new UnsupportedOperationException("unsupport MovieClip for AMF0");
    }

    private Object readNull(ByteBuffer data) {
        return null;
    }


    private Object readArray(ByteBuffer data) {
        long arraySize = ByteBufferUtils.readUInt32(data);

        Object[] array = new Object[(int) arraySize];
        
        try {
            for (int i = 0; i < arraySize; i++) {
                array[i] = readObject(data);
            }
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
        }

        return array;
    }

    private String readLongString(ByteBuffer data) {
        int length = (int) data.getInt();
        if (length < 0) {
            return "";
        } else {
            byte[] buffer = new byte[length];
            data.get(buffer);
            return new String(buffer);
        }
    }

    private Timestamp readTimestamp(ByteBuffer data) {
        long datetime = (long) data.getDouble();

        // Local time offset in minutes from UTC. For
        // time zones located west of Greenwich, UK
        // this value is a negative number. Time zone
        // east of Greenwich, UK, are positive.
        long metaDataTimeOffset = data.getShort() * 60;
        long systemTimeOffset = TimeZone.getDefault().getRawOffset() / 1000;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(datetime);

        // 校正时间
        // 例如，把东三区的时间，改成东八区的时间
        calendar.add(Calendar.SECOND, (int) (systemTimeOffset - metaDataTimeOffset));
        return new java.sql.Timestamp(datetime);
    }
}
