package com.thunisoft.mediax.stream;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractFileStreamer implements IStreamer {

    private final File file;

    private double startAt;
    
    final protected Logger logger = LoggerFactory.getLogger(getClass());
    
    protected AbstractFileStreamer(File file, double startAt) throws FileNotFoundException {
        this.file = file;
        this.startAt = Math.max(0, startAt);

        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath());
        }
    }

    public long lastModified() {
        return file.lastModified();
    }

    @Override
    public long contentLength() {
        return file.length();
    }

    protected File file() {
        return file;
    }
    public double startAt() {
        return this.startAt;
    }
    
    @Override
    public void close() throws IOException {
        
    }
}
