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
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thunisoft.mediax.http.HttpRange;
import com.thunisoft.mediax.stream.IStreamer;
import com.thunisoft.mediax.stream.StreamerFactory;
import com.thunisoft.mediax.stream.proxy.HttpHeaders;

public class FileServerServlet extends HttpServlet {



    /**   */
    private static final long serialVersionUID = 1L;


    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        try {
            String range = req.getHeader(HttpHeaders.Names.RANGE);

            resp.reset();
            if (StringUtils.isEmpty(range)) {
                doStream(req, resp); // 普通的流化处理
            } else {
                doRange(req, resp); // 断点续传
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    /**
     * 流化处理
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     * @since V1.0 2014-3-7
     * @author chenxh
     */
    private void doStream(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String start = req.getParameter("start");
        double startAt = doubleValue(start);
        startAt = Math.max(0, startAt);

        // find streamer for the file
        IStreamer streamer = null;
        try {

            String url = localUrl(req);

            logger.debug("will stream {} start={}", url, start);
            streamer = StreamerFactory.getInstance().newStreamer(url, startAt);

            // last modified
            long modifiedSince =
                    req
                       .getDateHeader(com.thunisoft.mediax.stream.proxy.HttpHeaders.Names.IF_MODIFIED_SINCE);
            long lastModified =
                    streamer.lastModified() > 0 ? streamer.lastModified()
                            : System.currentTimeMillis();
            if (modifiedSince >= lastModified) {
                resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                return;
            }

            // http head
            resp.setContentType(streamer.contentType());
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.addDateHeader(HttpHeaders.Names.LAST_MODIFIED, lastModified);

            // content
            streamer.transfer(Channels.newChannel(resp.getOutputStream()));
            logger.info("stream[{}] over!", req.getRequestURL());
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(streamer);
        }
    }

    private String localUrl(HttpServletRequest req) {
        String contextPath = req.getContextPath();
        String uri = req.getRequestURI().substring(contextPath.length());
        String url;
        if (!uri.startsWith("/http")) {
            url = getServletContext().getRealPath(uri);
        } else {
            url = uri.substring(1);
        }
        return url;
    }

    private double doubleValue(String d) {
        try {
            if (StringUtils.isNotEmpty(d)) {
                return Double.parseDouble(d);
            }
        } catch (Exception e) {
            logger.debug(d + " is Not Double");
        }
        return -1;
    }

    private void doRange(HttpServletRequest req, HttpServletResponse resp) throws IOException {

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


}
