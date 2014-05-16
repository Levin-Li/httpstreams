package com.thunisoft.sphy.cache.startup;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ehcache.CacheManager;

import com.thunisoft.sphy.cache.SystemCache;

public class ServletContextInitiatedLisenter implements ServletContextListener {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private CacheManager cacheManager;
    
    public void contextDestroyed(ServletContextEvent arg0) {
        
    }

    public void contextInitialized(ServletContextEvent event) {
        String ehcacheConfig = event.getServletContext().getRealPath("/WEB-INF/classes/ehcache.xml");
        try {
            cacheManager = CacheManager.create(new File(ehcacheConfig).toURI().toURL());
            logger.warn("loaded: {}", ehcacheConfig);
        } catch (Exception e) {
            cacheManager = CacheManager.create();
            logger.warn("error to load: {}, and use default Cache Manager", ehcacheConfig);
        }

        SystemCache.set(cacheManager);
    }

}
