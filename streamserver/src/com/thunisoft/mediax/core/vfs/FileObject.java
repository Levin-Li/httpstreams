package com.thunisoft.mediax.core.vfs;


public interface FileObject {
    public long lastModified();

    public String getName();

    public long length();

    public String toUri();

    public boolean exists();
}
