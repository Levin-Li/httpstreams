package com.thunisoft.mediax.core.vfs;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.WritableByteChannel;



public interface IStreamer extends Closeable {

    public abstract void transfer(double startAt, WritableByteChannel outChannel) throws IOException;
}
