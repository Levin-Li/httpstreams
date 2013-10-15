package github.chenxh.media.flv;

import github.chenxh.media.UnsignedDataInput;
import github.chenxh.media.flv.script.FlvMetaData;
import github.chenxh.media.flv.tags.KeyFrameVisitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class TestKeyFrames {
    public static void main(String[] args) throws IOException {
        FlvDecoder decoder = new FlvDecoder();

        File file = new File("C:/thunisoft/media/flv/test", "cuepoints.flv");

        FileInputStream inStream = null;
        try {
            inStream = new FileInputStream(file);
            KeyFrameVisitor frameVisitor = new KeyFrameVisitor();
            FlvSignature flv = decoder.decode(new UnsignedDataInput(inStream), frameVisitor);
            
            System.out.println(frameVisitor.getKeyFrames());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != inStream) {
                inStream.close();
            }
        }
    }
}
