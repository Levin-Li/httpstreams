package com.thunisoft.mediax.core.pseudostreaming.flv;

import java.io.File;

import org.apache.commons.codec.DecoderException;

import com.thunisoft.mediax.core.pseudostreaming.flv.FlvDecoder.TagIterator;
import com.thunisoft.mediax.core.pseudostreaming.flv.h264.AvcVideoTag;
import com.thunisoft.mediax.core.pseudostreaming.flv.tag.AudioTag;
import com.thunisoft.mediax.core.pseudostreaming.flv.tag.Tag;

public class TestFlvDecoder {
    public static void main(String[] args) throws DecoderException {
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


    private static void listTags(FlvDecoder decoder, String pathname) throws DecoderException {
        TagIterator iterator = decoder.decode(new File(pathname));

        while (iterator.hasNext()) {
            Tag tag = nextTag(iterator);

            System.out.println(tag);
        }
    }

    private static void listVideos(FlvDecoder decoder, String pathname) throws DecoderException {
        TagIterator iterator = decoder.decode(new File(pathname));

        while (iterator.hasNext()) {
            Tag tag = nextTag(iterator);

            if (tag instanceof AvcVideoTag) {
                System.out.println(tag);
            }
        }
    }

    private static void listAudios(FlvDecoder decoder, String pathname) throws DecoderException {
        TagIterator iterator = decoder.decode(new File(pathname));

        while (iterator.hasNext()) {
            Tag tag = nextTag(iterator);

            if (tag instanceof AudioTag) {
                System.out.println(tag);
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
