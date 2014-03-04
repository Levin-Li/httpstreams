package com.thunisoft.mediax.stream;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;

import com.thunisoft.mediax.stream.flv.FlvStreamer;
import com.thunisoft.mediax.stream.mp4.MP4Streamer;

public class StreamerFactory {
    public static StreamerFactory getInstance() {
        return new StreamerFactory();
    }

    public IStreamer newStreamer(String file) throws IOException {
        String suffix = FilenameUtils.getExtension(file);
        if ("mp4".equals(suffix)) {
            return new MP4Streamer(new File(file));
        } else if ("flv".equals(suffix)) {
            return new FlvStreamer(new File(file));
        } else {
            throw new IllegalArgumentException("unsupport " + file);
        }
    }
}
