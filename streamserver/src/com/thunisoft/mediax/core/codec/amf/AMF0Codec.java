package com.thunisoft.mediax.core.codec.amf;

import java.nio.ByteBuffer;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;

public class AMF0Codec {
    private static final AMF0Decoder DECODER = new AMF0Decoder();
    private static final AMF0Encoder ENCODER = new AMF0Encoder();
    
    public static ByteBuffer encode(Object[] values) throws EncoderException {
        return ENCODER.encode(values);
    }
    
    public static Object[] decode(ByteBuffer object) throws DecoderException {
        return DECODER.decode(object);
    }
}
