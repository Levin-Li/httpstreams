package com.thunisoft.mediax.core.m3u8live;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.Semaphore;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;

public class RTMPDump implements Runnable, Closeable, Serializable {
    /**   */
    private static final long serialVersionUID = 1L;

    private Logger logger = LoggerFactory.getLogger(RTMPDump.class);

    private String url;

    private M3U8Container m3u8Container;

    private int nextSegIndex;
    private IMediaReader reader;
    private IMediaWriter writer;

    private Semaphore lock;
    
    public RTMPDump(String server, int port, String app, String streamName, String targetM3u8) throws IOException {
        this.url = makeRTMPUrl(server, port, app, streamName);

        reader = ToolFactory.makeReader(url);

        if (logger.isInfoEnabled()) {
            reader.addListener(ToolFactory.makeDebugListener());
        }
        
        lock = new Semaphore(0);
        m3u8Container = new M3U8Container(streamName, targetM3u8);
        m3u8Container.setup();
    }

    public static String makeRTMPUrl(String server, int port, String app, String streamName) {
        StringBuilder rtmp = new StringBuilder();
        rtmp.append("rtmp://" + server + ":" + port + "/" + app + "/");
        rtmp.append(" app=" + app + "/");
        rtmp.append(" playpath=" + streamName);
        // rtmp.append( " swfUrl=http://127.0.0.1/video/mediaplayer/player.swf");
        // rtmp.append( " pageUrl=http://127.0.0.1/video/mediaplayer/player.swf");
        // rtmp.append( " flashver=`LNX 11,2,202,262`");
        rtmp.append(" live=true");
        rtmp.append("  buffer=1");

        String rtmpSourceUrl = rtmp.toString();

        String src = rtmpSourceUrl.toString();
        return src;
    }

    @Override
    public void run() {
        // start
        lock.release(Integer.MAX_VALUE);

        try {
            repeatDump();
        } finally {
            close();

            if (Thread.interrupted()) {
                logger.info("interrupted {}", this);
            }
        }
    }

    public void waitForStart() {
        if (lock.availablePermits() > 0) {
            logger.warn("{} 已经启动!", this);
            return;
        }

        try {
            lock.acquire();
        } catch (InterruptedException e) {
            logger.warn(this + "中断了", e);
        }
    }
    
    private void repeatDump() {
        createNewWriter();

        long preEndTime = System.currentTimeMillis();
        do {
            long now = System.currentTimeMillis();

            if (now - preEndTime > 5100) {
                preEndTime = now;

                // 关闭旧的
                removeLastWriter();

                // 打开新的
                createNewWriter();
            }

        } while (reader.readPacket() == null && !Thread.interrupted());
    }

    private void createNewWriter() {
        int segIndex = nextSegIndex++;
        String m3u8 = m3u8Container.getM3u8File();
        String m3u8FileName = FilenameUtils.getBaseName(m3u8);

        // 目录机构:
        //   stream1.m3u8
        //   stream1
        //     --seg_1.ts
        //     --seg_2.ts
        File tsDir = new File(FilenameUtils.getFullPath(m3u8), m3u8FileName);
        if (!tsDir.exists()) {
            boolean success = tsDir.mkdirs();
            if (!success) {
                throw new RuntimeException("error to create dir: " + tsDir.getAbsolutePath());
            }
        }
        String tsName = "seg_" + segIndex + ".ts";
        File tsPath = new File(tsDir, tsName);

        
        // ts uri 是相对于 m3u8 文件的地址
        String tsUri = m3u8FileName + "/" + tsName;

        writer = ToolFactory.makeWriter(tsPath.getAbsolutePath(), reader);
        writer.addListener(new SegmentAppender(m3u8Container, tsUri));
        reader.addListener(writer);

        logger.info("created: {}", tsPath);
    }

    private void removeLastWriter() {
        try {
            if (writer != null) {
                reader.removeListener(writer);
                
                if (writer.isOpen()) {
                    writer.close();
                } else {
                    logger.warn("{} is closed", writer);
                }
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        close();
    }

    @Override
    public void close() {
        removeLastWriter();

        reader.close();
    }
    
    public boolean isClosed(){
        return !reader.isOpen();
    }
}
