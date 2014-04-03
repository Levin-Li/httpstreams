package com.thunisoft.videox;

import java.io.FileOutputStream;
import java.io.IOException;

import com.thunisoft.videox.flv.FlvWiter;
import com.thunisoft.videox.mp4.MP4Parser;

public class TestFlvConverter {
    public static void main(String[] args) throws IOException {
        String name = "E:/media/国王的演讲/0001.优酷网-国王的演讲[高清版]-0001.mp4";
        MP4Parser parser = new MP4Parser(name);
        
        
        FlvConverter converter = new FlvConverter(parser);
        
        
        FileOutputStream out = new FileOutputStream("C:/thunisoft/Apache2.2/htdocs/flv/my.flv");
        FlvWiter flv = new FlvWiter(out);
        converter.convert(flv);
        
        out.close();
    }
}
