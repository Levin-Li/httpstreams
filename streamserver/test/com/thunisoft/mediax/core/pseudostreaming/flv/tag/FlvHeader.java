package com.thunisoft.mediax.core.pseudostreaming.flv.tag;

import java.nio.ByteBuffer;


public class FlvHeader implements Tag {
    private int version;
    private boolean hasVideo;
    private boolean hasAudio;

    private ByteBuffer tag;
    
    public FlvHeader(int version, boolean hasVideo, boolean hasAudio, ByteBuffer tag) {
        super();
        this.version = version;
        this.hasVideo = hasVideo;
        this.hasAudio = hasAudio;
        this.tag = tag;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int size() {
        return tag.limit();
    }

    public boolean isHasVideo() {
        return hasVideo;
    }

    public void setHasVideo(boolean hasVideo) {
        this.hasVideo = hasVideo;
    }

    public boolean isHasAudio() {
        return hasAudio;
    }

    public void setHasAudio(boolean hasAudio) {
        this.hasAudio = hasAudio;
    }

    public ByteBuffer getTag() {
        return tag;
    }
    

}
