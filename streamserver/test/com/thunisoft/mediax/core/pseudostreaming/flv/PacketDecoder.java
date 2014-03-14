package com.thunisoft.mediax.core.pseudostreaming.flv;

import java.nio.ByteBuffer;

import org.apache.commons.codec.DecoderException;

import com.thunisoft.mediax.core.codec.flv.tag.Tag;

public interface PacketDecoder {

    /** Mask for bit 0 of a byte. */
    public static final int BIT_0 = 1;

    /** Mask for bit 1 of a byte. */
    public static final int BIT_1 = 0x02;

    /** Mask for bit 2 of a byte. */
    public static final int BIT_2 = 0x04;

    /** Mask for bit 3 of a byte. */
    public static final int BIT_3 = 0x08;

    /** Mask for bit 4 of a byte. */
    public static final int BIT_4 = 0x10;

    /** Mask for bit 5 of a byte. */
    public static final int BIT_5 = 0x20;

    /** Mask for bit 6 of a byte. */
    public static final int BIT_6 = 0x40;

    /** Mask for bit 7 of a byte. */
    public static final int BIT_7 = 0x80;
    
    public Tag decode(ByteBuffer packetData) throws DecoderException;
}
