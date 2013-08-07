package org.httpstreams.flv.decoder.tags.data.scripts.elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.httpstreams.flv.FlvSupports;
import org.httpstreams.flv.StructureInputStream;


/**
 * 
 * @see #readTypedValue(StructureInputStream)
 * @author chenxiuheng@gmail.com
 *
 */
public abstract class AbstractValue implements Value {
	/**
	 * @see FlvSupports#SCRIPT_0_Number
	 */
	private int type;

	/**
	 * 实际的值
	 */
	protected Object value;

	/**
	 * @param type
	 * @param value
	 */
	public AbstractValue(int type) {
		super();
		this.type = type;
	}
	
	public Value read(StructureInputStream inStream) throws IOException {
		value = readTypedValue(inStream);
		return this;
	}

	/**
	 * 按类型读取数值
	 * 
	 * @param inStream
	 * @return
	 * @throws IOException 
	 */
	protected abstract Object readTypedValue (StructureInputStream inStream) throws IOException ;
	

	/* (non-Javadoc)
	 * @see org.httpstreams.flv.decoder.tags.data.scripts.elements.Value#getType()
	 */
	public final int getType() {
		return type;
	}

	public final Object getValue() {
		return value;
	}
	
	@Override
	public String toString() {
	    if (value instanceof String) {
	        return "'" + value + "'";
	    } else if (value instanceof Date) {
	        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	        return "'" + format.format((Date)value)+ "'";
	    } else {
	        return String.valueOf(value);
	    }
	}

}
