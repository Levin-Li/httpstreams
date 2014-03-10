package com.thunisoft.mediax.core.pseudostreaming;



import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thunisoft.mediax.core.vfs.IStreamer;
import com.thunisoft.mediax.core.vfs.RandomAccessChannel;


public abstract class AbstractStreamer implements IStreamer {

    protected final RandomAccessChannel channel;

    final protected Logger logger = LoggerFactory.getLogger(getClass());
    
    protected AbstractStreamer(RandomAccessChannel channel) throws FileNotFoundException {
        this.channel = channel;
    }
    
    @Override
    public void close() throws IOException {
        if (channel.isOpen()) {
            channel.close();
        }
    }
}
