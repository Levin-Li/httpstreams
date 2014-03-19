package com.thunisoft.mediax.http.servlet;

import java.io.File;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.CacheManager;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thunisoft.mediax.core.HttpHeaders;
import com.thunisoft.mediax.core.utils.ByteUtils;
import com.thunisoft.mediax.core.vfs.FileObject;
import com.thunisoft.mediax.core.vfs.IStreamer;
import com.thunisoft.mediax.core.vfs.RandomAccessChannel;
import com.thunisoft.mediax.core.vfs.VFS;
import com.thunisoft.mediax.http.HttpRange;

public class FileServerServlet extends HttpServlet {



    /**   */
    private static final long serialVersionUID = 1L;


    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void init() throws ServletException {
        String ehcacheConfig = getServletContext().getRealPath("/WEB-INF/classes/ehcache.xml");
        
        CacheManager cacheManager;
        try {
            cacheManager = CacheManager.create(new File(ehcacheConfig).toURI().toURL());
            logger.warn("loaded: {}", ehcacheConfig);
        } catch (Exception e) {
            cacheManager = CacheManager.create();
            logger.warn("error to load: {}, and use default Cache Manager", ehcacheConfig);
        }
        
        VFS.set(cacheManager);
        VFS.setLocalRoot(getServletContext().getRealPath("/"));
    }
    
    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        setHttpResponse(req, resp);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        int status = setHttpResponse(req, resp);


        try {
            if (status == HttpServletResponse.SC_OK) {
                doStream(req, resp);
            } else if (status == HttpServletResponse.SC_PARTIAL_CONTENT) {
                doRange(req, resp);
            } else {
                // do nothing
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }


    /**
     * @param req
     * @param resp
     * @return response status
     * @since V1.0 2014-3-10
     * @author chenxh
     * @throws IOException
     */
    private int setHttpResponse(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.reset();

        // 支持断点续传
        resp.addHeader(HttpHeaders.Names.ACCEPT_RANGES, HttpHeaders.Values.BYTES);

        // 判断文件存在
        String localUrl = localUrl(req);
        FileObject file = VFS.getFile(localUrl);
        if (null == file || !file.exists()) {
            logger.warn("{} Not Found", localUrl);
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return HttpServletResponse.SC_NOT_FOUND;
        }

        // 确定文件是否修改
        long modifiedSince = req.getDateHeader(HttpHeaders.Names.IF_MODIFIED_SINCE);
        long lastModified =
                file.lastModified() > 0 ? file.lastModified() : System.currentTimeMillis();
        boolean isModified = (lastModified > modifiedSince);

        // status, contentType, contentLength
        int status;
        String contentType;
        long contentLength;
        String range = req.getHeader(HttpHeaders.Names.RANGE);
        if (!StringUtils.isEmpty(range)) {
            HttpRange httpRange = HttpRange.parse(range, file.length());
            status = HttpServletResponse.SC_PARTIAL_CONTENT;
            contentType = "multipart/byteranges";
            contentLength = ByteUtils.long2Int(httpRange.length());
            resp.addHeader(HttpHeaders.Names.CONTENT_RANGE, httpRange.toContentRangeHeader());
        } else if (isModified) {
            status = HttpServletResponse.SC_OK;
            contentType = getServletContext().getMimeType(file.getName());
            contentLength = file.length();
        } else {
            status = HttpServletResponse.SC_NOT_MODIFIED;
            contentType = getServletContext().getMimeType(file.getName());
            contentLength = file.length();
        }

        resp.setStatus(status);
        resp.setContentType(contentType);
        resp.setHeader(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(contentLength));
        return status;
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
            streamer = VFS.getStreamer(url);

            // content
            streamer.transfer(startAt, Channels.newChannel(resp.getOutputStream()));
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
        
        url = uri.substring(1);
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

    /**
     * 断点续传
     * 
     * @param req
     * @param resp
     * @throws IOException
     * @since V1.0 2014-3-10
     * @author chenxh
     */
    private void doRange(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        RandomAccessChannel inChannel = null;
        try {

            String localUrl = localUrl(req);
            inChannel = VFS.getRandomAccessChannel(localUrl);

            String rangeConfig = req.getHeader(HttpHeaders.Names.RANGE);
            HttpRange range = HttpRange.parse(rangeConfig, inChannel.length());

            WritableByteChannel outChannel = Channels.newChannel(resp.getOutputStream());

            long t1 = System.currentTimeMillis();
            inChannel.transferTo(range.startPosition(), range.length(), outChannel);

            long t2 = System.currentTimeMillis();

            if (t2 != t1) {
                logger.debug("transfer {}KB, speed is {}KB/s", range.length() / 1024,
                        (range.length() / 1024 * 1000) / (t2 - t1));
            }
        } finally {
            IOUtils.closeQuietly(inChannel);
        }
    }


}
