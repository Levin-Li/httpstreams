package github.chenxh.media.flv.tags;

import github.chenxh.media.IDataTrunk;
import github.chenxh.media.UnsignedDataInput;
import github.chenxh.media.flv.FlvSignature;
import github.chenxh.media.flv.ITagTrunk;
import github.chenxh.media.flv.ITagData;
import github.chenxh.media.flv.script.EcmaArray;
import github.chenxh.media.flv.script.FlvMetaData;
import github.chenxh.media.flv.script.StrictArray;
import github.chenxh.media.flv.script.ScriptDataType;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ֻ��ȡ MetaData ��Ϣ
 * 
 * @author chenxh
 * 
 */
public class MetaDataVisitor extends TagDataVistorAdapter {
    // ��ദ��  8MB ��metadata
    private static final int MAX_SCRIPT_DATASIZE = 1024 * 1024 * 8;

    private Logger logger = LoggerFactory.getLogger(getClass());


    private FlvMetaData metaData;
    
    @Override
    public ITagData readScriptData(FlvSignature flv, IDataTrunk header, UnsignedDataInput inStream) throws IOException {
        if (header.getDataSize() > MAX_SCRIPT_DATASIZE){ 
            throw new IllegalArgumentException("ScriptData is too big to analysis, maxScriptSize is " +  MAX_SCRIPT_DATASIZE);
        }
        
        // �����нű�����ȡ����
        // �����Ȼ����ٶ�����ɺ������ݴ������
        byte[] script = new byte[(int) header.getDataSize()];
        inStream.readFully(script);
        
        // �Խű����ݽ��н���
        UnsignedDataInput scriptData = new UnsignedDataInput(new ByteArrayInputStream(script));
        return metaData = parseMetaData(scriptData);
    }
    
    private FlvMetaData parseMetaData(UnsignedDataInput inStream) throws IOException{
        int keyType = inStream.readUI8();
        if (keyType != ScriptDataType.DT_STRING) {
            return null;
        }
        String onMetadata = readString(inStream);
        if (!"onMetaData".equals(onMetadata)) {
            // ��Ȼ���� metadata
            return null;
        }

        int valueType = inStream.readUI8();
        if (valueType != ScriptDataType.DT_ECMA_ARRAY) {
            // ���Ǳ�׼����
            return null;
        }

        int arrayLength = (int)inStream.readUI32();
        EcmaArray array = readEcmaArray(inStream, arrayLength);
        return FlvMetaData.parseEcmaArray(array);
    }

    private EcmaArray readEcmaArray(UnsignedDataInput inStream, int arrayLength) {
        EcmaArray array = new EcmaArray(arrayLength);
        
        try {
            setEcmaArrayValues(inStream, array);
        } catch (Exception e) {
            logger.debug("���� EcmaArray ��������ԭ��" + e.getMessage(), e);
        }

        return array;
    }

    private void setEcmaArrayValues(UnsignedDataInput inStream, EcmaArray array) throws IOException {
        do {
            String key = readString(inStream);
            Object value = readObject(inStream);
            logger.info("Entry[{}={}]", key, value);

            // 009 ��β 
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
                    value = readEcmaArray(inStream, 6);
                    break;
                case ScriptDataType.DT_MOVIE_CLIP:
                    value = readMovieClip(inStream);
                    break;
                case ScriptDataType.DT_NULL:
                    value = readNull(inStream);
                    break;
                case ScriptDataType.DT_ECMA_ARRAY:
                    int arrayLength = (int)inStream.readUI32();
                    value = readEcmaArray(inStream, arrayLength);
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
        // TODO ��Ҫȷ��
        String movieClipPath = readString(inStream);
        return movieClipPath;
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

        // У��ʱ��
        // ���磬�Ѷ�������ʱ�䣬�ĳɶ�������ʱ��
        calendar.add(Calendar.SECOND, (int)(systemTimeOffset - metaDataTimeOffset));
        return new java.sql.Timestamp(datetime);
    }

    @Override
    public boolean interruptAfter(ITagTrunk tag) throws IOException, EOFException {
        return tag.getType() == ITagTrunk.SCRIPT_DATA;
    }
    public FlvMetaData getMetaData() {
        return metaData;
    }

}
