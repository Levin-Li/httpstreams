package org.httpstreams.flv.decoder.tags.data.scripts.elements;

import java.io.IOException;

import org.httpstreams.flv.FlvSupports;
import org.httpstreams.flv.StructureInputStream;


public class NullValue extends AbstractValue {

    public NullValue() {
        super(FlvSupports.SCRIPT_5_Null);
    }

    @Override
    protected Object readTypedValue(StructureInputStream inStream)
            throws IOException {
        System.out.println("Null Value");
        return null;
    }

}
