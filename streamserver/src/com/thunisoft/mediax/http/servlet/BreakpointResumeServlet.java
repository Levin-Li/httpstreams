package com.thunisoft.mediax.http.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thunisoft.mediax.http.HttpRange;
import com.thunisoft.mediax.stream.StreamerFactory;
import com.thunisoft.mediax.stream.proxy.HttpHeaders;

/**
 * 断点续传 Servlet
 * <p>
 * 
 * @author chenxiuheng@gmail.com
 * @since V1.0
 * @Date 2014-3-7
 */
public class BreakpointResumeServlet extends HttpServlet {

    /**   */
    private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.reset();

        String localUrl = localUrl(req);
        File file = StreamerFactory.getInstance().getFile(localUrl);
        if (null == file || !file.exists()) {
            logger.info("{} Not Found", localUrl);
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String rangeConfig = req.getHeader(HttpHeaders.Names.RANGE); 
        HttpRange range = HttpRange.newInstance(rangeConfig, file.length());

        resp.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
        resp.addDateHeader(HttpHeaders.Names.LAST_MODIFIED, file.lastModified());
        resp.addHeader(HttpHeaders.Names.CONTENT_TYPE, "multipart/byteranges");
        resp.addHeader(HttpHeaders.Names.ACCEPT_RANGES, HttpHeaders.Values.BYTES);
        resp.addHeader(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(range.length()));
        resp.addHeader(HttpHeaders.Names.CONTENT_RANGE, range.toContentRangeHeader());
        
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file);
            WritableByteChannel outChannel = Channels.newChannel(resp.getOutputStream());
            
            long time1 = System.currentTimeMillis();
            FileChannel fch = fin.getChannel();
            
            long time2 = System.currentTimeMillis();
            fch.transferTo(range.startPosition(), range.length(), outChannel);
            
            long time3 = System.currentTimeMillis();

            logger.debug("cost {}ms on open {}", time2 - time1, file.getAbsolutePath());
            if (time3 != time2) {
                logger.debug("transfer {}KB, speed is {}KB/s", range.length()/1024, (range.length() / 1024 * 1000)/(time3-time2));
            }
        } finally {
            IOUtils.closeQuietly(fin);
        }
    }

    private String localUrl(HttpServletRequest request) {
        String content = request.getContextPath();
        String uri = request.getRequestURI();
        String localUri = uri.substring(content.length());

        String localUrl = getServletContext().getRealPath(localUri);
        logger.debug("download {}", localUrl);

        return localUrl;

    }
}
