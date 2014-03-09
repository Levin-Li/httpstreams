package com.thunisoft.mediax.core.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

public interface RandomAccessReadableChannel extends ReadableByteChannel {
    @Override
    public boolean isOpen();

    public long position();

    public void position(long newPosition);

    @Override
    public int read(ByteBuffer dst) throws IOException;

    @Override
    public void close() throws IOException;
}
