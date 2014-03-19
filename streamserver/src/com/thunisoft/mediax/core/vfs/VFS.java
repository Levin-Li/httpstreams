package com.thunisoft.mediax.core.vfs;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thunisoft.mediax.core.pseudostreaming.DefaultStreamer;
import com.thunisoft.mediax.core.pseudostreaming.flv.FlvStreamer;
import com.thunisoft.mediax.core.pseudostreaming.mp4.MP4Streamer;
import com.thunisoft.mediax.core.vfs.http.HttpFileSystem;
import com.thunisoft.mediax.core.vfs.local.LocalFileSystem;
import com.thunisoft.mediax.core.vfs.rtmp.RTMPSystem;

public class VFS {
    public static String P_FILE = "file";
    public static String P_HTTP = "http";
    public static String P_RTMP = "rtmp";

    private static Logger logger = LoggerFactory.getLogger(VFS.class);
    
    private static final Map<String, FileSystem> fileSystems = new HashMap<String, FileSystem>();

    static {
        fileSystems.put(P_FILE, new LocalFileSystem());
        fileSystems.put(P_HTTP, new HttpFileSystem(new HttpClient()));
        fileSystems.put(P_RTMP, new RTMPSystem());
    }

    private static String localRoot;
    
    public static String getFullPath(String relativePath) {
        File file = new File(localRoot, relativePath);
        
        logger.debug("use: {}", file.getAbsolutePath());
        return file.getAbsolutePath();
    }
    
    public static FileSystem getFileSystem(String protocol) {
        FileSystem system = fileSystems.get(protocol);

        if (null != system) {
            return system;
        } else {
            return fileSystems.get(P_FILE);
        }
    }

    private static String getProtocol(String url) {
        int uriIndex = url.indexOf("://");
        
        if (uriIndex > 0) {
            return url.substring(0, uriIndex);
        } else {
            return P_FILE;
        }
    }
    
    public static FileObject getFile(String localUrl) throws IOException {
        return getFileSystem(getProtocol(localUrl)).getFile(localUrl);
    }

    public static RandomAccessChannel getRandomAccessChannel(String localUrl) throws IOException {

        FileSystem system = getFileSystem(getProtocol(localUrl));
        FileObject file = system.getFile(localUrl);

        return system.openReadChannel(file);
    }

    public static IStreamer getStreamer(String localUrl) throws IOException {


        FileSystem system = getFileSystem(getProtocol(localUrl));
        FileObject file = system.getFile(localUrl);


        String suffix = FilenameUtils.getExtension(localUrl);
        if ("flv".equals(suffix)) {
            return new FlvStreamer(system.openReadChannel(file));
        } else if ("mp4".equals(suffix)) {
            return new MP4Streamer(system.openReadChannel(file));
        } else {
            return new DefaultStreamer(system.openReadChannel(file));
        }
    }

    
    
    private static CacheManager cacheManager;
    public static CacheManager getCacheManager() {
        if (null != cacheManager) {
            return cacheManager;
        } else {
            return cacheManager = CacheManager.create();
        }
    }
    
    public static void set(CacheManager manager) {
        cacheManager = manager;
    }
    
    public static Cache getCache(Class<?> type) {
        String cacheKey = type.getName();

        Cache cache = VFS.getCacheManager().getCache(cacheKey);

        if (null == cache) {
            VFS.getCacheManager().addCache(cacheKey);
            cache = VFS.getCacheManager().getCache(cacheKey);
        }

        return cache;
    }

    public static void setLocalRoot(String localRoot) {
        VFS.localRoot = localRoot;
    }
}
