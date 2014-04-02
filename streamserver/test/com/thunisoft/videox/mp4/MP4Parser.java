package com.thunisoft.videox.mp4;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thunisoft.videox.mp4.sampletable.ChunkOffsetBox;
import com.thunisoft.videox.mp4.sampletable.CompositionOffsetBox;
import com.thunisoft.videox.mp4.sampletable.SampleChunkBox;
import com.thunisoft.videox.mp4.sampletable.SampleDescriptionBox;
import com.thunisoft.videox.mp4.sampletable.SampleDescriptionBox.SampleDescription;
import com.thunisoft.videox.mp4.sampletable.SampleSizeBox;
import com.thunisoft.videox.mp4.sampletable.SyncSampleBox;
import com.thunisoft.videox.mp4.sampletable.TimeSampleBox;

public class MP4Parser {
    public static final int BOX_HEAD_SIZE = 8;

    public static final long VERSION_0 = 0;
    public static final long VERSION_1 = 1;

    private FileChannel rch;

    public MP4Parser(String name) throws IOException {
        rch = new FileInputStream(name).getChannel();
    }

    public Box nextBoxInfo() throws IOException {
        // 到达文件末尾了
        if (rch.position() + BOX_HEAD_SIZE > rch.size()) {
            return null;
        }

        long start = rch.position();
        long size = readUInt32();
        String name = readBoxType();
        if (!StringUtils.isAlpha(name)) {
            rch.position(start);
            return null;
        }


        if (size == 1) {
            size = readUInt64();
        }

        Box box = new Box(size, name);
        box.position(start);
        box.headSize(rch.position() - start);

        return box;
    }

    public Box findBox(String code, Box parentBox) throws IOException {
        Box[] boxes = findBoxes(code, parentBox);

        return boxes.length == 0 ? null : boxes[0];
    }



    public Box findBox(String code) throws IOException {
        Box[] boxes = findBoxes(code);

        return boxes.length == 0 ? null : boxes[0];
    }

    public Box[] findBoxes(String code, Box parentBox) throws IOException {
        String[] codec = code.split("\\.");

        long fromPosition = parentBox.position() + parentBox.headSize();
        long endPosition = parentBox.position() + parentBox.size();
        List<Box> findBoxes = findBoxes(codec, 0, fromPosition, endPosition);

        return findBoxes.toArray(new Box[findBoxes.size()]);
    }


    public Box[] findBoxes(String code) throws IOException {
        String[] codec = code.split("\\.");

        rch.position(0);
        List<Box> findBoxes = findBoxes(codec, 0);

        return findBoxes.toArray(new Box[findBoxes.size()]);
    }

    private List<Box> findBoxes(String[] types, int typeIndex) throws IOException {
        return findBoxes(types, typeIndex, 0, rch.size());
    }

    private List<Box> findBoxes(String[] types, int typeIndex, long fromPosition, long endPosition)
            throws IOException {
        rch.position(fromPosition);

        // 分析 当前的 type
        int targetIndex = -1;
        String targetType = types[typeIndex];

        int leftIndexOfBracket = targetType.lastIndexOf('[');
        int rightIndexOfBracket = targetType.lastIndexOf(']');

        if (-1 != leftIndexOfBracket && -1 != rightIndexOfBracket
                && (leftIndexOfBracket != rightIndexOfBracket)) {
            targetIndex =
                    Integer.valueOf(targetType.substring(leftIndexOfBracket + 1,
                            rightIndexOfBracket));
            targetType = targetType.substring(0, leftIndexOfBracket);
        }

        // 找到需要找的 box
        List<Box> boxes = new ArrayList<Box>();
        Box box = null;
        while (rch.position() < endPosition && (box = nextBoxInfo()) != null) {
            if (targetType.equals(box.type())) {
                boxes.add(box);
            }

            skipBox(box);
        }

        if (targetIndex >= 0) {
            boxes = boxes.subList(targetIndex, targetIndex + 1);
        }

        // 最底层
        if (typeIndex == types.length - 1) {
            return new ArrayList<Box>(boxes);
        }
        // 还需下钻
        else {
            List<Box> selected = new LinkedList<Box>();
            for (Box parent : boxes) {
                final long startAt;
                startAt = parent.position() + parent.headSize();

                // 遍历下级
                selected.addAll(findBoxes(types, typeIndex + 1, startAt,
                        parent.position() + parent.size()));
            }

            return selected;
        }
    }

