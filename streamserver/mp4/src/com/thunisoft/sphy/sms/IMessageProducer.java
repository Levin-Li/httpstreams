package com.thunisoft.sphy.sms;

import java.io.Closeable;
import java.io.Serializable;

/**
 * 消息生产者。
 * 
 * <p>
 * 用于向会话中发布一条消息
 * @since V1.0  2014-5-14
 * @author chenxh
 */
public interface IMessageProducer extends Serializable, Closeable {
    public String getId();
    
    public String getName();

    /**
     * 向 session 的监听者发送一条消息
     * 
     * @param message
     * @since V1.0 2014-5-14
     * @author chenxh
     */
    public void sendMessage(IMessage message);
    
    /**
     * 表示该生产者失效了
     */
    public void close();
}
