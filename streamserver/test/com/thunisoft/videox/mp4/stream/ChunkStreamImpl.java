package com.thunisoft.videox.mp4.stream;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thunisoft.videox.flv.FlvTag;
import com.thunisoft.videox.mp4.Box;
import com.thunisoft.videox.mp4.MP4Parser;
import com.thunisoft.videox.mp4.MediaHeaderBox;
import com.thunisoft.videox.mp4.TrakHandlerBox;
import com.thunisoft.videox.mp4.TrakHeaderBox;
import com.thunisoft.videox.mp4.sampletable.ChunkOffsetBox;
import com.thunisoft.videox.mp4.sampletable.CompositionOffsetBox;
import com.thunisoft.videox.mp4.sampletable.SampleChunkBox;
import com.thunisoft.videox.mp4.sampletable.SampleDescriptionBox;
import com.thunisoft.videox.mp4.sampletable.SampleSizeBox;
import com.thunisoft.videox.mp4.sampletable.SyncSampleBox;
import com.thunisoft.videox.mp4.sampletable.TimeSampleBox;

public class ChunkStreamImpl implements Iterator<Chunk>, ChunkStream {
    private static Logger logger = LoggerFactory.getLogger(ChunkStreamImpl.class);

    private MP4Parser mp4;

    /**
     * trak 的头
     */
    final private TrakHeaderBox tkhd;

    /**
     * mp4 处理器
     */
    final private TrakHandlerBox hdlr;


    /**
     * 媒体信息
     */
    final private MediaHeaderBox mdhd;


    /**
     * trak 解码描述信息
     */
    final private SampleDescriptionBox stsd;

    /**
     * 每个 chunk 的样本个数
     */
    final private SampleChunkBox stsc;

    /**
     * chunk offset
     */
    final private ChunkOffsetBox stco;

    /**
     * 关键帧列表 如果为 null，则每个帧都是关键帧
     */
    final private SyncSampleBox stss;

    /***
     * 每个样本的大小
     */
    final private SampleSizeBox stsz;

    /**
     * 每个样本的时间戳
     */
    final private TimeSampleBox stts;

    /**
     * 视频时间戳分为显示时间戳和解码时间戳。
     * 一般来说， 解码时间戳应该先于显示时间戳的。 
     * ctts 中定义了提前量。
     */
    final private CompositionOffsetBox ctts;

    /** 上一个被使用的样本的索引 */
    private int lastSampleIndex = -1;
    private int lastChunkIndex = -1;


    public ChunkStreamImpl(MP4Parser mp4, Box trak) throws IOException {
        final Box parentBox = trak;

        Box box = null;

        // trak header
        box = mp4.findBox("tkhd", parentBox);
        tkhd = mp4.readTrakHeaderBox(box);

        // mdhd
        box = mp4.findBox("mdia.mdhd", parentBox);
        mdhd = mp4.readMediaHeader(box);

        // trak 处理器
        box = mp4.findBox("mdia.hdlr", parentBox);
        hdlr = mp4.readTrackHandlerBox(box);

        // 解码描述信息
        box = mp4.findBox("mdia.minf.stbl.stsd", parentBox);
        stsd = mp4.readSampleTableSampleDescriptBox(box);

        //
        box = mp4.findBox("mdia.minf.stbl.stts", parentBox);
        stts = mp4.readSampleTableTimeSampeBox(box);

        //
        box = mp4.findBox("mdia.minf.stbl.stss", parentBox);
        if (null != box) {
            stss = mp4.readSampleTableSyncSampleBox(box);
        } else {
            stss = null;
        }

        //
        box = mp4.findBox("mdia.minf.stbl.stsc", parentBox);
        stsc = mp4.readSampleTableSampleChunkBox(box);

        //
        box = mp4.findBox("mdia.minf.stbl.stsz", parentBox);
        stsz = mp4.readSampleTableSampleSizeBox(box);

        //
        box = mp4.findBox("mdia.minf.stbl.stco", parentBox);
        if (null == box) {
            box = mp4.findBox("mdia.minf.stbl.co64", parentBox);
        }
        stco = mp4.readSimpleTableChunkOffsetBox(box);

        box = mp4.findBox("mdia.minf.stbl.ctts", parentBox);
        if (null != box) {
            ctts = mp4.readSampleTableCompositionOffset(box);
        } else {
            ctts = null;
        }

        this.mp4 = mp4;
    }

    /* (non-Javadoc)
     * @see com.thunisoft.videox.mp4.stream.ChunkStream#hasNext()
     */
    @Override
    public boolean hasNext() {
        return lastChunkIndex + 1 < stco.offsetCount();
    }

