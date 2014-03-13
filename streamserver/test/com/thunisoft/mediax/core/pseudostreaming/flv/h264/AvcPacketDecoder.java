package com.thunisoft.mediax.core.pseudostreaming.flv.h264;

import java.nio.ByteBuffer;

import org.apache.commons.codec.DecoderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thunisoft.mediax.core.pseudostreaming.flv.PacketDecoder;
import com.thunisoft.mediax.core.utils.ByteUtils;

public class AvcPacketDecoder implements PacketDecoder {
    private static final Logger logger = LoggerFactory.getLogger(AvcVideoTag.class);

    private static final AvcPacketDecoder globalInstance = new AvcPacketDecoder();
    
    public static AvcVideoTag decodeAvcVideoTag(ByteBuffer data) throws DecoderException {
        return globalInstance.decode(data);
    }

    public static boolean isAvcSequenceHeader(ByteBuffer packetData){
        int start = packetData.position();
        try {
            int packetType = ByteUtils.readUInt8(packetData);
            
            return AvcPacketType.isSequenceHeader(packetType);
        } finally {
            packetData.position(start);
        }
    }
    
    @Override
    public AvcVideoTag decode(ByteBuffer packetData) throws DecoderException {
        final int start = packetData.position();

        int type = ByteUtils.readUInt8(packetData);
        int compositionTime = ByteUtils.readUInt24(packetData);

        AvcVideoTag tag;
        if (AvcPacketType.isSequenceHeader(type)) {
            // 退回去，重新解析
            packetData.position(start);
            tag = decodeAvcConfig(packetData);
        } else {
            tag = new AvcVideoTag();
            tag.setPacketType(type);
            tag.setCompositionTime(compositionTime);
        }

        return tag;
    }
    
    public AvcConfigTag decodeAvcConfig(ByteBuffer data) throws DecoderException {
        final int start = data.position();

        int packetType = ByteUtils.readUInt8(data);
        int compositionTime = ByteUtils.readUInt24(data);
        
        if (!AvcPacketType.isSequenceHeader(packetType)) {
            throw new DecoderException("[" + packetType + "] is not Avc Sequence Header");
        }

        
        int version = ByteUtils.readUInt8(data);
        logger.info("configurationVersion: {}", version);
        
        int profileIndication = ByteUtils.readUInt8(data);
        logger.info("AVCProfileIndication:  {}", profileIndication);
        
        int profile_compatibility  = ByteUtils.readUInt8(data);
        logger.info("profile_compatibility:  {}", profile_compatibility);
        
        int levelIndication = ByteUtils.readUInt8(data);
        logger.info("AVCLevelIndication:  {}", levelIndication);
        
        int lengthSizeMinusOne = ByteUtils.readUInt8(data);
        logger.info("Nalu LengthSize:  {}", lengthSizeMinusOne&3+1);
        
        
        // SPS
        int numOfSequenceParameterSets  = 0x1F & ByteUtils.readUInt8(data);
        int sequenceParameterSetLength = ByteUtils.readUInt16(data);
        logger.info("numOfSequenceParameterSets(SPS): {}", numOfSequenceParameterSets);
        logger.info("sequenceParameterSetLength: {}", sequenceParameterSetLength);
        
        int allSPSUnits = numOfSequenceParameterSets * sequenceParameterSetLength;
        data.position(data.position() + allSPSUnits);
        logger.info("SPS content length: {}byte", allSPSUnits);

        // PPS
        int numOfPictureParameterSets = ByteUtils.readUInt8(data);
        int pictureParameterSetLength = ByteUtils.readUInt16(data);
        logger.info("numOfPictureParameterSets(PPS): {}", numOfPictureParameterSets);
        logger.info("pictureParameterSetLength: {}", pictureParameterSetLength);
        
        int allPPSUnits = numOfSequenceParameterSets * pictureParameterSetLength;
        logger.info("PPS content length: {}byte", allPPSUnits);
        data.position(data.position() + allPPSUnits);
        
        
        // new AvcSequenceHeaderTag instance
        // 后面还有 chromaFormat 等配置信息, 从packet的开头第一个字节就开始拷贝
        byte[] configRecord = new byte[(data.limit()-start)];
        data.position(start);
        data.get(configRecord);
        AvcConfigTag tag = new AvcConfigTag(configRecord, packetType, compositionTime);
        
        return tag;
    }

}
