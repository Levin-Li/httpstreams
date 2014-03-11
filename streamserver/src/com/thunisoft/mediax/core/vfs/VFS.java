package com.thunisoft.mediax.core.vfs;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import net.sf.ehcache.CacheManager;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.io.FilenameUtils;

import com.thunisoft.mediax.core.pseudostreaming.flv.FlvStreamer;
import com.thunisoft.mediax.core.pseudostreaming.mp4.MP4Streamer;
import com.thunisoft.mediax.core.vfs.http.HttpFileSystem;
import com.thunisoft.mediax.core.vfs.local.LocalFileSystem;

public class VFS {
    public static String P_FILE = "file";
    public static String P_HTTP = "http";


    private static final Map<String, FileSystem> fileSystems = new HashMap<String, FileSystem>();

    static {
        fileSystems.put(P_FILE, new LocalFileSystem());
        fileSystems.put(P_HTTP, new HttpFileSystem(new HttpClient()));
    }

    public static FileSystem getFileSystem(String protocol) {
        FileSystem system = fileSystems.get(protocol);

        if (null != system) {
            return system;
        } else {
            throw new UnSupportedFileSystemException(protocol);
        }
    }

    public static FileObject getFile(String localUrl) throws IOException {
        URL url = new URL(localUrl);

        return getFileSystem(url.getProtocol()).getFile(localUrl);
    }

    public static RandomAccessChannel getRandomAccessChannel(String localUrl) throws IOException {
        URL url = new URL(localUrl);

        FileSystem system = getFileSystem(url.getProtocol());
        FileObject file = system.getFile(localUrl);

        return system.openReadChannel(file);
    }

    public static IStreamer getStreamer(String localUrl) throws IOException {

        URL url = new URL(localUrl);

        FileSystem system = getFileSystem(url.getProtocol());
        FileObject file = system.getFile(localUrl);


        String suffix = FilenameUtils.getExtension(localUrl);
        if ("flv".equals(suffix)) {
            return new FlvStreamer(system.openReadChannel(file));
        } else if ("mp4".equals(suffix)) {
            return new MP4Streamer(system.openReadChannel(file));
        }

        throw new IOException("No Streamer support: " + file);
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
}
