package com.thunisoft.mediax.core.pseudostreaming.flv.aac;

import java.nio.ByteBuffer;

import org.apache.commons.codec.DecoderException;

import com.thunisoft.mediax.core.pseudostreaming.flv.PacketDecoder;
import com.thunisoft.mediax.core.utils.ByteUtils;

public class AacPacketDecoder implements PacketDecoder {
    private static final AacPacketDecoder globalInstance = new AacPacketDecoder();

    public static AacAudioTag decodeAvcVideoTag(ByteBuffer data) throws DecoderException {
        return globalInstance.decode(data);
    }

    public static boolean isAacSequenceHeader(ByteBuffer packetData){
        int start = packetData.position();
        try {
            int packetType = ByteUtils.readUInt8(packetData);
            
            return AACPacketType.isSequenceHeader(packetType);
        } finally {
            packetData.position(start);
        }
    }
    
    @Override
    public AacAudioTag decode(ByteBuffer packetData) throws DecoderException {
        final int start = packetData.position();
        
        int packetType = ByteUtils.readUInt8(packetData);
        if (!AACPacketType.isSequenceHeader(packetType)) { // AAC raw
            return new AacAudioTag(packetType);
        } else { // AAC sequence header
            packetData.position(start);
            return decodeAacConfig(packetData);
        }
    }
    
    
    public AacConfigTag decodeAacConfig(ByteBuffer packetData) throws DecoderException {
        final int start = packetData.position();

        int packetType = ByteUtils.readUInt8(packetData);
        if (!AACPacketType.isSequenceHeader(packetType)) {
            throw new DecoderException("[" + packetType + "] is not aac Sequence Header");
        }

        // 5 bit: audioObjectType
        // 4 bit: samplingFrequencyIndex
        // 4 bit: channelConfiguration
        // 3 bit: GASpecificConfig
        int config = ByteUtils.readUInt16(packetData);
        

        int audioObjectType = (config >> 11) & 31; // 31 = (bit)11111
        int frequencyIndex = (config >> 7) & 15;   // 15 = (bit)1111
        int channel = (config >> 3) & 15;          // 15 = (bit)1111

        byte[] data = new byte[packetData.limit()-start];
        packetData.position(start);
        packetData.get(data);
        AacConfigTag tag = new AacConfigTag(packetType, audioObjectType, frequencyIndex, channel, data);
        return tag;
    }
    
}
