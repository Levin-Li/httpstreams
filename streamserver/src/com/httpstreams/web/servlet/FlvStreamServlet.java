package com.httpstreams.web.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.httpstreams.flv.decoder.Flv;
import org.httpstreams.flv.decoder.FlvDecoder;

public class FlvStreamServlet extends HttpServlet {
    private Log  logger = LogFactory.getLog(getClass());
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String app = req.getContextPath();
        String uri = req.getRequestURI();
        
        String name = uri.substring(app.length());
        File file = new File(getServletContext().getRealPath(name));

        logger.info(uri);
        
        String start = req.getParameter("start");

        resp.setContentType("video/x-flv");
        InputStream inStream = null;
        OutputStream outStream = null;
        try {
            outStream = resp.getOutputStream();
            
            if (StringUtils.isEmpty(start)) {
                inStream = new FileInputStream(file);
            } else {
                FlvDecoder decode = new FlvDecoder();
                Flv flv = decode.decode(file.getAbsolutePath());
                inStream = flv.seek(Double.parseDouble(start));
            }
            
            int length = 0;
            byte[] buffer = new byte[2048];
            while ((length = inStream.read(buffer)) != -1){
                outStream.write(buffer, 0, length);
                
                Thread.sleep(100);
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            IOUtils.closeQuietly(inStream);
        }
        
        super.doGet(req, resp);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
}
