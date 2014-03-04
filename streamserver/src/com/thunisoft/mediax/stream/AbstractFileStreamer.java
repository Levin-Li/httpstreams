package com.thunisoft.mediax.stream;



import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractFileStreamer implements IStreamer {

    private final File file;

    final protected Logger logger = LoggerFactory.getLogger(getClass());
    
    protected AbstractFileStreamer(File file) throws FileNotFoundException {
        this.file = file;

        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath());
        }
    }

    public Timestamp lastModified() {
        return new Timestamp(file.lastModified());
    }

    @Override
    public long contentLength() {
        return file.length();
    }

    protected File file() {
        return file;
    }
}
