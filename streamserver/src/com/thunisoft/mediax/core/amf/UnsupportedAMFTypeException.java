package com.thunisoft.mediax.core.amf;

import java.nio.ByteBuffer;

import org.apache.commons.codec.DecoderException;

public class UnsupportedAMFTypeException extends DecoderException {

    /**   */
    private static final long serialVersionUID = 1L;

    public UnsupportedAMFTypeException(int type, ByteBuffer content) {
        super("unsupported type[" + type + "] at [" + content.position() + "]");
    }
}
