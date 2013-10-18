package github.chenxh.media.flv;

import github.chenxh.media.UnsignedDataInput;
import github.chenxh.media.flv.script.FlvMetaData;

import java.io.File;
import java.io.IOException;

public class TestMetaData {
    public static void main(String[] args) throws IOException {
        FlvDecoder decoder = new FlvDecoder();

        File file = new File(ITestFiles.ROOT_DIR, ITestFiles.FILE);

        UnsignedDataInput dataInput = null;
        try {
            dataInput = new UnsignedDataInput(file);
            FlvMetaData metaData = decoder.decodeMetaData(dataInput);
            System.out.println(metaData);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != dataInput) {
                dataInput.close();
            }
        }
    }
}
