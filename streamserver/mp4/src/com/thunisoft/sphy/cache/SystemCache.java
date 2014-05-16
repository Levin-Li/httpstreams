package com.thunisoft.sphy.cache;


import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;


/**
 * 系统缓存。
 * 
 * 缓存包括普通对象和大IO片段
 * 
 * @since V1.0 2014-4-16
 * @author chenxh
 */
public class SystemCache {

    private static CacheManager cacheManager;

    public static CacheManager getCacheManager() {
        if (null != cacheManager) {
            return cacheManager;
        } else {
            return cacheManager = CacheManager.create();
        }
    }

    public static void set(CacheManager manager) {
        cacheManager = manager;
    }

    /**
     * @param name
     * @return  null if no config
     * @since V1.0 2014-4-17
     * @author chenxh
     */
    public static Cache getCache(String name) {
        return getCacheManager().getCache(name);
    }
    
    public static Cache addCacheIfAbsent(Class<?> type) {
        String cacheKey = type.getName();

        return addCacheIfAbsent(cacheKey);
    }

    public static Cache addCacheIfAbsent(String cacheKey) {
        CacheManager cacheManager = getCacheManager();
        Cache cache = cacheManager.getCache(cacheKey);

        if (null == cache) {
            synchronized (cacheManager) {
                cacheManager.addCacheIfAbsent(cacheKey);
                cache = cacheManager.getCache(cacheKey);
            }
        }

        return cache;
    }
}
