package com.thunisoft.videox;

import java.io.IOException;
import java.util.List;

import com.thunisoft.mediax.core.codec.amf.AMFArray;
import com.thunisoft.mediax.core.codec.flv.FlvMetaData;
import com.thunisoft.videox.flv.FLVHead;
import com.thunisoft.videox.flv.FlvTag;
import com.thunisoft.videox.flv.FlvWiter;
import com.thunisoft.videox.flv.format.AudioFormat;
import com.thunisoft.videox.flv.format.VideoFormat;
import com.thunisoft.videox.mp4.Box;
import com.thunisoft.videox.mp4.MP4Parser;
import com.thunisoft.videox.mp4.MediaHeaderBox;
import com.thunisoft.videox.mp4.TrakHandlerBox;
import com.thunisoft.videox.mp4.TrakHeaderBox;
import com.thunisoft.videox.mp4.sampletable.SampleDescriptionBox;
import com.thunisoft.videox.mp4.sampletable.TimeSampleBox;
import com.thunisoft.videox.mp4.stream.ChunkStream;
import com.thunisoft.videox.mp4.stream.ChunkStreamImpl;
import com.thunisoft.videox.mp4.stream.ChunkStreamMixer;
import com.thunisoft.videox.mp4.stream.Sample;

public class FlvConverter {
    private MP4Parser mp4;

    public FlvConverter(MP4Parser mp4) {
        this.mp4 = mp4;
    }

    public void convert(FlvWiter flv) throws IOException {
        // flv 头
        FLVHead head = getFlvHead();
        // head.hasVideo(false);
        // head.hasAudio(false);
        
        flv.writeFlvHead(head);

        // metadata
        AMFArray meta = getMetadata(head);
        flv.writeMetaData(meta);

        // h264 的配置信息
        if (head.hasVideo()) {
            SampleDescriptionBox config = getVideoConfigData();
            if (null != config && null != config.getVideoConfig()) {
                flv.writeAvcConfig(config.getVideoConfig());
            }
        }

        // aac 的配置信息
        if (head.hasAudio()) {
            SampleDescriptionBox config = getAudioConfigData();
            if (null != config && null != config.getAudioConfig()) {
                AudioFormat format = flv.getAudioFormat();
                format.soundFormat(AudioFormat.SOUND_FORMAT_AAC);
                format.soundChannels(config.getAudioChannelCount());
                format.soundSize(config.getAudioSampleSize());
                format.soundRate(config.getAudioSampleRate());
                
                
                flv.writeAacConfig(config.getAudioConfig());
            }
        }

        ChunkStreamImpl stream = null;
        // 视频流
        /**
        stream = new ChunkStream(mp4, mp4.findBox("moov.trak[0]"));
        while(stream.hasNext()) {
            Chunk chunk = stream.next();
            for (Sample sample : chunk.getSamples()) {
                long compositeTime = sample.getCompositeTime() * 1000 / sample.getTimeScale();
                double timestampInSecond = ((double)sample.getTimestamp()) / sample.getTimeScale();
                flv.writeAvcPacket((long)(timestampInSecond * 1000), compositeTime, sample.isKeyFrame(), sample.getData());
            }
        }*/
        
        // 音频
        /**
        stream = new ChunkStreamImpl(mp4, mp4.findBox("moov.trak[1]"));
        while(stream.hasNext()) {
            Chunk chunk = stream.next();
            for (Sample sample : chunk.getSamples()) {
                double timestampInSecond = ((double)sample.getTimestamp()) / sample.getTimeScale();
                flv.writeAacPacket((long)(timestampInSecond * 1000), sample.getData());
            }
        }
        */
        
        // 音频和视频
        Box[] traks = mp4.findBoxes("moov.trak");
        ChunkStream[] streams = new ChunkStream[traks.length];
        for (int i = 0; i < streams.length; i++) {
            streams[i] = new ChunkStreamImpl(mp4, traks[i]);
        }
        
        ChunkStreamMixer mixer = new ChunkStreamMixer(streams);
        List<Sample> samples = mixer.getSamples();
        for (Sample sample : samples) {
            if (sample.getFlvTagType() == FlvTag.TAG_TYPE_AUDIO) {
                double timestampInSecond = ((double)sample.getTimestamp()) / sample.getTimeScale();
                flv.writeAacPacket((long)(timestampInSecond * 1000), sample.getData());
            } else {
                long compositeTime = sample.getCompositeTime() * 1000 / sample.getTimeScale();
                double timestampInSecond = ((double)sample.getTimestamp()) / sample.getTimeScale();
                flv.writeAvcPacket((long)(timestampInSecond * 1000), compositeTime, sample.isKeyFrame(), sample.getData());
            }
        }
        
    }


