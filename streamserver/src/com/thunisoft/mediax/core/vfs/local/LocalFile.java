package com.thunisoft.mediax.core.vfs.local;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.thunisoft.mediax.core.vfs.FileObject;

public class LocalFile implements FileObject  {
    final private File file;

    public LocalFile(File file) {
        this.file = file;
    }
    
    public LocalFile(String uri) throws URISyntaxException, IOException {
        this.file = new File(new URI(uri).toURL().getFile());
    }
    
    @Override
    public long lastModified() {
        return file.lastModified();
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public long length() {
        return file.length();
    }

    @Override
    public String toUri() {
        return file.toURI().toString();
    }
    
    public File getFile() {
        return file;
    }
    
    @Override
    public boolean exists() {
        return file.exists();
    }
    
    
    @Override
    public String toString() {
        return "LocalFile: " + file.getAbsolutePath();
    }
}
