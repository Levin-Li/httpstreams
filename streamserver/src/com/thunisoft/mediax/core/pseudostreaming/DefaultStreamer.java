package com.thunisoft.mediax.core.pseudostreaming;

import java.io.IOException;
import java.nio.channels.WritableByteChannel;

import com.thunisoft.mediax.core.vfs.IStreamer;
import com.thunisoft.mediax.core.vfs.RandomAccessChannel;

public class DefaultStreamer implements IStreamer {
    private RandomAccessChannel ch;



    public DefaultStreamer(RandomAccessChannel ch) {
        super();
        this.ch = ch;
    }

    @Override
    public void close() throws IOException {
        ch.close();
    }

    @Override
    public void transfer(double startAt, WritableByteChannel outChannel) throws IOException {
        ch.transferTo(0, ch.length(), outChannel);
    }
}
