package org.httpstreams.flv.decoder.tags.data.scripts.elements;

import java.io.IOException;

import org.httpstreams.flv.FlvSupports;
import org.httpstreams.flv.StructureInputStream;


public class NumberValue extends AbstractValue {

	public NumberValue() {
		super(FlvSupports.SCRIPT_0_Number);
	}

	@Override
	public Object readTypedValue(StructureInputStream inStream) throws IOException {
		return inStream.readDouble();
	}
}
