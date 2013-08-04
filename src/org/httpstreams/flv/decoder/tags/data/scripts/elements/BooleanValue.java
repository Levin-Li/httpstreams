package org.httpstreams.flv.decoder.tags.data.scripts.elements;

import java.io.IOException;

import org.httpstreams.flv.FlvSupports;
import org.httpstreams.flv.StructureInputStream;


public class BooleanValue extends AbstractValue {

	public BooleanValue() {
		super(FlvSupports.SCRIPT_1_Boolean);
	}

	@Override
	public Object readTypedValue(StructureInputStream inStream) throws IOException {
		int bInt = inStream.readUI8();
		
		return 0 != bInt;
	}
}