    public void skipBox(Box box) throws IOException {
        long boxEnd = box.position() + box.size();

        rch.position(Math.min(boxEnd, rch.size()));
    }

    private void skip(long length) throws IOException {
        long target = rch.position() + length;
        rch.position(Math.min(target, rch.size()));
    }

    private String readBoxType() throws IOException {
        return readString(4);
    }

    private String readString(int chars) throws IOException {
        ByteBuffer b = readBytes(chars);

        return new String(b.array(), "UTF-8");
    }

    ByteBuffer _byte64 = ByteBuffer.allocate(8);
 

    private long readUInt8() throws IOException {
        _byte64.clear();
        _byte64.putInt(0);
        _byte64.putShort((short)0);
        _byte64.put((byte)0);

        rch.read(_byte64);
        _byte64.flip();

        return _byte64.getLong();
    }

    private long readUInt16() throws IOException {
        _byte64.clear();
        _byte64.putInt(0);
        _byte64.putShort((short)0);

        rch.read(_byte64);
        _byte64.flip();

        return _byte64.getLong();
    }

    private long readUInt24() throws IOException {
        _byte64.clear();
        _byte64.putInt(0);
        _byte64.put((byte)0);

        rch.read(_byte64);
        _byte64.flip();

        return _byte64.getLong();
    }
    
    private long readUInt32() throws IOException {
        _byte64.clear();

        _byte64.putInt(0);
        rch.read(_byte64);
        _byte64.flip();

        return _byte64.getLong();
    }
    
    private long readSInt32() throws IOException {
        _byte64.clear();

        _byte64.putInt(0);
        rch.read(_byte64);
        
        _byte64.flip();

        _byte64.getInt();
        return _byte64.getInt();
    }

    private long readUInt64() throws IOException {
        _byte64.clear();

        rch.read(_byte64);
        _byte64.flip();

        long uint64 = _byte64.getLong();
        
        if (uint64 < 0) {
            throw new IOException("不支持的 UINT64");
        }

        return uint64;
    }

    private ByteBuffer readBytes(long bytes) throws IOException {
        return readBytes((int) bytes);
    }

    private ByteBuffer readBytes(int bytes) throws IOException {
        ByteBuffer b = ByteBuffer.allocate(bytes);
        rch.read(b);

        if (b.remaining() > 0) {
            throw new IOException("没有完整的读取 box 类型");
        }

        b.flip();
        return b;
    }

    public ByteBuffer readContent(long position, int length) throws IOException {
        rch.position(position);

        ByteBuffer content = ByteBuffer.allocate(length);
        do {
            rch.read(content);
        } while (content.remaining() > 0 && rch.position() < rch.size());

        content.flip();
        return content;
    }
    
    public ByteBuffer readContent(Box box) throws IOException {
        rch.position(box.position() + box.headSize());

        ByteBuffer content;
        if (box.size() == 0) { // 这是要到末尾
            content = ByteBuffer.allocate((int) (rch.size() - rch.position()));
        } else {
            content = ByteBuffer.allocate((int) (box.size() - box.headSize()));
        }

        rch.read(content);

        content.flip();
        return content;
    }

    public TrakHandlerBox readTrackHandlerBox(Box box) throws IOException {
        TrakHandlerBox hdlr = new TrakHandlerBox(box);
        readFullBox(hdlr);

        // preDefined
        readUInt32();

        hdlr.handlerType(readString(4));

        // reserveed
        readString(12);

        long leftSpace = box.position() + box.size() - rch.position();
        hdlr.descript(readString((int) leftSpace));

        return hdlr;
    }

