package com.thunisoft.mediax.core.vfs.http;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;

import com.thunisoft.mediax.core.HttpHeaders;
import com.thunisoft.mediax.core.vfs.FileObject;
import com.thunisoft.mediax.core.vfs.FileSystem;
import com.thunisoft.mediax.core.vfs.RandomAccessChannel;
import com.thunisoft.mediax.http.HttpRange;

public class HttpFileSystem implements FileSystem {
    final private static org.slf4j.Logger logger = LoggerFactory.getLogger(HttpFileSystem.class);

    final private HttpClient client;

    public HttpFileSystem(HttpClient client) {
        super();
        this.client = client;
    }

    @Override
    public FileObject getFile(String uri) throws FileNotFoundException {
        HttpMethod method = new HeadMethod(uri);
        try {
            int sc = client.executeMethod(method);

            FileObject file = null;
            if (sc == HttpServletResponse.SC_NOT_FOUND) {
                file = new HttpFileObject(uri, new HttpHeaders(method.getResponseHeaders()), false);
            } else if (sc != HttpServletResponse.SC_OK
                    && sc != HttpServletResponse.SC_PARTIAL_CONTENT) {
                throw new FileNotFoundException(uri + "response statue: " + sc);
            }

            HttpHeaders header = new HttpHeaders(method.getResponseHeaders());
            if (!header.acceptRanges()) {
                throw new IOException("server can't accept ranges");
            }
            
            file = new HttpFileObject(uri, new HttpHeaders(method.getResponseHeaders()), true);

            return file;
        } catch (IOException e) {
            throw new FileNotFoundException("error for call [" + uri + "]");
        } finally {
            method.releaseConnection();
        }
    }

    @Override
    public RandomAccessChannel openReadChannel(FileObject file) throws IOException {
        return new RandomAccessHttpChannelImpl((HttpFileObject)file, this);
    }

    public ByteBuffer getRangeData(HttpFileObject file, HttpRange range) throws IOException {
        String uri = file.toUri();
        HttpMethod method = new GetMethod(uri);

        ByteBuffer buffer = ByteBuffer.allocate((int) range.length());
        method.setRequestHeader(HttpHeaders.Names.RANGE, range.toRangeHeader());

        InputStream in = null;
        try {
            int sc = client.executeMethod(method);

            // 必须是基于 HTTP 断点续传
            if (sc != HttpServletResponse.SC_PARTIAL_CONTENT) {
                throw new IOException("server of " + uri
                        + " don't support interrupted data transfer");
            }

            // 结果检验
            HttpHeaders header = new HttpHeaders(method.getResponseHeaders());
            logger.debug("get {} by execute {}", header, uri);
            if (header.getContentLength() > buffer.remaining()) {
                throw new IOException("服务端返回的内容比请求的内容多!");
            }

            // 读取返回的内容
            in = method.getResponseBodyAsStream();
            ReadableByteChannel inChannel = Channels.newChannel(in);
            while (buffer.remaining() > 0) {
                int read = inChannel.read(buffer);
                if (read < 0) {
                    break;
                }
            }

            buffer.flip();
            buffer.limit((int)header.getContentLength());
            return buffer;
        } finally {
            IOUtils.closeQuietly(in);
            method.releaseConnection();
        }
    }
}
