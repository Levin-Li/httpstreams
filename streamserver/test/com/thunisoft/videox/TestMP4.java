package com.thunisoft.videox;

import java.io.IOException;

import com.thunisoft.videox.mp4.Box;
import com.thunisoft.videox.mp4.MP4Parser;

public class TestMP4 {
    public static void main(String[] args) throws IOException {
        String name = "E:/media/国王的演讲/0001.优酷网-国王的演讲[高清版]-0001.mp4";
        MP4Parser parser = new MP4Parser(name);
        
        
        // trak head
        Box[] tkhd = parser.findBoxes("moov.trak.tkhd");
        for (int i = 0; i < tkhd.length; i++) {
            Box box = parser.readTrakHeaderBox(tkhd[i]);
            System.out.println(box);
        }

        // time 2 sample
        Box[] stts = parser.findBoxes("moov.trak.mdia.minf.stbl.stts");
        for (int i = 0; i < stts.length; i++) {
            Box box = parser.readSampleTableTimeSampeBox(stts[i]);
            System.out.println(box);
        }
        
        // 多少个样本组成一个 chunk
        Box[] stsc = parser.findBoxes("moov.trak.mdia.minf.stbl.stsc");
        for (int i = 0; i < stsc.length; i++) {
            Box box = parser.readSampleTableSampleChunkBox(stsc[i]);
            System.out.println(box);
        }
        
        // 每个 chunk 的偏移量
        Box[] stco = parser.findBoxes("moov.trak.mdia.minf.stbl.stco");
        for (int i = 0; i < stco.length; i++) {
            Box box = parser.readSimpleTableChunkOffsetBox(stco[i]);
            System.out.println(box);
        }
        
        
        // simple table size
        Box[] stsz = parser.findBoxes("moov.trak.mdia.minf.stbl.stsz");
        for (int i = 0; i < stsz.length; i++) {
            Box box = parser.readSampleTableSampleSizeBox(stsz[i]);
            System.out.println(box);
        }
        
        // sample table sync sample
        Box[] stss = parser.findBoxes("moov.trak.mdia.minf.stbl.stss");
        for (int i = 0; i < stss.length; i++) {
            Box box = parser.readSampleTableSyncSampleBox(stss[i]);
            System.out.println(box);
        }
        

        // mdhd
        Box[] mdhd = parser.findBoxes("moov.trak.mdia.mdhd");
        for (int i = 0; i < mdhd.length; i++) {
            Box box = parser.readMediaHeader(mdhd[i]);
            System.out.println(box);
        }

        // handler 
        Box[] hdlrBox = parser.findBoxes("moov.trak.mdia.hdlr");
        for (int i = 0; i < hdlrBox.length; i++) {
            Box box = parser.readTrackHandlerBox(hdlrBox[i]);
            System.out.println(box);
        }
   
        Box[] stsd = parser.findBoxes("moov.trak.mdia.minf.stbl.stsd");
        for (int i = 0; i < stsd.length; i++) {
            Box box = parser.readSampleTableSampleDescriptBox(stsd[i]);
            System.out.println(box);
        }
        
        Box[] ctts = parser.findBoxes("moov.trak.mdia.minf.stbl.ctts");
        for (int i = 0; i < ctts.length; i++) {
            Box box = parser.readSampleTableCompositionOffset(ctts[i]);
            System.out.println(box);
        }
    }
}
