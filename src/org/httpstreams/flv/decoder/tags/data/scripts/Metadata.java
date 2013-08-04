package org.httpstreams.flv.decoder.tags.data.scripts;

import org.httpstreams.flv.decoder.tags.data.scripts.elements.ECMAArrayValue;

public class Metadata {
    private ECMAArrayValue value;

    /**
     * @param value
     */
    public Metadata(ECMAArrayValue value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return null!= value ? value.toString():"null";
    }
}
