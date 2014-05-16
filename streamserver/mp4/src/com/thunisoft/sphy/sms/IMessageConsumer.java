package com.thunisoft.sphy.sms;

import java.io.Closeable;
import java.io.Serializable;

public interface IMessageConsumer extends Serializable, Closeable {
    public String getId();
    
    public String getName();
    
    /**
     * 
     * @return true 表示还有消息
     * @since V1.0 2014-5-14
     * @author chenxh
     */
    public boolean hasMoreMessage();
    
    /**
     * 接收一条消息
     * 
     * <p>
     *   长时间等待
     * @return null： 当   session 被关闭、或者发生其他错误
     * @since V1.0 2014-5-14
     * @author chenxh
     */
    public IMessage receive();
    
    /**
     * 
     * @param timeout 等待时间，单位: ms
     * @return null 当   session 被关闭、超时、或者发生其他错误
     * 
     * @since V1.0 2014-5-14
     * @author chenxh
     */
    public IMessage receive(long timeout);
    
    /**
     * 表示不再接收消息
     */
    public void close();
}