    public FLVHead getFlvHead() throws IOException {
        FLVHead flvHead = new FLVHead();

        Box[] handlers =
                mp4.findBoxes(TrakHandlerBox.CONTAINER + TrakHandlerBox.QUERY_DOT + TrakHandlerBox.TYPE);
        for (int i = 0; i < handlers.length; i++) {
            TrakHandlerBox handler = mp4.readTrackHandlerBox(handlers[i]);

            if (handler.isAudio()) {
                flvHead.hasAudio(true);
            } else if (handler.isVideo()) {
                flvHead.hasVideo(true);
            }
        }

        return flvHead;
    }

    public AMFArray getMetadata(FLVHead flv) throws IOException {
        AMFArray meta = new AMFArray(11);

        // has audio or video
        meta.put(FlvMetaData.P_HAS_AUDIO, flv.hasAudio());
        meta.put(FlvMetaData.P_HAS_VIDEO, flv.hasVideo());

        // video data rate
        // audio sample rate
        // duration
        Box[] traks = mp4.findBoxes("moov.trak");
        for (int i = 0; i < traks.length; i++) {
            Box trak = traks[i];


            Box tkhd = mp4.findBox("tkhd", trak);
            Box mdhd = mp4.findBox("mdia.mdhd", trak);
            Box hdlr = mp4.findBox("mdia.hdlr", trak);
            Box stts = mp4.findBox("mdia.minf.stbl.stts", trak);

            if (null != mdhd && null != hdlr && null != stts) {
                TrakHeaderBox trakHead = mp4.readTrakHeaderBox(tkhd);
                TrakHandlerBox handler = mp4.readTrackHandlerBox(hdlr);
                MediaHeaderBox mediaHeader = mp4.readMediaHeader(mdhd);
                TimeSampleBox timeSample = mp4.readSampleTableTimeSampeBox(stts);

                if (handler.isAudio() && timeSample.getSampleDelta() > 0) {
                    meta.put(FlvMetaData.P_AUDIOSAMPLERATE, mediaHeader.timeScale() * 1.0
                            / timeSample.getSampleDelta());
                } else if (handler.isVideo() && timeSample.getSampleDelta() > 0) {
                    meta.put(FlvMetaData.P_FRAMERATE,
                            mediaHeader.timeScale() * 1.0 / timeSample.getSampleDelta());
                    meta.put(FlvMetaData.P_HEIGHT, trakHead.height());
                    meta.put(FlvMetaData.P_WIDTH, trakHead.width());
                }

                meta.put(FlvMetaData.P_DURATION, mediaHeader.durationInSeconds());
            }
        }

        // has keyframe
        meta.put(FlvMetaData.P_HAS_KEYFRAMES, false);

        // had meta
        meta.put(FlvMetaData.P_HAS_METADATA, true);
        meta.put(FlvMetaData.P_VIDEO_CODEC_ID, VideoFormat.CODEC_ID_AVC);

        // 作者
        meta.put(FlvMetaData.P_METADATA_CREATOR, "chenxh@thunisoft.com");
        return meta;
    }


    private SampleDescriptionBox getVideoConfigData() throws IOException {
        Box[] box = mp4.findBoxes("moov.trak.mdia.minf.stbl.stsd");
        for (int i = 0; i < box.length; i++) {
            SampleDescriptionBox stsd = mp4.readSampleTableSampleDescriptBox(box[i]);

            if (null != stsd && null != stsd.getVideoConfig()) {
                return stsd;
            }
        }

        return null;
    }

    private SampleDescriptionBox getAudioConfigData() throws IOException {
        Box[] box = mp4.findBoxes("moov.trak.mdia.minf.stbl.stsd");
        for (int i = 0; i < box.length; i++) {
            SampleDescriptionBox stsd = mp4.readSampleTableSampleDescriptBox(box[i]);

            if (null != stsd && null != stsd.getAudioConfig()) {
                return stsd;
            }
        }

        return null;
    }
}
