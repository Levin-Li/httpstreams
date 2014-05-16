package com.thunisoft.sphy.sms;

/**
 * 事件
 * 
 * @since V1.0 2014-5-14
 * @author chenxh
 */
public class EventMessage implements IMessage {
    private String name;
    private Object[] params;

    public EventMessage(String name, Object[] params) {
        super();
        this.name = name;
        this.params = params;
    }

    public String getName() {
        return name;
    }

    public Object[] getParams() {
        return params;
    }


}
