package com.thunisoft.mediax.core.pseudostreaming.flv.tag;

public class AudioTag extends AbstractFrameTag implements Tag {

    public AudioTag(){
        super(Tag.AUDIO);
    }
    
    public AudioTag(int type, long dataSize, long timestamp, long streamId) {
        super(type, dataSize, timestamp, streamId);
    }

}
