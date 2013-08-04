package org.httpstreams.flv.decoder.tags.data.scripts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.httpstreams.flv.FlvSupports;
import org.httpstreams.flv.StructureInputStream;
import org.httpstreams.flv.decoder.tags.data.TagData;
import org.httpstreams.flv.decoder.tags.data.scripts.elements.ECMAArrayValue;
import org.httpstreams.flv.decoder.tags.data.scripts.elements.ScriptObject;
import org.httpstreams.flv.decoder.tags.data.scripts.elements.StrictArrayValue;
import org.httpstreams.flv.decoder.tags.data.scripts.elements.Value;
import org.httpstreams.flv.decoder.tags.data.scripts.elements.ValueFactory;


public class ScriptData implements TagData {
    private List<Value> values = Collections.emptyList();

    /* (non-Javadoc)
     * @see org.httpstreams.flv.decoder.tags.data.TagData#read(org.httpstreams.flv.StructureInputStream, long)
     */
    public void read(StructureInputStream inStream, long length)
            throws IOException {
        // �ѽű����ݽ׶�,�����ȡ���������
        StructureInputStream newInStream = StructureInputStream.subInputStream(inStream, length);
        
        values = readObject(newInStream);
    }
    
    public boolean isMetaData () {
        return isMetaData(values);
    }

    private boolean isMetaData (List<Value> values) {
        if (values.size() < 2) {
            return false;
        } else if (!"onMetaData".equals(values.get(0).getValue())) {
            return false;
        } else if (values.get(1) instanceof ECMAArrayValue) {
            return true;
        }
        
        return false;
    }

    public long getFilePosition (double timestamp) {
        int position = -1;
        if (!isMetaData()) {
            return position;
        }
        
        ECMAArrayValue metadata = (ECMAArrayValue) values.get(1);
        ScriptObject object = (ScriptObject) metadata.getProperty("keyframes");
        if(null == object) {
            return position;
        }
        
        StrictArrayValue times = (StrictArrayValue) object.getProperty("times");
        StrictArrayValue filepositions = (StrictArrayValue) object.getProperty("filepositions");
        if (null == times || null ==filepositions) {
            return position;
        }

        int itemIndex = 0;
        Iterator<Value> timeItr = times.iterator();
        while (timeItr.hasNext()) {
            Value timeValue = timeItr.next();
            if ((Double)timeValue.getValue() >= timestamp ) {
                break;
            } else {
                itemIndex ++;
            }
        }

        if (filepositions.valueAt(itemIndex) != null) {
            return ((Double)filepositions.valueAt(itemIndex).getValue()).longValue();
        } else {
            return position;
        }
        
        
    }
    private List<Value> readObject(StructureInputStream newInStream) throws IOException {
        
        // read objects
        int type;
        List<Value> values = new ArrayList<Value>();  
        try {
            do {
                // Ԥ��ȡǰ�����ֽڣ��鿴�Ƿ��Ѿ�������ĩβ��
                // ����Ѿ��������ļ�ĩβ����ֱ���˳�
                int tag = newInStream.preReadUI24();
                if (FlvSupports.SCRIPT_9_END == tag) {
                    newInStream.skip(3);
                    break;
                }
                
                type = newInStream.readUI8();
                Value entry = ValueFactory.getEmptyValue(type).read(newInStream);
                values.add(entry);
            } while (true);
        } catch (Exception e) {
            e.printStackTrace();
        }
      
        
        return values;
    }

    
    @Override
    public String toString() {
        return values.toString();
    }
}
