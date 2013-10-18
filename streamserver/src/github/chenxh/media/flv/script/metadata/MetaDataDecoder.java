package github.chenxh.media.flv.script.metadata;

import github.chenxh.media.UnsignedDataInput;
import github.chenxh.media.flv.script.EcmaArray;
import github.chenxh.media.flv.script.AbstractDynamicObject;
import github.chenxh.media.flv.script.EcmaObject;
import github.chenxh.media.flv.script.MovieClip;
import github.chenxh.media.flv.script.ScriptDataType;
import github.chenxh.media.flv.script.StrictArray;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimeZone;

import org.slf4j.LoggerFactory;

public class MetaDataDecoder {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MetaDataDecoder.class);
    
    public FlvMetaData decode(byte[] scriptData) throws IOException {
     // 对脚本数据进行解析
        UnsignedDataInput input = new UnsignedDataInput(new ByteArrayInputStream(scriptData));
        FlvMetaData metaData = parseMetaData(input);
        metaData.setRawBytes(scriptData);
        
        return metaData;
    }

    private FlvMetaData parseMetaData(UnsignedDataInput inStream) throws IOException{
        int keyType = inStream.readUI8();
        if (keyType != ScriptDataType.DT_STRING) {
            return null;
        }
        String onMetadata = readString(inStream);
        if (!"onMetaData".equals(onMetadata)) {
            // 果然不是 metadata
            return null;
        }

        int valueType = inStream.readUI8();
        if (valueType != ScriptDataType.DT_ECMA_ARRAY) {
            // 不是标准类型
            return null;
        }

        EcmaArray array = readEcmaArray(inStream);
        return FlvMetaData.parseEcmaArray(null, array);
    }

    private EcmaArray readEcmaArray(UnsignedDataInput inStream) throws IOException {
        int arrayLength = (int)inStream.readUI32();
        EcmaArray array = new EcmaArray(arrayLength);
        
        try {
            setEcmaArrayValues(inStream, array);
        } catch (Exception e) {
            logger.debug("解析 EcmaArray 发生错误，原因：" + e.getMessage(), e);
        }

        return array;
    }

    private EcmaObject readEcmaObject(UnsignedDataInput inStream) {
        EcmaObject array = new EcmaObject();
        
        try {
            setEcmaArrayValues(inStream, array);
        } catch (Exception e) {
            logger.debug("解析 EcmaArray 发生错误，原因：" + e.getMessage(), e);
        }

        return array;
    }
    
    
    private void setEcmaArrayValues(UnsignedDataInput inStream, AbstractDynamicObject array) throws IOException {
        do {
            String key = readString(inStream);
            Object value = readObject(inStream);
            logger.info("Entry[{}={}]", key, value);

            // 009 结尾 
            if ( key.length() == 0 
                    && ScriptDataType.END == value) {
                break;
            } else {
                array.put(key, value);
            }
        } while(true);
    }

    private Object readObject(UnsignedDataInput inStream) {
        try {
            int valueType = inStream.readUI8();
            final Object value;
            switch (valueType) {
                case ScriptDataType.DT_NUMBER:
                    value = readNumber(inStream);
                    break;
                case ScriptDataType.DT_BOOLEAN:
                    value = readBoolean(inStream);
                    break;
                case ScriptDataType.DT_STRING:
                    value = readString(inStream);
                    break;
                case ScriptDataType.DT_OBJECT:
                    value = readEcmaObject(inStream);
                    break;
                case ScriptDataType.DT_MOVIE_CLIP:
                    value = readMovieClip(inStream);
                    break;
                case ScriptDataType.DT_NULL:
                    value = readNull(inStream);
                    break;
                case ScriptDataType.DT_ECMA_ARRAY:
                    value = readEcmaArray(inStream);
                    break;
                case ScriptDataType.DT_ARRAY:
                    value = readArray(inStream);
                    break;
                case ScriptDataType.DT_DATETIME:
                    value = readTimestamp(inStream);
                    break;
                case ScriptDataType.DT_LONGSTRIING:
                    value = readLongString(inStream);
                    break;
                case ScriptDataType.DT_REFERENCE:
                case ScriptDataType.DT_UNDEFINED:
                default:
                    value = ScriptDataType.END;
            }

            logger.debug("[{}]", value);
            return value;
        } catch (Exception e) {
            return ScriptDataType.END;
        }
        
    }



    private double readNumber(UnsignedDataInput inStream) throws IOException {
        return inStream.readDouble();
    }

    private boolean readBoolean(UnsignedDataInput inStream) throws IOException {
        return 0 != inStream.readUI8();
    }
    
    private String readString(UnsignedDataInput inStream) throws IOException {
        int length = (int)inStream.readUI16();
        if (length < 0) {
            return "";
        } else {
            byte[] buffer = new byte[length];
            inStream.readFully(buffer);
            return new String(buffer);
        }
    }


    private Object readMovieClip(UnsignedDataInput inStream) throws IOException {
        // TODO 需要确认
        String movieClipPath = readString(inStream);
        return new MovieClip(movieClipPath);
    }

    private Object readNull(UnsignedDataInput inStream) {
        return ScriptDataType.NULL;
    }


    private Object readArray(UnsignedDataInput inStream) throws IOException {
        long arraySize = inStream.readUI32();

        StrictArray array = new StrictArray((int)arraySize);
        for (int i = 0; i < arraySize; i++) {
            array.add(readObject(inStream));
        }

        return array;
    }
    
    private String readLongString(UnsignedDataInput inStream) throws IOException {
        int length = (int)inStream.readUI32();
        if (length < 0) {
            return "";
        } else {
            byte[] buffer = new byte[length];
            inStream.readFully(buffer);
            return new String(buffer);
        }
    }
    
    private Timestamp readTimestamp(UnsignedDataInput inStream) throws IOException {
        long datetime = (long)inStream.readDouble();

        // Local time offset in minutes from UTC. For
        // time zones located west of Greenwich, UK
        // this value is a negative number. Time zone
        // east of Greenwich, UK, are positive.
        long metaDataTimeOffset = inStream.readUI16() * 60;
        long  systemTimeOffset = TimeZone.getDefault().getRawOffset() / 1000;
        logger.debug("metaDataTimeOffset:[{}h], systemTimeOffset:[{}h]", metaDataTimeOffset/60/60, systemTimeOffset/60/60);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(datetime);

        // 校正时间
        // 例如，把东三区的时间，改成东八区的时间
        calendar.add(Calendar.SECOND, (int)(systemTimeOffset - metaDataTimeOffset));
        return new java.sql.Timestamp(datetime);
    }

}
