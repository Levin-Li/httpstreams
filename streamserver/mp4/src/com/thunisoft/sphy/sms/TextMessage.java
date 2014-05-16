package com.thunisoft.sphy.sms;

/**
 * 文本消息
 * 
 * @since V1.0  2014-5-14
 * @author chenxh
 */
public class TextMessage implements IMessage {
    private String content;
    
    public TextMessage (String content) {
        this.content = content;
    }
    
    public String getContent() {
        return content;
    }
}
