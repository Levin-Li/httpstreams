package github.chenxh.media.flv;

import github.chenxh.media.flv.script.metadata.FlvMetaData;

import java.io.File;
import java.io.IOException;

public class TestMetaData {
    public static void main(String[] args) throws IOException {
        FlvDecoder decoder = new FlvDecoder();

        File file = new File(ITestFiles.ROOT_DIR, ITestFiles.NEW_FILE);

        try {
            FlvMetaData metaData = decoder.decodeMetaData(file);
            System.out.println(metaData);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
}
