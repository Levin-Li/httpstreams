package com.thunisoft.mediax.core.vfs.rtmp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import com.thunisoft.mediax.core.m3u8live.RTMPDump;
import com.thunisoft.mediax.core.vfs.FileObject;
import com.thunisoft.mediax.core.vfs.FileSystem;
import com.thunisoft.mediax.core.vfs.RandomAccessChannel;
import com.thunisoft.mediax.core.vfs.VFS;
import com.thunisoft.mediax.core.vfs.local.LocalFile;
import com.thunisoft.mediax.core.vfs.local.RandomAccessFileChannelImpl;

public class RTMPSystem implements FileSystem {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FileObject getFile(String uri) throws FileNotFoundException {
        String extension = FilenameUtils.getExtension(uri);
        if ("m3u8".equals(extension)) {
            return getM3U8File(uri);
        } else if ("ts".equals(extension)){
            String target = getFullPath(uri);
            return new LocalFile(new File(target));
        } else {
            String target = getFullPath(uri);
            return new LocalFile(new File(target));
        }
    }
    
    public FileObject getM3U8File(String uri) {
        String targetM3u8 = getFullPath(uri);
        File localFile = new File(targetM3u8);
        if (!localFile.getParentFile().exists()) {
            boolean success = localFile.getParentFile().mkdirs();
            if (!success) {
                throw new RuntimeException("fail to mkdirs [" + localFile.getParentFile().getAbsolutePath() + "]");
            }
        }
        
        try {
            Matcher matcher = URL_PATTERN.matcher(uri);
            if(!matcher.matches()) {
                throw new IllegalArgumentException("illegal url :" + uri);
            }

            int groupIndex = 1;
            String protocol =  matcher.group(groupIndex ++);
            String server = matcher.group(groupIndex ++);
            int port = Integer.parseInt(matcher.group(groupIndex ++));
            String app = matcher.group(groupIndex ++);
            String streamName = matcher.group(groupIndex ++);
            
            submitRTMPDump(server, port, app, streamName, targetM3u8);
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }
        LocalFile file = new LocalFile(localFile);
        logger.debug("return: {}", file);
        return file;
    }

    private String getFullPath(String uri) {
        String relativePath = StringUtils.replaceEach(uri, new String[]{"://", ":", "//"}, new String[]{"@", "@", "/"});
        
        return VFS.getFullPath(relativePath);
    }
    
    @Override
    public RandomAccessChannel openReadChannel(FileObject file) throws IOException {
        return new RandomAccessFileChannelImpl((LocalFile)file);
    }

    /**
     * 
     * @param uri  rtmp://172.16.6.1:1935/app/streamName
     * @return
     * @since V1.0 2014-3-19
     * @author chenxh
     * @throws IOException 
     */
    private RTMPDump submitRTMPDump(String server, int port, String app, String streamName, String targetM3u8) throws IOException {
        Cache cache = VFS.getCache(RTMPSystem.class);
        Element element = cache.get(targetM3u8);

        if (null == element) {
            synchronized (writeLock) {
                element = cache.get(targetM3u8);

                if (null == element || ((RTMPDump)element.getValue()).isClosed()) {
                    cache.remove(targetM3u8);

                    RTMPDump dump = new RTMPDump(server, port, app, streamName, targetM3u8);
                    thredPool.submit(dump);

                    // start
                    dump.waitForStart();

                    element = new Element(targetM3u8, dump);
                    cache.put(element);
                }
            }
        }

        return (RTMPDump) element.getValue();
    }
    
    private final Object writeLock = new Object();
    
    
    // 格式如: "rtmp://172.16.4.5:1935/teach_app/livestream.m3u8"
    // 或者： "rtmpr://172.16.4.5:1935/teach_app/livestream.m3u8"
    private static Pattern URL_PATTERN = Pattern.compile("^([^:]+)://([^:]+):([^/]*)/([^/]*)/([^\\./]+)\\.m3u8");
    
    public static void main(String[] args) {
        Matcher matcher = URL_PATTERN.matcher("rtmp://172.16.4.5:1935/teach_app/livestream.m3u8");
        
        if(!matcher.matches()){
            throw new IllegalArgumentException("不匹配");
        }
    }

    private ExecutorService thredPool = Executors.newCachedThreadPool();
}
