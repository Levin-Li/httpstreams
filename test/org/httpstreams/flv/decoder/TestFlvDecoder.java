package org.httpstreams.flv.decoder;

import java.io.FileInputStream;
import java.io.IOException;

import org.httpstreams.flv.decoder.Flv;
import org.httpstreams.flv.decoder.FlvDecoder;
import org.httpstreams.flv.decoder.FlvTagIterator;
import org.httpstreams.flv.decoder.tags.data.TagData;


public class TestFlvDecoder {
    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        FlvTagIterator  tagIterator =null;
        FlvDecoder file = new FlvDecoder();
        Flv flv = file.decode("test.flv");
        System.out.println(flv);

        tagIterator = flv.getTagIterator();
        listTags(tagIterator);
        
        
        tagIterator = flv.seek(0.438);
        listTags(tagIterator);

    
        

        test2(args);
    }

    private static void listTags(
            FlvTagIterator tagIterator)
            throws IOException {
        long tagNumber = 0;
        int audioTags = 0;
        int videoTags = 0;
        int scriptTags = 0;
        int unknownTags = 0;
        while (tagIterator.hasNext()) {
            org.httpstreams.flv.decoder.FlvTag flvTag = tagIterator.next();
            
            // 计算包数量
            if (flvTag.isAudio()) {
                audioTags ++;
            } else if (flvTag.isVideo()) {
                videoTags ++;
            } else if (flvTag.isScript()) {
                scriptTags ++;
            } else {
                unknownTags ++;
            }

            tagNumber ++;
            TagData data = flvTag.getData();
            System.out.println("["+tagNumber+"]" + flvTag + "; data: " + data);
        }
    }
    
    public static void test2(String[] args) throws Exception {
        FileInputStream inStream = new FileInputStream("test.flv");
        byte[] b = new byte[11];
        System.out.println(inStream.read(b));
        
        System.out.println(inStream.skip(-11));
        System.out.println((char)inStream.read());
    }
}
