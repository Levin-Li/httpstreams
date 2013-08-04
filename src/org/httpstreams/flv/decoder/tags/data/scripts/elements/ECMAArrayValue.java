package org.httpstreams.flv.decoder.tags.data.scripts.elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.httpstreams.flv.FlvSupports;
import org.httpstreams.flv.StructureInputStream;


public class ECMAArrayValue implements Value {
    private List<Entry> items = new ArrayList<Entry>();

    @Override
    public Value read(StructureInputStream inStream) throws IOException {
        long arrayLength = inStream.readUI32();
        while (arrayLength > 0) {
            // 属性名
            String name = readName(inStream);

            // 属性值
            Value value = readValue(inStream);

            items.add(new Entry(name, value));
            arrayLength--;
        }

        return this;
    }

    /**
     * 获取属性值
     * 
     * @param inStream
     * @return
     * @throws IOException
     */
    private String readName(StructureInputStream inStream) throws IOException {
        int type = FlvSupports.SCRIPT_2_String;
        Value name =  ValueFactory.getEmptyValue(type).read(inStream);

        return (String) name.getValue();
    }

    private Value readValue(StructureInputStream inStream) throws IOException {
        int type = inStream.readUI8();
        return ValueFactory.getEmptyValue(type).read(inStream);
    }

    public Value getProperty (String name) {
        for (Entry entry : items) {
            if (name.equals(entry.getName())) {
                return entry.getValue();
            }
        }
        
        return null;
    }
    
    @Override
    public Object getValue() {
        return items;
    }

    @Override
    public String toString() {
        return items.toString();
    }
}
