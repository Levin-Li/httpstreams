package org.httpstreams.flv.decoder.tags.data.scripts.elements;

import java.io.IOException;

import org.httpstreams.flv.StructureInputStream;


public interface Value {
	public abstract Value read(StructureInputStream inStream) throws IOException;

    public abstract Object getValue();
}