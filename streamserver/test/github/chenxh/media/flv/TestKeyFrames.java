package github.chenxh.media.flv;

import github.chenxh.media.UnsignedDataInput;
import github.chenxh.media.flv.script.KeyFrames;

import java.io.File;
import java.io.IOException;

public class TestKeyFrames {
    public static void main(String[] args) throws IOException {
        FlvDecoder decoder = new FlvDecoder();

        File file = new File("C:/thunisoft/media/flv/test", "cuepoints.flv");

        UnsignedDataInput inStream = null;
        try {
            inStream = new UnsignedDataInput(file);
            KeyFrames keyFrames = decoder.decodeKeyFrames(inStream);
            
            System.out.println(keyFrames);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != inStream) {
                inStream.close();
            }
        }
    }
}
