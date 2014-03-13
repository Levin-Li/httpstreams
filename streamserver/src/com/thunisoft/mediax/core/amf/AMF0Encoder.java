package com.thunisoft.mediax.core.amf;

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
                append(elment, rst);
            }
        } else {
            append(source, rst);
        }

        return rst.asByteBuffer();
    }

    @SuppressWarnings("unchecked")
    private void append(Object source, Result rst) throws EncoderException {
        if (null == source) {
            rst.appendUInt8(AMFType.DT_NULL);
            encodeNull(rst);
        } else if (source instanceof AMFArray) {
            rst.appendUInt8(AMFType.DT_ECMA_ARRAY);
            encodeEcmaArray((AMFArray) source, rst);
        } else if (source instanceof AMFObject) {
            rst.appendUInt8(AMFType.DT_OBJECT);
            encodeEcmaObject((AMFObject) source, rst);
        } else if (source instanceof Map) {
            rst.appendUInt8(AMFType.DT_OBJECT);
            encodeEcmaObject((Map<String, Object>) source, rst);
        } else if (source.getClass().isArray()) {
            rst.appendUInt8(AMFType.DT_ARRAY);
            encodeArray(source, rst);
        } else if (source instanceof List) {
            rst.appendUInt8(AMFType.DT_ARRAY);
            encodeArray((List<Object>) source, rst);
        } else if (source instanceof Boolean || source.getClass().equals(boolean.class)) {
            rst.appendUInt8(AMFType.DT_BOOLEAN);
            encodeBoolean((Boolean) source, rst);
        } else if (source instanceof String) {
            rst.appendUInt8(AMFType.DT_STRING);
            encodeString((String) source, rst);
        } else if (source instanceof Date) {
            rst.appendUInt8(AMFType.DT_DATETIME);
            encodeTimestamp((Date) source, rst);
        } else if (source instanceof Calendar) {
            rst.appendUInt8(AMFType.DT_DATETIME);
            encodeTimestamp((Calendar) source, rst);
        } else if (source instanceof Number
                || source.getClass().equals(byte.class)
                || source.getClass().equals(short.class)
                || source.getClass().equals(int.class)
                || source.getClass().equals(long.class)
                || source.getClass().equals(float.class)
                || source.getClass().equals(double.class)) {
            // 使用自动装箱，然后转成Number
            rst.appendUInt8(AMFType.DT_NUMBER);
            encodeNumber((Number)(Object)source, rst);
        } else {
            throw new EncoderException("不支持的类型[" + source.getClass() + "]");
        }
    }



    private void encodeArray(Object array, Result rst) throws EncoderException {
        // array length
        int length = Array.getLength(array);
        rst.appendUInt32(length);

        // array
        for (int i = 0; i < length; i++) {
            Object elment = Array.get(array, i);
            append(elment, rst);
        }

        // end
        rst.append(AMFType.EOF);
    }

    private void encodeArray(List<Object> array, Result rst) throws EncoderException {
        // array length
        rst.appendUInt32(array.size());

        // array
        for (Object object : array) {
            append(object, rst);
        }

        // end
        rst.append(AMFType.EOF);
    }


    private void encodeEcmaArray(AMFArray array, Result rst) throws EncoderException {
        // array length
        int length = array.size();
        rst.appendUInt32(length);

        // array
        for (Entry entry : array) {
            String name = entry.getName();
            Object value = entry.getValue();

            rst.append(name.getBytes(Charset.forName("UTF-8")));
            append(value, rst);
        }

        // end
        rst.append(AMFType.EOF);
    }


    private void encodeEcmaObject(AMFObject source, Result rst) throws EncoderException {
        // array
        for (Entry entry : source) {
            String name = entry.getName();
            Object value = entry.getValue();

            rst.append(name.getBytes(Charset.forName("UTF-8")));
            append(value, rst);
        }

        // end
        rst.append(AMFType.EOF);

    }

    private void encodeEcmaObject(Map<String, Object> object, Result rst) throws EncoderException {
        // <key, value>
        for (Map.Entry<String, Object> entry : object.entrySet()) {
            String name = entry.getKey();
            Object value = entry.getValue();

            rst.append(name.getBytes(Charset.forName("UTF-8")));
            append(value, rst);
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
            byte[] content = new byte[1];
            content[0] = (byte) ((value >> 0) & 0xFF);

            append(content);
        }

        public void appendUInt16(int value) {
            byte[] content = new byte[2];
            content[0] = (byte) ((value >> 8) & 0xFF);
            content[1] = (byte) ((value >> 0) & 0xFF);

            append(content);
        }

        public void appendUInt32(int value) {
            byte[] content = new byte[4];
            content[0] = (byte) ((value >> 24) & 0xFF);
            content[1] = (byte) ((value >> 16) & 0xFF);
            content[2] = (byte) ((value >> 8) & 0xFF);
            content[3] = (byte) ((value >> 0) & 0xFF);

            append(content);
        }



        public void appendDouble(double value) {
            ByteBuffer buffer = ByteBuffer.allocate(8);

            buffer.putDouble(value);

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
