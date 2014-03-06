package com.thunisoft.mediax.stream;

import java.io.IOException;
import java.nio.channels.WritableByteChannel;

public class StreamerWrapper implements IStreamer {
    private IStreamer streamer;

    public StreamerWrapper(IStreamer streamer) {
        this.streamer = streamer;
    }

    @Override
    public void close() throws IOException {
        streamer.close();
    }

    @Override
    public long lastModified() {
        return streamer.lastModified();
    }

    @Override
    public String contentType() {
        return streamer.contentType();
    }

    @Override
    public long contentLength() {
        return streamer.contentLength();
    }

    @Override
    public void transfer(WritableByteChannel outChannel) throws IOException {
        streamer.transfer(outChannel);
    }

}
