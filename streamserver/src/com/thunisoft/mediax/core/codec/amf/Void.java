package com.thunisoft.mediax.core.codec.amf;

public class Void {
    private Void(){}
    
    private static Void instance = new Void();
    
    public static Void getInstance() {
        return instance;
    }
}
