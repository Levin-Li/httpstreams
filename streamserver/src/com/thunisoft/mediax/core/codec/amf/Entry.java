package com.thunisoft.mediax.core.codec.amf;

public class Entry {
    private String name;
    private Object value;

    public Entry(String name, Object value) {
        super();
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return  "Entry {" + name + ":" + value + "}";
    }
}