    private void readFullBox(FullBox box) throws IOException {
        if (null != box){
            logger.debug("{}'s data start at :{}", box.type(), box.position());
        }

        rch.position(box.position() + box.headSize());

        box.version(readUInt8());
        box.flags(readUInt24());
    }

    public MediaHeaderBox readMediaHeader(Box box) throws IOException {
        MediaHeaderBox mdhdBox = new MediaHeaderBox(box);
        readFullBox(mdhdBox);

        long version = mdhdBox.version();
        mdhdBox.created(readTimestamp(version));
        mdhdBox.lastModified(readTimestamp(version));

        mdhdBox.timeScale(readUInt32());

        if (VERSION_0 == version) {
            mdhdBox.duration(readUInt32());
        } else {
            mdhdBox.duration(readUInt64());
        }

        return mdhdBox;
    }

    private Timestamp readTimestamp(long version) throws IOException {
        long timestamp = 0;

        if (VERSION_0 == version) {
            timestamp = readUInt32();
        } else if (VERSION_1 == version) {
            timestamp = readUInt64();
        } else {
            throw new IOException("非法的版本号[" + version + "]");
        }

        return new Timestamp(timestamp);
    }

    public SampleChunkBox readSampleTableSampleChunkBox(Box box) throws IOException {
        SampleChunkBox stsc = new SampleChunkBox(box);
        readFullBox(stsc);

        long count = readUInt32();
        stsc.setSTSCEntryCount(count);

        for (int i = 0; i < count; i++) {
            long firstChunk = readUInt32();
            long samplesPerChunk = readUInt32();
            long sampleDescIndex = readUInt32();

            stsc.addEntry(firstChunk, samplesPerChunk, sampleDescIndex);
        }

        return stsc;
    }

    public ChunkOffsetBox readSimpleTableChunkOffsetBox(Box box) throws IOException {
        ChunkOffsetBox stco = new ChunkOffsetBox(box);
        readFullBox(stco);

        long count = readUInt32();
        stco.setOffsetCount(count);

        boolean read32 = ChunkOffsetBox.STCO.equals(box.type());
        if (read32) {
            for (int i = 0; i < count; i++) {
                stco.addOffset(readUInt32());
            }
        } else {
            for (int i = 0; i < count; i++) {
                stco.addOffset(readUInt64());
            }
        }

        return stco;
    }

    public TimeSampleBox readSampleTableTimeSampeBox(Box box) throws IOException {
        TimeSampleBox stts = new TimeSampleBox(box);
        readFullBox(stts);

        long count = readUInt32();
        stts.setSTTSCount(count);

        for (int i = 0; i < count; i++) {
            long sampleCount = readUInt32();
            long sampleDelta = readUInt32();

            stts.addSTTSRecord(sampleCount, sampleDelta);
        }

        return stts;
    }

    public SampleSizeBox readSampleTableSampleSizeBox(Box box) throws IOException {
        SampleSizeBox stsz = new SampleSizeBox(box);
        readFullBox(stsz);

        long constantSize = readUInt32();
        stsz.constantSize(constantSize);

        long sizeCount = readUInt32();
        stsz.setSampleCount(sizeCount);

        if (constantSize == 0) {
            for (int i = 0; i < sizeCount; i++) {
                stsz.addSampleSize(readUInt32());
            }
        }

        return stsz;
    }

    public SyncSampleBox readSampleTableSyncSampleBox(Box box) throws IOException {
        SyncSampleBox stss = new SyncSampleBox(box);
        readFullBox(stss);

        long syncCount = readUInt32();
        stss.setSyncCount(syncCount);

        for (int i = 0; i < syncCount; i++) {
            stss.addSyncSample(readUInt32());
        }

        return stss;
    }


