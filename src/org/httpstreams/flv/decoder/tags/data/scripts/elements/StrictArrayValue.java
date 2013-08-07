package org.httpstreams.flv.decoder.tags.data.scripts.elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.httpstreams.flv.StructureInputStream;


public class StrictArrayValue implements Value {
	private List<Value> items = new ArrayList<Value>();

    public Value read(StructureInputStream inStream) throws IOException {
        long itemNumber = inStream.readUI32();
        while (itemNumber > 0) {
            int type = inStream.readUI8();
            Value value =  ValueFactory.getEmptyValue(type).read(inStream);
            items.add(value);

            itemNumber--;
        }
        
        return this;
    }
    
    public Value valueAt(int index) {
        return index < items.size()? items.get(index):null;
    }

    public Iterator<Value> iterator () {
        return items.iterator();
    }

    public List<Value> getValue() {
        return items;
    }
	
    public String toString() {
        return items.toString();
    }
}
