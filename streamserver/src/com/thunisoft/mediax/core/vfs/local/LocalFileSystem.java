package com.thunisoft.mediax.core.vfs.local;

import java.io.IOException;

import org.apache.commons.io.FilenameUtils;

import com.thunisoft.mediax.core.pseudostreaming.flv.FlvStreamer;
import com.thunisoft.mediax.core.pseudostreaming.mp4.MP4Streamer;
import com.thunisoft.mediax.core.vfs.FileObject;
import com.thunisoft.mediax.core.vfs.FileSystem;
import com.thunisoft.mediax.core.vfs.IStreamer;
import com.thunisoft.mediax.core.vfs.RandomAccessChannel;

public class LocalFileSystem implements FileSystem {

    @Override
    public FileObject getFile(String url) {
        try {
            return new LocalFile(url);
        } catch (Exception e) {
            throw new IllegalArgumentException("非法的 URL:" + url, e);
        }
    } 


    @Override
    public RandomAccessChannel openReadChannel(FileObject file) throws IOException {
        return new RandomAccessFileChannelImpl(((LocalFile) file).getFile());
    }

}
