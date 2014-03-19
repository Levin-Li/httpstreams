package com.thunisoft.mediax.core.pseudostreaming.flv;

import com.thunisoft.mediax.core.codec.flv.FlvSplit;

public class TestFlvSplit {
    public static void main(String[] args) throws Exception {
        FlvSplit split = new FlvSplit();
        
        String src = "D:/Thunisoft/MyEclipse/workspaces/统一音视频平台/streamer/web/flv/kuiba/kuiba-0002.flv";
        String dest = "D:/Thunisoft/MyEclipse/workspaces/统一音视频平台/streamer/web/flv/kuiba/kuiba-0002.flv";
        split.split(src, dest);
    }
}
