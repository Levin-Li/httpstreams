package com.thunisoft.mediax.core.vfs.local;

import java.io.File;
import java.io.IOException;

import com.thunisoft.mediax.core.vfs.FileObject;
import com.thunisoft.mediax.core.vfs.FileSystem;
import com.thunisoft.mediax.core.vfs.RandomAccessChannel;
import com.thunisoft.mediax.core.vfs.VFS;

public class LocalFileSystem implements FileSystem {

    @Override
    public FileObject getFile(String url) {
        try {
            return new LocalFile(new File(VFS.getFullPath(url)));
        } catch (Exception e) {
            throw new IllegalArgumentException("非法的 URL:" + url, e);
        }
    } 


    @Override
    public RandomAccessChannel openReadChannel(FileObject file) throws IOException {
        return new RandomAccessFileChannelImpl(((LocalFile) file).getFile());
    }

}
