package com.thunisoft.mediax.core.vfs.http;

import org.apache.commons.io.FilenameUtils;

import com.thunisoft.mediax.core.HttpHeaders;
import com.thunisoft.mediax.core.vfs.FileObject;

public class HttpFileObject implements FileObject {
    final private String uri;
    final private boolean exists;
    final private HttpHeaders headers;

    
    public HttpFileObject(String uri, HttpHeaders headers, boolean exists) {
        super();
        this.uri = uri;
        this.headers = headers;
        this.exists = exists;
    }

    @Override
    public long lastModified() {
        return headers.getContentLength();
    }

    @Override
    public String getName() {
        return FilenameUtils.getName(uri);
    }

    @Override
    public long length() {
        return headers.getContentLength();
    }

    @Override
    public String toUri() {
        return uri;
    }

    @Override
    public boolean exists() {
        return exists;
    }

    @Override
    public String toString() {
        return "HttpFileObject: " + toUri();
    }
}
