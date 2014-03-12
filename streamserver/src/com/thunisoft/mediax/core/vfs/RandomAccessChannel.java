package com.thunisoft.mediax.core.vfs;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public interface RandomAccessChannel extends ReadableByteChannel {
    @Override
    public boolean isOpen();

    public long length() throws IOException;

    public long position() throws IOException;

    public void position(long newPosition) throws IOException;


    /* (non-Javadoc)
     * @see java.nio.channels.ReadableByteChannel#read(java.nio.ByteBuffer)
     */
    public int read(ByteBuffer dst) throws IOException;

    /**
     * 向   dst 中追加数据<br/>
     * 
     * <p>
     * 区别于 {@link FileChannel}，如果没有读到文件末尾，原则上尽可能多的向   <code>dst</code> 中插入数据。
     * 
     * 在 {@link FileChannel#read(ByteBuffer)}中，在没有写满ByteByffer的情况下，读取操作可能会提前终止。
     * @return 实际向    dst 中写入的字节个数， -1 表示已经到文件末尾。
     * @see java.nio.channels.ReadableByteChannel#read(java.nio.ByteBuffer)
     */
    public int readFull(ByteBuffer dst) throws IOException;

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
