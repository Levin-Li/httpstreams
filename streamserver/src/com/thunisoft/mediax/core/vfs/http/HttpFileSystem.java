package com.thunisoft.mediax.core.vfs.http;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import com.thunisoft.mediax.core.HttpHeaders;
import com.thunisoft.mediax.core.vfs.FileObject;
import com.thunisoft.mediax.core.vfs.FileSystem;
import com.thunisoft.mediax.core.vfs.RandomAccessChannel;
import com.thunisoft.mediax.core.vfs.VFS;
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
            } else {
                HttpHeaders header = new HttpHeaders(method.getResponseHeaders());
                if (!header.acceptRanges()) {
                    throw new IOException("server can't accept ranges");
                }

                file = new HttpFileObject(uri, new HttpHeaders(method.getResponseHeaders()), true);
            }

            return file;
        } catch (IOException e) {
            throw new FileNotFoundException("error for call [" + uri + "]");
        } finally {
            method.releaseConnection();
        }
    }

    @Override
    public RandomAccessChannel openReadChannel(FileObject file) throws IOException {
        return new RandomAccessHttpChannelImpl((HttpFileObject) file, this);
    }

    public ByteBuffer getRangeData(HttpFileObject file, HttpRange range) throws IOException {
        String uri = file.toUri();

        Cache cache = getCache();
        Key key = buildKey(file, range);
        Element valueEl = cache.get(key);


        byte[] value = null;
        if (null != valueEl) {
            value = (byte[]) valueEl.getValue();
            logger.debug("cached: {}", key);
        } else {
            value = getRangeDataFromWeb(range, uri);
            cache.put(new Element(key, value));
        }

        return ByteBuffer.wrap(value).asReadOnlyBuffer();
    }

    private byte[] getRangeDataFromWeb(HttpRange range, String uri) throws IOException,
            HttpException {
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
            return method.getResponseBody();
        } finally {
            IOUtils.closeQuietly(in);
            method.releaseConnection();
        }
    }

    private Cache getCache() {
        final String cacheKey = "HttpFileSystemCache";
        Cache cache = VFS.getCacheManager().getCache(cacheKey);

        if (null == cache) {
            VFS.getCacheManager().addCache(cacheKey);
            cache = VFS.getCacheManager().getCache(cacheKey);
        }

        return cache;
    }

    private Key buildKey(HttpFileObject file, HttpRange range) {
        return new Key(file.toUri(), file.lastModified(), range.startPosition(), range.length());
    }

    private static class Key implements Serializable {
        /**   */
        private static final long serialVersionUID = 1L;
        private String uri;
        private long lastModified;
        private long position;
        private long length;



        public Key(String uri, long lastModified, long position, long length) {
            super();
            this.uri = uri;
            this.position = position;
            this.length = length;
            this.lastModified = lastModified;
        }

        @Override
        public int hashCode() {
            return 31 * uri.hashCode() + ((int) (lastModified ^ (lastModified >>> 32)));
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (!(obj instanceof Key)) {
                return false;
            }

            Key other = (Key) obj;
            return StringUtils.equals(uri, other.uri) && (lastModified == other.lastModified)
                    && (position == other.position) && (length == other.length);
        }

        @Override
        public String toString() {
            return "{uri:" + uri + ", lastModified:" + lastModified + ", position:" + position + "}";
        }
    }
}
