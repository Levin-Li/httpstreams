package com.thunisoft.sphy.sms;

import java.io.Closeable;
import java.io.Serializable;


/**
 * 会话（Session）： 主题实例
 * 
 * <p>
 * session 主要用于管理及时消息，负责把发送者的消息转发给接收者。
 * 对于每个消息接收者，都需要维护一个向自己发送消息的 session。 
 * 
 * @since V1.0  2014-5-14
 * @author chenxh
 */
public interface ITopicSession extends Serializable, Closeable{
    public void close();
    
    public void setProperty(String name, Serializable value);
    
    public Serializable getProperty(String name);
    
    public IMessageProducer createProducer(String producer);
    
    public IMessageConsumer createConsumer(String consumer);
}
