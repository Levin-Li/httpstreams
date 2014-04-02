package com.thunisoft.videox.mp4;

import java.sql.Timestamp;


public class TrakHeaderBox extends FullBox {

    final public static String CONTAINER = "moov.trak";
    final public static String TYPE = "tkhd";
    
    private Timestamp created;
    private Timestamp lastModified;
    private long trackId;
    private long duration;
    
    private long alternateGroup;
    private long width;
    private long height;
    
    
    public TrakHeaderBox(Box copy) {
        super(copy);
    }


    public Timestamp created() {
        return created;
    }


    public void created(Timestamp created) {
        this.created = created;
    }


    public Timestamp lastModified() {
        return lastModified;
    }


    public void lastModified(Timestamp lastModified) {
        this.lastModified = lastModified;
    }


    public long trackId() {
        return trackId;
    }


    public void trackId(long trackId) {
        this.trackId = trackId;
    }


    public long duration() {
        return duration;
    }


    public void duration(long duration) {
        this.duration = duration;
    }


    public long alternateGroup() {
        return alternateGroup;
    }


    public void alternateGroup(long alternateGroup) {
        this.alternateGroup = alternateGroup;
    }


    public long width() {
        return width;
    }


    public void width(long width) {
        this.width = width;
    }


    public long height() {
        return height;
    }


    public void height(long height) {
        this.height = height;
    }

    @Override
    public String toString() {
        StringBuffer b = new StringBuffer();
        
        b.append(TYPE);
        b.append("{");
        b.append("id:").append(trackId);
        b.append(", Height:").append(height);
        b.append(", Width:").append(width);
        b.append(", created:").append(created);
        b.append(", lastModified:").append(lastModified);
        b.append(", group:").append(alternateGroup);
        b.append(", duration:").append(duration);
        
        b.append("}");
        
        return b.toString();
    }
}
