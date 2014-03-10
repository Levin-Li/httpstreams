package com.thunisoft.mediax.core.vfs;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface FileSystem {

    public FileObject getFile(String uri) throws FileNotFoundException;

    public RandomAccessChannel openReadChannel(FileObject file) throws IOException;
}