    /* (non-Javadoc)
     * @see com.thunisoft.videox.mp4.stream.ChunkStream#next()
     */
    @Override
    public Chunk next() {
        if (!hasNext()) {
            return null;
        }

        final int chunkIndex = lastChunkIndex + 1;
        final int sampleIndex = lastSampleIndex + 1;
        logger.debug("chunk[{}]", chunkIndex+1);

        // 这个 chunk 的样本数
        int sampleCount = (int) stsc.sampleCountOfChunk(chunkIndex);
        ChunkImpl chunk = new ChunkImpl(sampleCount);
        chunk.timeStart = sampleIndex * stts.getSampleDelta();
        chunk.timeEnd = (sampleIndex + sampleCount) * stts.getSampleDelta();
        chunk.timeScale = mdhd.timeScale();

        // 文件存储的起始位置
        long chunkPosition = 0;
        chunkPosition = stco.getOffset(chunkIndex);
        logger.debug("chunk position:[{}],  [{}] samples, ", chunkPosition, sampleCount);

        // 可以读取的长度
        long sampleOffset = chunkPosition;
        for (int i = 0; i < sampleCount; i++) {
            int currentSampleIndex = sampleIndex + i;

            SampleImpl sample = new SampleImpl(mp4);
            sample.isKeyFrame = null == stss ? true : stss.isSyncFrame(currentSampleIndex+1);
            sample.compositeTime = (null == ctts ? 0: ctts.getCompositionOffset(currentSampleIndex));
            sample.timestamp = currentSampleIndex * stts.getSampleDelta();
            sample.timeScale = mdhd.timeScale();
            sample.startAt = sampleOffset;
            sample.length = (int) stsz.getSampleSize(currentSampleIndex);
            sample.flvTagType = hdlr.isAudio()?FlvTag.TAG_TYPE_AUDIO:FlvTag.TAG_TYPE_VIDEO;

            chunk.addSample(sample);
            logger.debug("[{}] {}", currentSampleIndex, sample);
            
            sampleOffset += stsz.getSampleSize(currentSampleIndex);

            lastSampleIndex = currentSampleIndex;
        }

 

        lastChunkIndex = chunkIndex;

        return chunk;
    }

    /* (non-Javadoc)
     * @see com.thunisoft.videox.mp4.stream.ChunkStream#remove()
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove");
    }

    public static class ChunkImpl implements Chunk {
        private List<Sample> samples = null;

        private long timeStart;
        private long timeEnd;
        private long timeScale;
        
        public ChunkImpl(int sampleCount) {
            samples = new ArrayList<Sample>(sampleCount);
        }
        
        public void addSample(Sample sample) {
            samples.add(sample);
        }
        
        @Override
        public List<Sample> getSamples() throws IOException {
            return samples;
        }

        public long getTimeStart() {
            return timeStart;
        }

        public long getTimeEnd() {
            return timeEnd;
        }

        public long getTimeScale() {
            return timeScale;
        }

    }
    
    public static final class SampleImpl implements Sample {
        private boolean isKeyFrame;
        private int length;
        private long compositeTime;
        private long timestamp;
        private long timeScale;
        
        private int flvTagType;
        
        
        /** 文件的起始位置 */
        private long startAt;
        private MP4Parser mp4;

        public SampleImpl(MP4Parser mp4) {
            this.mp4 = mp4;
        }

        /* (non-Javadoc)
         * @see com.thunisoft.videox.mp4.stream.Sample#getData()
         */
        @Override
        public ByteBuffer getData() throws IOException {
            return mp4.readContent(startAt, length);
        }

        /* (non-Javadoc)
         * @see com.thunisoft.videox.mp4.stream.Sample#isKeyFrame()
         */
        @Override
        public boolean isKeyFrame() {
            return isKeyFrame;
        }

        /* (non-Javadoc)
         * @see com.thunisoft.videox.mp4.stream.Sample#getLength()
         */
        @Override
        public int getLength() {
            return length;
        }

        /* (non-Javadoc)
         * @see com.thunisoft.videox.mp4.stream.Sample#getTimestamp()
         */
        @Override
        public long getTimestamp() {
            return timestamp;
        }

        /* (non-Javadoc)
         * @see com.thunisoft.videox.mp4.stream.Sample#getTimeScale()
         */
        @Override
        public long getTimeScale() {
            return timeScale;
        }
        
        public long getCompositeTime() {
            return compositeTime;
        }

        public int getFlvTagType() {
            return flvTagType;
        }
        
        @Override
        public String toString() {
            StringBuilder b = new StringBuilder();

            b.append("Sample{");
            b.append("keyFrame:").append(isKeyFrame);
            b.append(", type:").append(flvTagType);
            b.append(", time:").append(timestamp);
            b.append(", compositeTime:").append(compositeTime);
            b.append(", data:[").append(startAt).append(", ").append(length).append("]");
            
            b.append("}");
            return b.toString();
        }

    
        
    }
}
