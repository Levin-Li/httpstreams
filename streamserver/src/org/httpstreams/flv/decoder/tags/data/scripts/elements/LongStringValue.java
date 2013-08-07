package org.httpstreams.flv.decoder.tags.data.scripts.elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.httpstreams.flv.FlvSupports;
import org.httpstreams.flv.StructureInputStream;


public class LongStringValue extends AbstractValue {

	public LongStringValue() {
		super(FlvSupports.SCRIPT_12_LongString);
	}

	@Override
	protected Object readTypedValue(StructureInputStream inStream)
			throws IOException {
		long strLength = inStream.readUI32();

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		int tempLength = 0;
		byte[] temp = new byte[2048];
		while (strLength > 0
				&& (tempLength = inStream.read(temp, 0, (int) Math.min(
						strLength, temp.length))) > 0) {
			out.write(temp, 0, tempLength);
			strLength -= tempLength;
		}

		return out.toByteArray();
	}

}
