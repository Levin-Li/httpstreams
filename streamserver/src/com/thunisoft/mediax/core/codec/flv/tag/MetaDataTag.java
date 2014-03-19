package com.thunisoft.mediax.core.codec.flv.tag;

import com.thunisoft.mediax.core.codec.amf.AMFArray;
import com.thunisoft.mediax.core.codec.flv.FlvMetaData;

public class MetaDataTag extends FlvMetaData implements Tag {

    public MetaDataTag(AMFArray metadata) {
        super(metadata);
    }
    
    @Override
    public boolean isKey() {
        return false;
    }

    @Override
    public long getTimestamp() {
        return 0;
    }
}