    public TrakHeaderBox readTrakHeaderBox(Box box) throws IOException {
        TrakHeaderBox tkhd = new TrakHeaderBox(box);
        readFullBox(tkhd);

        long version = tkhd.version();

        tkhd.created(readTimestamp(version));
        tkhd.lastModified(readTimestamp(version));

        tkhd.trackId(readUInt32());

        // reserved
        readUInt32();

        // duration
        if (VERSION_0 == version) {
            tkhd.duration(readUInt32());
        } else {
            tkhd.duration(readUInt64());
        }

        // reserved UI32[2]
        readUInt32();
        readUInt32();

        // layer
        readUInt16();

        // Alternate Group
        tkhd.alternateGroup(readUInt16());

        // volume 8.8
        readUInt8();
        readUInt8();

        // Reserved
        readUInt16();

        // TransformMatrix
        for (int i = 0; i < 9; i++) {
            readUInt32();
        }

        // width, height
        tkhd.width(readUInt32() / 0xFFFF);
        tkhd.height(readUInt32() / 0xFFFF);

        return tkhd;
    }

    public SampleDescriptionBox readSampleTableSampleDescriptBox(Box box) throws IOException {
        SampleDescriptionBox stsd = new SampleDescriptionBox(box);
        readFullBox(stsd);

        long count = readUInt32();
        stsd.setDescriptCont(count);
        logger.debug("descript conut: {}", count);

        for (int i = 0; i < count; i++) {
            long descriptLength = readUInt32();
            String type = readString(4);
            SampleDescription sampleDescript = stsd.createSampleDescript(descriptLength, type);
            
            long endPosition = rch.position() + descriptLength - 8;

            // visual sample
            boolean isVisualSample = VisualSampleEntry.TYPES.contains(type);
            if (isVisualSample) {
                skip(24);
                long width = readUInt16();
                long height = readUInt16();
                skip (78 - 24 - 4);
                
                sampleDescript.setVideoHeight((int)height);
                sampleDescript.setVideoWidth((int)width);
                do {
                    logger.warn("startAt:{}", rch.position());
                    long compressorConfigLength = readUInt32();
                    String compressorConfigName = readBoxType();
                    ByteBuffer content = readBytes(compressorConfigLength - 8);
                    logger.warn("length:{}, name:{}", compressorConfigLength, compressorConfigName);
                    
                    sampleDescript.addConfig(compressorConfigName, content);
                } while (rch.position() < endPosition);
            }

            // audio sample
            boolean isAudioSample = AudioSampleEntry.TYPES.contains(type);
            if (isAudioSample) {
                skip(16);
                long channelCount = readUInt16();
                long sampleSize = readUInt16();
                //reserved bits - used by qt
                readUInt16();
                //reserved bits - used by qt
                readUInt16();
                //sampleRate = in.readFixedPoint1616();
                double sampleRate = readUInt32() * 1.0 / 65536;
                skip(28-16-12);
                sampleDescript.setChannelCount((int)channelCount);
                sampleDescript.setSampleRate(channelCount * sampleRate);
                sampleDescript.setSampleSize((int)sampleSize);
                
                do {
                    logger.warn("startAt:{}", rch.position());
                    long compressorConfigLength = readUInt32();
                    String compressorConfigName = readBoxType();
                    
                    ByteBuffer content;
                    if ("esds".equals(compressorConfigName)) {
                        skip(26);
                        content = readBytes(7);
                        skip(compressorConfigLength - 8 - 26 - 7);
                    } else {
                        content = readBytes(compressorConfigLength - 8);
                    }
                    logger.warn("length:{}, name:{}", compressorConfigLength, compressorConfigName);

                    
                    sampleDescript.addConfig(compressorConfigName, content);
                } while (rch.position() < endPosition);
            }

        }

        return stsd;
    }

    public CompositionOffsetBox readSampleTableCompositionOffset(Box box) throws IOException {
        CompositionOffsetBox ctts = new CompositionOffsetBox(box);
        readFullBox(ctts);
        
        long count = readUInt32();
        ctts.setCompositionOffsetCount(count);
        
        if (VERSION_0 == ctts.version()) {
            for (int i = 0; i < count; i++) {
               ctts.addCompositionOffset(readUInt32(), readUInt32()); 
            }    
        } else {
            for (int i = 0; i < count; i++) {
                ctts.addCompositionOffset(readUInt32(), readSInt32()); 
            }
        }
        
        
        return ctts;
    }
    private Logger logger = LoggerFactory.getLogger(getClass());

}
