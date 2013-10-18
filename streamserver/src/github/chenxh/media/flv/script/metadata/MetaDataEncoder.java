package github.chenxh.media.flv.script.metadata;

import github.chenxh.media.UnsignedDataOutput;
import github.chenxh.media.flv.script.EcmaArray;
import github.chenxh.media.flv.script.AbstractDynamicObject;
import github.chenxh.media.flv.script.EcmaObject;
import github.chenxh.media.flv.script.MovieClip;
import github.chenxh.media.flv.script.ScriptDataType;
import github.chenxh.media.flv.script.StrictArray;
import github.chenxh.media.flv.script.AbstractDynamicObject.Entry;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import org.slf4j.LoggerFactory;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

public class MetaDataEncoder {
    private static final org.slf4j.Logger logger = LoggerFactory
            .getLogger(MetaDataDecoder.class);

    private static final byte[] OBJECT_END = { 0, 0, 9 };

    public byte[] encode(FlvMetaData metaData) throws IOException {
        ByteOutputStream target = new ByteOutputStream();

        UnsignedDataOutput dataOut = new UnsignedDataOutput(target);
        encode(metaData, dataOut);
        return toBytes(target);
    }

    public void encode(FlvMetaData metaData, UnsignedDataOutput target) throws IOException {
        // onMetaData
        writeObject("onMetaData", target);

        // metadata ecmaArray
        writeObject(metaData.getRawValue(), target);

        // End
        target.write(OBJECT_END);
    }

    private void writeEcmaArray(EcmaArray array, UnsignedDataOutput target) throws IOException {
        // length
        target.writeUI32(array.size());
        
        // name-value
        Iterator<Entry> entryItr = array.iterator();
        while(entryItr.hasNext()) {
            Entry entry = entryItr.next();
            writeString(entry.getKey(), target);
            writeObject(entry.getValue(), target);
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



    
    private void writeMoviceClip(MovieClip value, UnsignedDataOutput target) {
        // TODO Auto-generated method stub
        
    }

    private void writeTimestamp(Date value, UnsignedDataOutput target) {
        // TODO Auto-generated method stub
        
    }

    private void writeArray(StrictArray value, UnsignedDataOutput target) {
        // TODO Auto-generated method stub
        
    }

    private void writeEcmaObject(EcmaObject value, UnsignedDataOutput target) {
        // TODO Auto-generated method stub
        
    }
    private void writeObject(Object value, UnsignedDataOutput target) throws IOException {
        ensureNotNull(value);

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

    public static byte[] toBytes(ByteOutputStream target) {
        byte[] bytes = target.getBytes();
        int count = target.getCount();

        if (count == bytes.length) {
            return bytes;
        } else {
            byte[] newBytes = new byte[count];
            System.arraycopy(bytes, 0, newBytes, 0, count);
            return newBytes;
        }
    }
}
