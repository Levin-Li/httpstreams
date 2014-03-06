package com.thunisoft.mediax.stream;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.WritableByteChannel;



public interface IStreamer extends Closeable {

    /**
     * @return  -1 表示修改时间未知
     * @since V1.0 2014-3-6
     * @author chenxh
     */
    public abstract long lastModified();

    public abstract String contentType();

    public abstract long contentLength();

    public abstract void transfer(WritableByteChannel outChannel) throws IOException;
}
