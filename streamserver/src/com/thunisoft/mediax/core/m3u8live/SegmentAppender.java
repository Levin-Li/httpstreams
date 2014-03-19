package com.thunisoft.mediax.core.m3u8live;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.event.ICloseEvent;
import com.xuggle.mediatool.event.IWriteHeaderEvent;
import com.xuggle.mediatool.event.IWriteTrailerEvent;

public class SegmentAppender extends MediaToolAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SegmentAppender.class);

    private M3U8Container container;
    private String uri;
    
    public SegmentAppender(M3U8Container container, String uri) {
        super();
        this.container = container;
        this.uri = uri;
    }

    private long started;
    private long stoped; 

    private boolean appended = false;
    @Override
    public void onWriteHeader(IWriteHeaderEvent event) {
        started = System.currentTimeMillis();
    }

    @Override
    public void onWriteTrailer(IWriteTrailerEvent event) {
        stoped = System.currentTimeMillis();
        
        appendSegment();
    }

    @Override
    public void onClose(ICloseEvent event) {
        // stoped = System.currentTimeMillis();
        
        // appendSegment();
        LOGGER.info("closed!");
    }
    
    
    private void appendSegment() {
        if (!appended) {
            long duration =  (stoped - started);
            try {
                container.append(uri, duration);
                LOGGER.warn("append {} to {}", uri, container);
            } catch (IOException e) {
                LOGGER.warn("error to append " + uri + " for " + e.getMessage(), e);
            }
        }

        appended = true;
    }
}
