package com.thunisoft.mediax.core.pseudostreaming.flv.tag;

import com.thunisoft.mediax.core.amf.AMFArray;
import com.thunisoft.mediax.core.pseudostreaming.flv.FlvMetaData;

public class MetaDataTag extends FlvMetaData implements Tag {

    public MetaDataTag(AMFArray metadata) {
        super(metadata);
    }

    
}
