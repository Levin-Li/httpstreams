package com.thunisoft.mediax.core.amf;

import java.io.File;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;

import com.thunisoft.mediax.core.pseudostreaming.flv.FlvDecoder;
import com.thunisoft.mediax.core.pseudostreaming.flv.FlvDecoder.TagIterator;
import com.thunisoft.mediax.core.pseudostreaming.flv.FlvMetaData;
import com.thunisoft.mediax.core.pseudostreaming.flv.tag.Tag;

public class TestAmf {
    public static void main(String[] args) throws DecoderException, EncoderException {
        FlvDecoder decoder = new FlvDecoder();

        // String pathname = "D:/Thunisoft/MyEclipse/workspaces/统一音视频平台/red5-client/03.flv";
        String pathname = "C:/thunisoft/Apache2.2/htdocs/flv/kuiba-0001.flv";
        listTags(decoder, pathname);
        
       // listAudios(decoder, pathname);

        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("");

      //  listVideos(decoder, pathname);
    }


    private static void listTags(FlvDecoder decoder, String pathname) throws DecoderException, EncoderException {
        TagIterator iterator = decoder.decode(new File(pathname));

        while (iterator.hasNext()) {
            Tag tag = nextTag(iterator);

            System.out.println(tag);
            if (tag instanceof FlvMetaData) {
                AMF0Encoder encoder = new AMF0Encoder();
                ByteBuffer bytes = encoder.encode(((FlvMetaData) tag).getMetadata());
                System.out.println(bytes.limit());
                
                AMF0Decoder amf0Decoder = new AMF0Decoder();
                System.out.println(Arrays.toString(amf0Decoder.decode(bytes)));
                break;
            }

        }
    }
    
    private static Tag nextTag(TagIterator iterator) {

        try {
            Tag tag = iterator.nextTag();
            return tag;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
