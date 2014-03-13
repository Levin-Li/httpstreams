package com.thunisoft.mediax.core.pseudostreaming.flv;

import java.io.File;

import org.apache.commons.codec.DecoderException;

import com.thunisoft.mediax.core.pseudostreaming.flv.FlvDecoder.TagIterator;
import com.thunisoft.mediax.core.pseudostreaming.flv.tag.AudioTag;
import com.thunisoft.mediax.core.pseudostreaming.flv.tag.Tag;
import com.thunisoft.mediax.core.pseudostreaming.flv.tag.VideoTag;

public class TestFlvDecoder {
    public static void main(String[] args) throws DecoderException {
        FlvDecoder decoder = new FlvDecoder();

        String pathname = "D:/Thunisoft/MyEclipse/workspaces/统一音视频平台/red5-record/03.flv";
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

            if (tag instanceof VideoTag) {
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
            // e.printStackTrace();
            return null;
        }
    }
}
