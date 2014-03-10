package com.thunisoft.mediax.core.vfs.local;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.channels.FileChannel.MapMode;

import com.thunisoft.mediax.core.vfs.RandomAccessChannel;

public class RandomAccessFileChannelImpl implements RandomAccessChannel {
    private File file;

    private FileChannel fch;

    public RandomAccessFileChannelImpl(LocalFile file) throws IOException {
        this(file.getFile());
    }
    
    public RandomAccessFileChannelImpl(File file) throws IOException {
        this.file = file;
        this.fch = new FileInputStream(file).getChannel();
    }
    
    @Override
    public boolean isOpen() {
        return null != fch && fch.isOpen();
    }

    @Override
    public long position() throws IOException {
        return fch.position();
    }

    @Override
    public void position(long newPosition) throws IOException {
        fch.position(newPosition);
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        return fch.read(dst);
    }

    @Override
    public ByteBuffer map(long startPosition, long size) throws IOException {
        return fch.map(MapMode.READ_ONLY, startPosition, size);
    }

    @Override
    public void close() throws IOException {
        if (null != fch) {
            fch.close();
        }
    }

    @Override
    public long length() throws IOException {
        return fch.size();
    }
    
    @Override
    public long transferTo(long startPosition, long count, WritableByteChannel sink)
            throws IOException {
        return fch.transferTo(startPosition, count, sink);
    }

    @Override
    public String toString() {
        return "RandomAccessReadChannel (" + file + ")";
    }
}
