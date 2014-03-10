package com.thunisoft.mediax.core.pseudostreaming.mp4;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

import com.googlecode.mp4parser.DataSource;
import com.thunisoft.mediax.core.vfs.RandomAccessChannel;

public class LocalDataSource implements DataSource {
    private RandomAccessChannel rch;

    public LocalDataSource(RandomAccessChannel rch) {
        super();
        this.rch = rch;
    }


    @Override
    public void close() throws IOException {
        rch.close();
    }

    @Override
    public ByteBuffer map(long startPosition, long size) throws IOException {
        return rch.map(startPosition, size);
    }

    @Override
    public long position() throws IOException {
        return rch.position();
    }

    @Override
    public void position(long newPosition) throws IOException {
        rch.position(newPosition);
    }

    @Override
    public int read(ByteBuffer buffer) throws IOException {
        return rch.read(buffer);
    }

    @Override
    public long size() throws IOException {
        return rch.length();
    }

    public long transferTo(long startPosition, long count, WritableByteChannel sink) throws IOException {
        return rch.transferTo(startPosition, count, sink);
    }

}
