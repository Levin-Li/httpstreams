package com.thunisoft.mediax.http;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thunisoft.mediax.stream.IStreamer;
import com.thunisoft.mediax.stream.StreamerFactory;

public class FileServerServlet extends HttpServlet {
    private static final String HEADER_CONNECTION = "Connection";
    private static final String HEADER_CACHE_CONTROL = "Cache-Control";
    private static final String MAX_AGE = "max-age=86400";


    /**   */
    private static final long serialVersionUID = 1L;


    public static final java.lang.String HEADER_IFMODSINCE = "If-Modified-Since";
    public static final java.lang.String HEADER_LASTMOD = "Last-Modified";

    private Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        String uri = req.getRequestURI();
        String path = req.getContextPath();;

        String relativePath = uri.substring(path.length());
        String localFile = getServletContext().getRealPath(relativePath);
        logger.debug("will stream {}", localFile);

        // find streamer for the file
        IStreamer streamer = null;
        try {
            streamer = StreamerFactory.getInstance().newStreamer(localFile);
        } catch (FileNotFoundException e) {
            logger.warn(localFile + " Not Found", e);
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        } catch (IOException e) {
            logger.warn(localFile + " 读取失败，原因：" + e.getMessage(), e);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // last modified
        long modifiedSince = req.getDateHeader(HEADER_IFMODSINCE);
        if (modifiedSince >= streamer.lastModified().getTime()) {
            resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        }

        // http head
        resp.setContentType(streamer.contentType());
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.addDateHeader(HEADER_LASTMOD, streamer.lastModified().getTime());
        resp.addHeader(HEADER_CONNECTION, req.getHeader(HEADER_CONNECTION));
        resp.addHeader(HEADER_CACHE_CONTROL, MAX_AGE);

        // content
        String start = req.getParameter("start");
        if (isDouble(start)) {
            logger.info("stream {} start={}", relativePath, start);
            streamer.write(resp.getOutputStream(), Double.parseDouble(start));
        } else {
            streamer.write(resp.getOutputStream());
        }
        logger.info("stream[{}] over!", relativePath);
    }

    private boolean isDouble(String d) {
        try {
            if (StringUtils.isNotEmpty(d)) {
                Double.parseDouble(d);
                return true;
            }
        } catch (Exception e) {
            logger.debug(d + " is Not Double");
        }
        return false;
    }

}
