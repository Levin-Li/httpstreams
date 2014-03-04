package com.thunisoft.mediax.core.mp4;

import java.io.File;
import java.io.IOException;

public class TestMp4Reader {
    public static void main(String[] args) throws IOException {
        File file = new File("E:/programs/Workspaces/MyEclipse 8.6/streamer/web/mp4/test1.mp4");
        Mp4Reader reader = new Mp4Reader(file);
        
        while (reader.hasNext()) {
            Box box = reader.next();
            
            if (BoxType.ftyp.equals(box.getType())) {
                reader.parseBox(box);
            }
            
            System.out.println(box);
        }
        
        
        reader.close();
    }
}
