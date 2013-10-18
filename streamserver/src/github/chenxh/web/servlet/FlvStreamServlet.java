package github.chenxh.web.servlet;

import github.chenxh.media.UnsignedDataInput;
import github.chenxh.media.UnsignedDataOutput;
import github.chenxh.media.flv.FlvDecoder;
import github.chenxh.media.flv.FlvEncoder;
import github.chenxh.media.flv.FlvSignature;
import github.chenxh.media.flv.script.FlvMetaData;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

public class FlvStreamServlet extends HttpServlet {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected long getLastModified(HttpServletRequest req) {
        return System.currentTimeMillis();
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI().substring(appUriPrefix.length());
        File source = new File(rootPath, uri);
        logger.debug("download:{}", source.getAbsoluteFile());

        if (source.exists()) {
            resp.setContentType("video/x-flv");
            write(req, resp, source);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, uri);
        }
    }

    final FlvEncoder encoder = new FlvEncoder();
    final FlvDecoder decoder = new FlvDecoder();
    private void write(HttpServletRequest req, HttpServletResponse resp, File source) throws IOException {
        String start = req.getParameter("start");
        if (StringUtils.isEmpty(start)) {
            directOutput(resp, source);
        } else {

            UnsignedDataInput dataIn = null;
            UnsignedDataOutput dataOut = new UnsignedDataOutput(resp.getOutputStream());
            try {
                dataIn = new UnsignedDataInput(source);
                FlvMetaData metaData = decoder.decodeMetaData(dataIn);
                if (null == metaData) {
                    // 没有 metadata 数据
                    directOutput(resp, source);
                    return;
                } else {
                    logger.info("metadata:{}", metaData);
                }

                long position = metaData.getNearestPosition(Long.parseLong(start));
                logger.warn("实际位置:{}", position);
                if (position < 0) {
                    // 没有相应的关键帧
                    directOutput(resp, source);
                    return;
                } else {
                    // flv head 
                    encoder.encodeSignature(metaData.getSignature(), dataOut);
                    
                    // pre tag size
                    // metadata
                    dataOut.writeUI32(FlvSignature.MIN_HEAD_SIZE);
                    //int tagSize = encoder.encodeTag(metaData, dataOut);
                    
                    // pre tags ize
                    //dataOut.writeUI32(tagSize);
                    directOutput(resp, source, position);
                }

                dataIn.close();
            } finally {
                IOUtils.closeQuietly(dataIn);
                IOUtils.closeQuietly(dataOut);
            }
        }
        
    }
    
    /**
     * 文件直接输出
     * 
     * @param resp
     * @param source
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void directOutput(HttpServletResponse resp, File source) throws IOException {
        // 直接下载
        InputStream inStream = null;
        try {
            inStream = new BufferedInputStream(new FileInputStream(source));
            IOUtils.copyLarge(inStream, resp.getOutputStream());
            inStream.close();  // 使得能捕获 IO 异常
        } finally {
            IOUtils.closeQuietly(inStream);
        }
    }

    /**
     * 文件直接输出
     * 
     * @param resp
     * @param source
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void directOutput(HttpServletResponse resp, File source, long startAt) throws IOException {
        // 直接下载
        UnsignedDataInput inStream = null;
        try {
            inStream = new UnsignedDataInput(source);
            inStream.skipBytes(startAt);

            IOUtils.copyLarge(inStream, resp.getOutputStream());
            inStream.close();  // 使得能捕获 IO 异常
        } finally {
            IOUtils.closeQuietly(inStream);
        }
    }

    private String appUriPrefix;
    private String rootPath;
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
        String alias = config.getInitParameter("alias");
        appUriPrefix = config.getServletContext().getContextPath() + "/" + alias;
        
        String rawPath = config.getInitParameter("path");
        rootPath = config.getServletContext().getRealPath(rawPath);
        
        
        logger.warn("streamServer started, [alias={}, root={}]", alias, rootPath);
    }
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

}
