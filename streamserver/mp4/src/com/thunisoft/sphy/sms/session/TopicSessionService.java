package com.thunisoft.sphy.sms.session;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import com.thunisoft.sphy.cache.SystemCache;
import com.thunisoft.sphy.sms.ITopicSession;

public class TopicSessionService {

    private TopicSessionService() {}

    /**
     * 获取一个会话
     * 
     * 如果会话不存在，则自动创建一个
     * @param topic
     * @return
     * @since V1.0 2014-5-15
     * @author chenxh
     * @see #getTopicSession(String, boolean)
     */
    public ITopicSession getTopicSession(String topic) {
        return getTopicSession(topic, true);
    } 
    
    public ITopicSession getTopicSession(String topic, boolean createIfAbsent) {
        if (createIfAbsent) {
            putIfAbsent(topic);
        }
        
        Cache cache = getCache();
        Element sessionEl = cache.get(topic);
        
        return (ITopicSession) (null != sessionEl ? sessionEl.getObjectValue() : null);
    }
    
    public ITopicSession remove(String topic) {
        Cache cache = getCache();
        Element sessionEl = cache.removeAndReturnElement(topic);

        return (ITopicSession) (null != sessionEl ? sessionEl.getObjectValue() : null);
    }
    
    public ITopicSession putIfAbsent(String topic) {
        Cache cache = getCache();
        Element sessionEl = cache.get(topic);

        if (null == sessionEl) {
            ITopicSession session = new TopicSessionImpl(topic);
            cache.putIfAbsent(new Element(topic, session));

            // putIfAbsent 能保证里面有一个缓存， 但是不保证是当前线程加入的这个对象
            // 所以要重新从缓存中获取
            sessionEl = cache.get(topic);
        }

        if (null == sessionEl) {
            throw new IllegalStateException("Not Found Session Topic For [" + topic + "]");
        }

        return (ITopicSession) sessionEl.getObjectValue();
    }
    

    private Cache getCache() {
        return SystemCache.addCacheIfAbsent("sms");
    }


}
