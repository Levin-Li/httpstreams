package org.httpstreams.flv.decoder.tags.data.scripts;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
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
    private List<Value> container = new LinkedList<Value>();

    /* (non-Javadoc)
     * @see org.httpstreams.flv.decoder.tags.data.TagData#read(org.httpstreams.flv.StructureInputStream, long)
     */
    public void read(StructureInputStream inStream, long length)
            throws IOException {
        // 把脚本内容阶段,避免读取额外的数据
        StructureInputStream newInStream = StructureInputStream.subInputStream(inStream, length);
        
        container.clear();
        readObject(newInStream);
    }
    
    public boolean isMetaData () {
        return isMetaData(container);
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
        
        ECMAArrayValue metadata = (ECMAArrayValue) container.get(1);
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
    private void readObject(StructureInputStream newInStream) throws IOException {
        
        // read objects
        int type;
        try {
            do {
                // 预读取前三个字节，查看是否已经到包的末尾了
                // 如果已经读到了文件末尾，则直接退出
                int tag = newInStream.preReadUI24();
                if (-1 == tag) {
                    break; // 已经到文件末尾了
                } else if (FlvSupports.SCRIPT_9_END == tag) {
                    newInStream.skip(3);
                    break;
                }
                
                type = newInStream.readUI8();
                Value entry = ValueFactory.getEmptyValue(type).read(newInStream);
                container.add(entry);
            } while (true);
        } catch (Exception e) {
            e.printStackTrace();
        }
      
    }

    
    @Override
    public String toString() {
        return container.toString();
    }
}
