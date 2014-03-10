package com.thunisoft.mediax.core.amf;

public class UnsupportedAMFTypeException extends RuntimeException {

    /**   */
    private static final long serialVersionUID = 1L;

    public UnsupportedAMFTypeException(int type) {
        super("unsupported type[" + type + "]");
    }
}
