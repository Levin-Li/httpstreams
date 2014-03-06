package com.thunisoft.mediax.stream.proxy;


import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import org.apache.commons.httpclient.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thunisoft.mediax.stream.IStreamer;

public class HttpProxyStreamer implements IStreamer{
    private static Logger logger = LoggerFactory.getLogger(HttpProxyStreamer.class);

    final private HttpHeaders headers;


    private InputStream in;
    public HttpProxyStreamer(HttpHeaders headers, InputStream inStream) {
        this.headers = headers;
        this.in = inStream;
    }

    private String getHeader(String name) {
        return headers.get(name);
    }

    @Override
    public long lastModified() {
        String header = getHeader(HttpHeaders.Names.LAST_MODIFIED);

        try {
            return DateUtil.parseDate(header).getTime();
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public String contentType() {
        return getHeader(HttpHeaders.Names.CONTENT_TYPE);
    }

    @Override
    public long contentLength() {
        String contentLength = getHeader(HttpHeaders.Names.CONTENT_LENGTH);
        try {
            return Long.parseLong(contentLength);
        } catch (Exception e) {
            return -1;
        }
    }

    

    @Override
    public void transfer(WritableByteChannel outChannel) throws IOException {
        ReadableByteChannel rch = Channels.newChannel(in);

        
        final ByteBuffer buffer = ByteBuffer.allocate(1024* 8);
        while (-1 != rch.read(buffer)) {
            buffer.flip();

            while (buffer.remaining() > 0) {
                outChannel.write(buffer);
            }

            buffer.rewind();
        }
    }

    @Override
    public void close() throws IOException {
        in.close();
    }
    
    @Override
    public String toString() {
        return "HttpProxyStream " + headers;
    }

}
