package org.httpstreams.flv.decoder.tags.data;

import java.io.IOException;

import org.httpstreams.flv.StructureInputStream;

public class UnknownTypeData implements TagData {

    private int tagtype;
    
    public void read(StructureInputStream inStream, long packetSize)
            throws IOException {
        inStream.skip(packetSize);
    }


    public final void setTagtype(int type) {
        this.tagtype = type;
    }

    @Override
    public String toString() {
        return "unknown tag type [" + tagtype + "]";
    }
}
