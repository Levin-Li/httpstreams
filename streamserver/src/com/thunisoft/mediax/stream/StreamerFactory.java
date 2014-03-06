package com.thunisoft.mediax.stream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.input.AutoCloseInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thunisoft.mediax.stream.flv.FlvStreamer;
import com.thunisoft.mediax.stream.mp4.MP4Streamer;
import com.thunisoft.mediax.stream.proxy.HttpHeaders;
import com.thunisoft.mediax.stream.proxy.HttpProxyStreamer;

public class StreamerFactory {
    private static final Logger logger = LoggerFactory.getLogger(StreamerFactory.class);

    public StreamerFactory() {}

    public static StreamerFactory getInstance() {
        return new StreamerFactory();
    }

    public IStreamer newStreamer(String uri, double start) throws Exception {
        String suffix = FilenameUtils.getExtension(uri);

        if (uri.startsWith("http")) {
            return newHttpProxyStreamer(uri, start);
        } else if ("mp4".equals(suffix)) {
            return new MP4Streamer(new File(uri), start);
        } else if ("flv".equals(suffix)) {
            return new FlvStreamer(new File(uri), start);
        } else {
            throw new IllegalArgumentException("unsupport " + uri);
        }
    }

    private IStreamer newHttpProxyStreamer(String sUrl, double start) throws Exception {
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(sUrl);
        if (start > 0) {
            method.setQueryString("start=" + start);
        }
        int httpCode = client.executeMethod(method);

        if (httpCode != 200) {
            throw new IOException("status is not OK");
        }

        InputStream inStream = method.getResponseBodyAsStream();
        HttpHeaders headers = new HttpHeaders(method.getResponseHeaders());

        HttpProxyStreamer streamer =
                new HttpProxyStreamer(headers, new AutoCloseInputStream(inStream));

        logger.debug("proxy: {}", streamer);
        return streamer;
    }
}
