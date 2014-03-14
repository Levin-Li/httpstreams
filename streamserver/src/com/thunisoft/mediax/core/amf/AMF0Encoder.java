package com.thunisoft.mediax.core.amf;

import java.io.EOFException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.codec.Encoder;
import org.apache.commons.codec.EncoderException;

public class AMF0Encoder implements Encoder {

    @Override
    public ByteBuffer encode(Object source) throws EncoderException {
        Result rst = new Result();

        if(null != source && source.getClass().isArray()) {
            int length = Array.getLength(source);

            // array
            for (int i = 0; i < length; i++) {
                Object elment = Array.get(source, i);
                encodeObject(elment, rst);
            }
        } else {
            encodeObject(source, rst);
        }

        return rst.asByteBuffer();
    }

    @SuppressWarnings("unchecked")
    private void encodeObject(Object source, Result rst) throws EncoderException {
        // null
        if (null == source) {
            rst.appendUInt8(AMFType.DT_NULL);
            encodeNull(rst);
            return;
        } 
        
        // string
        if (source instanceof String) {
            rst.appendUInt8(AMFType.DT_STRING);
            encodeString((String) source, rst);
            return;
        }
        
        // boolean
        if (source instanceof Boolean || source.getClass().equals(boolean.class)) {
            rst.appendUInt8(AMFType.DT_BOOLEAN);
            encodeBoolean((Boolean) source, rst);
            return;
        } 
        
        // timestamp
        if (source instanceof Date) {
            rst.appendUInt8(AMFType.DT_DATETIME);
            encodeTimestamp((Date) source, rst);
            return;
        } else if (source instanceof Calendar) {
            rst.appendUInt8(AMFType.DT_DATETIME);
            encodeTimestamp((Calendar) source, rst);
            return;
        } 
        
        // number
        if (source instanceof Number
                || source.getClass().equals(byte.class)
                || source.getClass().equals(short.class)
                || source.getClass().equals(int.class)
                || source.getClass().equals(long.class)
                || source.getClass().equals(float.class)
                || source.getClass().equals(double.class)) {
            // 使用自动装箱，然后转成Number
            rst.appendUInt8(AMFType.DT_NUMBER);
            encodeNumber((Number)(Object)source, rst);
            
            return;
        } 
        
        // AMFArray
        if (source instanceof AMFArray) {
            rst.appendUInt8(AMFType.DT_ECMA_ARRAY);
            encodeEcmaArray((AMFArray) source, rst);
            return;
        } 
        
        // AMFObject or Map
        if (source instanceof AMFObject) {
            rst.appendUInt8(AMFType.DT_OBJECT);
            encodeEcmaObject((AMFObject) source, rst);
            return;
        } else if (source instanceof Map) {
            rst.appendUInt8(AMFType.DT_OBJECT);
            encodeEcmaObject((Map<String, Object>) source, rst);
            return;
        } 
        
        // strict array
        if (source.getClass().isArray()) {
            rst.appendUInt8(AMFType.DT_ARRAY);
            encodeStrictArray(source, rst);
            return;
        } else if (source instanceof List) {
            rst.appendUInt8(AMFType.DT_ARRAY);
            encodeStrictArray((List<Object>) source, rst);
            return;
        }
        
        
        throw new EncoderException("不支持的类型[" + source.getClass() + "]");
    }



    private void encodeStrictArray(Object array, Result rst) throws EncoderException {
        // array length
        int length = Array.getLength(array);
        rst.appendUInt32(length);

        // array
        for (int i = 0; i < length; i++) {
            Object elment = Array.get(array, i);
            encodeObject(elment, rst);
        }
    }

    private void encodeStrictArray(List<Object> array, Result rst) throws EncoderException {
        // array length
        int length = array.size();
        rst.appendUInt32(length);

        // array
        for (Object object : array) {
            encodeObject(object, rst);
        }
    }


    private void encodeEcmaArray(AMFArray array, Result rst) throws EncoderException {
        // array length
        int length = array.size();
        rst.appendUInt32(length);

        // array
        for (Entry entry : array) {
            String name = entry.getName();
            Object value = entry.getValue();

            encodeString(name, rst);
            encodeObject(value, rst);
        }

        // end
        rst.append(AMFType.EOF);
    }


    private void encodeEcmaObject(AMFObject source, Result rst) throws EncoderException {
        // <key, value>
        for (Entry entry : source) {
            String name = entry.getName();
            Object value = entry.getValue();

            encodeString(name, rst);
            encodeObject(value, rst);
        }

        // end
        rst.append(AMFType.EOF);

    }

    private void encodeEcmaObject(Map<String, Object> object, Result rst) throws EncoderException {
        // <key, value>
        for (Map.Entry<String, Object> entry : object.entrySet()) {
            String name = entry.getKey();
            Object value = entry.getValue();

            encodeString(name, rst);
            encodeObject(value, rst);
        }

        // end
        rst.append(AMFType.EOF);
    }


    private void encodeBoolean(boolean isTrue, Result rst) {
        rst.appendUInt8(isTrue ? 1 : 0);
    }


    private void encodeNull(Result rst) {

    }

    private void encodeString(String value, Result result) {
        byte[] content = value.getBytes(Charset.forName("UTF-8"));

        // 2 字节长度, UInt(16)
        result.appendUInt16(content.length);

        // UTF-8 编码
        result.append(content);
    }

    private void encodeNumber(Number n, Result rst) {
        rst.appendDouble(n.doubleValue());
    }

    private void encodeTimestamp(Date date, Result rst) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        encodeTimestamp(calendar, rst);
    }

    private void encodeTimestamp(Calendar calendar, Result rst) {
        long time = calendar.getTimeInMillis();
        TimeZone timeZone = calendar.getTimeZone();

        rst.appendDouble(time);
        rst.appendUInt16(timeZone.getRawOffset() / 1000 / 60);
    }

    private static class Result {
        private int countBytes;
        private List<byte[]> segments = new LinkedList<byte[]>();


        public void appendUInt8(int value) {
            ByteBuffer buffer = ByteBuffer.allocate(1);
            buffer.put((byte)value);

            append(buffer.array());
        }

        public void appendUInt16(int value) {
            ByteBuffer buffer = ByteBuffer.allocate(2);
            buffer.putShort((short)value);

            append(buffer.array());
        }

        public void appendUInt32(int value) {
            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.putInt(value);

            append(buffer.array());
        }



        public void appendDouble(double value) {
            ByteBuffer buffer = ByteBuffer.allocate(8);

            long lValue = Double.doubleToLongBits(value);
            buffer.putLong(lValue);

            append(buffer.array());
        }

        public void append(byte[] bytes) {
            segments.add(bytes);
            countBytes += bytes.length;
        }

        public void appendObjectType(int type) {
            appendUInt8(type);
        }

        public ByteBuffer asByteBuffer() {
            ByteBuffer rst = ByteBuffer.allocate(countBytes);

            for (byte[] segment : segments) {
                rst.put(segment);
            }

            rst.flip();
            return rst;
        }
    }
}
