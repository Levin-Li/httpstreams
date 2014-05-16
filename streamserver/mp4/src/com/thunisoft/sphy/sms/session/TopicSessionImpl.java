package com.thunisoft.sphy.sms.session;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.thunisoft.sphy.sms.IMessage;
import com.thunisoft.sphy.sms.IMessageConsumer;
import com.thunisoft.sphy.sms.IMessageProducer;
import com.thunisoft.sphy.sms.ITopicSession;

public class TopicSessionImpl implements ITopicSession {
    /**   */
    private static final long serialVersionUID = 1L;

    final private String name;
    
    private Map<String, Serializable> properties = new ConcurrentHashMap<String, Serializable>();

    private Map<IMessageConsumer, IMessageConsumer> consumers = new ConcurrentHashMap<IMessageConsumer, IMessageConsumer>();

    public TopicSessionImpl (String name) {
        if (null == name) {
            throw new IllegalArgumentException("topic name should't be null");
        }

        this.name = name;
    }
    
    public IMessageProducer createProducer(String producer) {
        
        return null;
    }

    public IMessageConsumer createConsumer(String consumer) {
        
        return null;
    }

    public void sendMessage(IMessageProducer producer, IMessage msg) {
        
    }

    public void close() {
        
        
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof TopicSessionImpl)) {
            return false;
        } else {
            TopicSessionImpl target = (TopicSessionImpl)obj;
            return name.equals(target.name);
        }
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public void setProperty(String name, Serializable value) {
        properties.put(name, value);
    }

    public Serializable getProperty(String name) {
        return properties.get(name);
    }
}
