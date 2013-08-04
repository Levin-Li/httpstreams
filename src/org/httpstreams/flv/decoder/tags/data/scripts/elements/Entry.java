/**
 * 
 */
package org.httpstreams.flv.decoder.tags.data.scripts.elements;

import java.io.IOException;

import org.httpstreams.flv.FlvSupports;
import org.httpstreams.flv.StructureInputStream;


public class Entry implements Value {
    private String name;
    private Value value;
    
    @Override
    public Value read(StructureInputStream inStream) throws IOException {
        int strType = FlvSupports.SCRIPT_2_String;
        Value strValue = ValueFactory.getEmptyValue(strType).read(inStream);
        name = (String) strValue.getValue();
        
        int type = inStream.readUI8();
        value =  ValueFactory.getEmptyValue(type).read(inStream);
        
        return this;
    }
    
    /**
     * @param name
     * @param value
     */
    public Entry(String name, Value value) {
        super();
        this.name = name;
        this.value = value;
    }
    public Entry (){}

    public final String getName() {
        return name;
    }
    public final Value getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(name);
        b.append(": ");
        b.append(value);

        return b.toString();
    }
}