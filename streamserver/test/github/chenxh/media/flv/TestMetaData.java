package github.chenxh.media.flv;

import github.chenxh.media.flv.script.FlvMetaData;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestMetaData {
    public static void main(String[] args) throws IOException {
        Logger logger = LoggerFactory.getLogger(TestMetaData.class);
        logger.debug("test");
        FlvDecoder decoder = new FlvDecoder();

        File file = new File("C:/thunisoft/media/flv/test", "cuepoints.flv");

        FileInputStream inStream = null;
        try {
            inStream = new FileInputStream(file);
            FlvMetaData metaData = decoder.decode(inStream);
            System.out.println(metaData);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != inStream) {
                inStream.close();
            }
        }
    }
}
