package com.thunisoft.videox.mp4;

import java.sql.Timestamp;

/**
 * Media Header
 * <p>
 * @author chenxiuheng@gmail.com
 * @since V1.0
 * @Date 2014-3-30
 */
public class MediaHeaderBox extends FullBox {
    public final static String CONTAINER = "moov.trak.mdia";
    public final static String TYPE = "mdhd";

    private Timestamp created;
    private Timestamp lastModified;
    private long timeScale;
    private long duration;
    private long pad;
    private String language;
    private long reserved;

    public MediaHeaderBox (Box box) {
        super(box);
    }

    public Timestamp created() {
        return created;
    }

    public void created(Timestamp created) {
        this.created = created;
    }

    public Timestamp lastmModified() {
        return lastModified;
    }

    public void lastModified(Timestamp modified) {
        this.lastModified = modified;
    }

    public long timeScale() {
        return timeScale;
    }

    public void timeScale(long timeScale) {
        this.timeScale = timeScale;
    }


    /**
     * 
     * @return -1 if timeScale is unknown
     * @since V1.0 2014-3-31
     * @author chenxh
     */
    public double durationInSeconds() {
        if (timeScale > 0) {
            return duration * 1.0 / timeScale;
        } else {
            return -1;
        }
    }
    
    public long duration() {
        return duration;
    }

    public void duration(long duration) {
        this.duration = duration;
    }

    public long pad() {
        return pad;
    }

    public void pad(long pad) {
        this.pad = pad;
    }

    public String language() {
        return language;
    }

    public void language(String language) {
        this.language = language;
    }

    public long reserved() {
        return reserved;
    }

    public void reserved(long reserved) {
        this.reserved = reserved;
    }
    
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder(TYPE);
        
        b.append("{");
        b.append("seconds:").append(durationInSeconds()).append("s");
        b.append(", duration:").append(duration);
        b.append(", timescale:").append(timeScale);
        b.append("}");
        
        return b.toString();
    }

    
}
