package com.thunisoft.mediax.core.flv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TestFlvReader {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        File flv = new File("D:/Thunisoft/MyEclipse/workspaces/统一音视频平台/streamer/web/flv/kuiba-0001.flv");
        
        FlvFile reader = new FlvFile(flv);
        FlvMetaData object = reader.getMetadata();
        
        System.out.println(object);
    }
}
