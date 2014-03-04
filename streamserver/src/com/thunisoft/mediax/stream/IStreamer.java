package com.thunisoft.mediax.stream;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;



public interface IStreamer {

    public abstract Timestamp lastModified();

    public abstract String contentType();

    public abstract long contentLength();

    public abstract void write(OutputStream out) throws IOException;

    public abstract void write(OutputStream out, double startAt) throws IOException;

}
