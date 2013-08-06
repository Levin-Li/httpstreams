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
    
    private boolean isNull = false;

    @Override
    public Value read(StructureInputStream inStream) throws IOException {
        int strLength = inStream.readUI16();
        byte[] bytes = new byte[strLength];
        inStream.readActual(bytes);
        name = new String(bytes);

        int valueType = inStream.readUI8();
        if (0 == strLength && valueType == FlvSupports.SCRIPT_9_END) {
            // Entry 内容为： 00 9，表示已经到末尾了 
            isNull = true;
            return this;
        }

        
        value =  ValueFactory.getEmptyValue(valueType).read(inStream);
        return this;
    }

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

    /**
     * 
     * @see {@link #isNull isNull} 
     **/
    public final boolean isNull() {
        return isNull;
    }
}