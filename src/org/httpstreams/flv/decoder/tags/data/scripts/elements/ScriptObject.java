package org.httpstreams.flv.decoder.tags.data.scripts.elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.httpstreams.flv.FlvSupports;
import org.httpstreams.flv.StructureInputStream;


public class ScriptObject implements Value {
    private List<Entry> values = new ArrayList<Entry>();

    @Override
    public Object getValue() {
        return values;
    }

    @Override
    public Value read(StructureInputStream inStream) throws IOException {
        do {
            int nextTag = inStream.preReadUI24();
            if (nextTag == FlvSupports.SCRIPT_9_END) {
                inStream.skip(3);
                break;
            }
            
            Entry entry = new Entry();
            entry.read(inStream);
            values.add(entry);
        } while (true);

        return this;
    }

    public Value getProperty (String name) {
        for (Entry entry : values) {
            if (entry.getName().equals(name)) {
                return entry.getValue();
            }
        }
        
        return null;
    }
    
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("{");
        int entryIndex = 0;
        for (Entry entry : values) {
            if (entryIndex != 0) {
                b.append(",");
            }
            b.append(entry.getName());
            b.append(":");
            b.append(entry.getValue());
            
            entryIndex ++;
        }
        b.append("}");
        return b.toString();
    }
}
