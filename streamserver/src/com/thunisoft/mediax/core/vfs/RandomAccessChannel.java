package com.thunisoft.mediax.core.vfs;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public interface RandomAccessChannel extends ReadableByteChannel {
    @Override
    public boolean isOpen();

    public long length() throws IOException;

    public long position() throws IOException;

    public void position(long newPosition) throws IOException;

    @Override
    public int read(ByteBuffer dst) throws IOException;

    
    /**
     * 从指定位置上读取数据，但是不改变 this channel 的 position 属性
     * @param startPosition
     * @param size
     * @return
     * @throws IOException
     * @since V1.0 2014-3-10
     * @author chenxh
     */
    public ByteBuffer map(long startPosition, long size) throws IOException;
    
    public long transferTo(long startPosition, long count, WritableByteChannel sink)
            throws IOException;


    @Override
    public void close() throws IOException;
}
