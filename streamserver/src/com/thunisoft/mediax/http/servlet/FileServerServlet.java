package com.thunisoft.mediax.http.servlet;

import java.io.IOException;
import java.nio.channels.Channels;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            doService(req, resp);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    private void doService(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        double startAt = -1;
        String start = req.getParameter("start");
        if (isDouble(start)) {
            startAt = Double.parseDouble(start);
        }


        // find streamer for the file
        IStreamer streamer = null;
        try {

            String contextPath = req.getContextPath();
            String uri = req.getRequestURI().substring(contextPath.length());
            String url;
            if (!uri.startsWith("/http")) {
                url = getServletContext().getRealPath(uri);
            } else {
                url = uri.substring(1);
            }
            
            logger.debug("will stream {} start={}", url, start);
            streamer = StreamerFactory.getInstance().newStreamer(url, startAt);

            // last modified
            long modifiedSince = req.getDateHeader(com.thunisoft.mediax.stream.proxy.HttpHeaders.Names.IF_MODIFIED_SINCE);
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
        } catch(IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(streamer);
        }
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
