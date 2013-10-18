package github.chenxh.media.flv.script.metadata;

import github.chenxh.media.UnsignedDataOutput;
import github.chenxh.media.flv.script.EcmaArray;
import github.chenxh.media.flv.script.EcmaObject;
import github.chenxh.media.flv.script.MovieClip;
import github.chenxh.media.flv.script.ScriptDataType;
import github.chenxh.media.flv.script.StrictArray;
import github.chenxh.media.flv.script.AbstractDynamicObject.Entry;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

import org.slf4j.LoggerFactory;



public class MetaDataEncoder {
    private static final org.slf4j.Logger logger = LoggerFactory
            .getLogger(MetaDataDecoder.class);

    private static final byte[] OBJECT_END = { 0, 0, 9 };

    public byte[] encode(FlvMetaData metaData) throws IOException {
        ByteArrayOutputStream target = new ByteArrayOutputStream();

        encode(metaData, new UnsignedDataOutput(target));
        return toBytes(target);
    }

    public void encode(FlvMetaData metaData, UnsignedDataOutput target) throws IOException {
        // onMetaData
        writeObject("onMetaData", target);

        // metadata ecmaArray
        metaData.set(FlvMetaData.P_METADATA_CREATOR, "chenxiuheng@gmail.com");
        writeObject(metaData.getRawObject(), target);

        // End
        target.write(OBJECT_END);
    }

    private void writeEcmaArray(EcmaArray array, UnsignedDataOutput target) throws IOException {
        ensureNotNull(array);

        // length
        target.writeUI32(array.size());
        
        // name-value
        Iterator<Entry> entryItr = array.iterator();
        while(entryItr.hasNext()) {
            Entry entry = entryItr.next();
            writeString(entry.getKey(), target);
            writeObject(entry.getValue(), target);
            
            logger.debug("[{}={}]", entry.getKey(), entry.getValue());
        }
        
        // end
        target.write(OBJECT_END);
    }


    private void writeString(String value, UnsignedDataOutput target) throws IOException {
        ensureNotNull(value);

        byte[] bytes = value.getBytes();
        target.writeUI16(bytes.length);
        target.write(bytes);
    }

    private void writeNumber(Number value, UnsignedDataOutput target) throws IOException {
        ensureNotNull(value);

        target.writeDouble(value.doubleValue());
    }

    private void writeBoolean(boolean isTrue, UnsignedDataOutput target) throws IOException {
        target.writeUI8((isTrue ? 1 : 0));
    }
    
    private void writeMoviceClip(MovieClip value, UnsignedDataOutput target) throws IOException {
        ensureNotNull(value);

        writeString(value.stringValue(), target);
    }

    private void writeTimestamp(Date value, UnsignedDataOutput target) throws IOException {
        ensureNotNull(value);

        long datetime = value.getTime();
        target.writeDouble(datetime);

        // Local time offset in minutes from UTC. For
        // time zones located west of Greenwich, UK
        // this value is a negative number. Time zone
        // east of Greenwich, UK, are positive.
        int timeOffset = TimeZone.getDefault().getRawOffset() / 1000 / 60;
        target.writeUI16(timeOffset);
    }

    private void writeArray(StrictArray array, UnsignedDataOutput target) throws IOException {
        ensureNotNull(array);
    
        // array length
        long arraySize = array.size();
        target.writeUI32(arraySize);
        

        // array
        for (Object object : array) {
            writeObject(object, target);
        }

    }

    private void writeEcmaObject(EcmaObject object, UnsignedDataOutput target) throws IOException {
        ensureNotNull(object);

        // name-value
        Iterator<Entry> entryItr = object.iterator();
        while(entryItr.hasNext()) {
            Entry entry = entryItr.next();
            writeString(entry.getKey(), target);
            writeObject(entry.getValue(), target);

            logger.debug("[{}={}]", entry.getKey(), entry.getValue());
        }
        
        // end
        target.write(OBJECT_END);
        
    }
    private void writeObject(Object value, UnsignedDataOutput target) throws IOException {
        if (ScriptDataType.isNull(value)) {
            writeType(ScriptDataType.DT_NULL, target);
        } else if (value instanceof Boolean) {
            writeType(ScriptDataType.DT_BOOLEAN, target);
            writeBoolean((Boolean)value, target);
        } else if (value instanceof String) {
            writeType(ScriptDataType.DT_STRING, target);
            writeString((String)value, target);
        } else if (value instanceof Number) {
            writeType(ScriptDataType.DT_NUMBER, target);
            writeNumber((Number)value, target);
        } else if (value instanceof MovieClip) {
            writeType(ScriptDataType.DT_MOVIE_CLIP, target);
            writeMoviceClip((MovieClip)value, target);
        } else if (value instanceof EcmaArray) {
            writeType(ScriptDataType.DT_ECMA_ARRAY, target);
            writeEcmaArray((EcmaArray)value, target);
        } else if (value instanceof EcmaObject) {
            writeType(ScriptDataType.DT_OBJECT, target);
            writeEcmaObject((EcmaObject)value, target);
        }  else if (value instanceof StrictArray) {
            writeType(ScriptDataType.DT_ARRAY, target);
            writeArray((StrictArray)value, target);
        } else if (value instanceof Date) {
            writeType(ScriptDataType.DT_DATETIME, target);
            writeTimestamp((Date)value, target);
        }else {
            throw new IllegalArgumentException("Unsupport DataType[" + value + "]");
        }
    } 


    private void writeType(int type, UnsignedDataOutput target) throws IOException {
        target.writeUI8(type);
    }

    private void ensureNotNull(Object v) {
        if (null == v) {
            throw new NullPointerException();
        }
    }

    public static byte[] toBytes(ByteArrayOutputStream target) {
        return target.toByteArray();
    }
}
