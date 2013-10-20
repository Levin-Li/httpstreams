package github.chenxh.media.flv;

import github.chenxh.media.flv.script.metadata.KeyFrames;

import java.io.File;
import java.io.IOException;

public class TestKeyFrames  {
    

    public static void main(String[] args) throws IOException {
        FlvDecoder decoder = new FlvDecoder();

        File file = new File(ITestFiles.ROOT_DIR, ITestFiles.FILE);

        try {
            KeyFrames keyFrames = decoder.decodeKeyFrames(file);
            
            System.out.println(keyFrames);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
}
