package org.httpstreams.flv.decoder.tags.data.scripts.elements;

import java.io.IOException;

import org.httpstreams.flv.FlvSupports;
import org.httpstreams.flv.StructureInputStream;


public class StringValue extends AbstractValue {

	public StringValue() {
		super(FlvSupports.SCRIPT_2_String);
	}

	@Override
	protected Object readTypedValue(StructureInputStream inStream)
	        throws IOException {
	    int length = inStream.readUI16();
	    
	    byte[] value = new byte[length];
	    inStream.readActual(value);
	    
	    return new String(value);
	}
}
