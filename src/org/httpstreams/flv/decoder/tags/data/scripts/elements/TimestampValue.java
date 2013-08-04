package org.httpstreams.flv.decoder.tags.data.scripts.elements;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang.LocaleUtils;
import org.httpstreams.flv.FlvSupports;
import org.httpstreams.flv.StructureInputStream;


public class TimestampValue extends AbstractValue {

	public TimestampValue() {
		super(FlvSupports.SCRIPT_11_Date);
	}

	@Override
	protected Object readTypedValue(StructureInputStream inStream) throws IOException {
		double millisecondes = inStream.readDouble();
		int localDateTimeOffset = inStream.readSI16();
		
		Date date = new Date((long)millisecondes);

		return date;
	}

}
