package com.thunisoft.mediax.mp4;

import java.io.IOException;

import com.coremedia.iso.BoxParser;
import com.coremedia.iso.IsoFile;
import com.coremedia.iso.PropertyBoxParserImpl;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;

public class ListBoxes {
    public static void main(String[] args) throws IOException {
        IsoFile file =
                new IsoFile("D:/Thunisoft/MyEclipse/workspaces/统一音视频平台/streamer/web/mp4/test1.mp4");


        BoxParser boxParser = new PropertyBoxParserImpl();
        listBoxes(file, boxParser, 1);
    }


    private static void listBoxes(Object box, BoxParser boxParser, int level) {
        StringBuilder space = new StringBuilder();
        for (int i = 0; i < level; i++) {
            System.out.print("  ");
        }

        if (box instanceof Container) {
            System.out.println(space.toString() + boxSize(box) + box);
            Container container = (Container) box;
            for (Box item : container.getBoxes()) {
                listBoxes(item, boxParser, level + 1);
            }
        } else if (box instanceof Box) {
            System.out.println(boxSize((Box) box) + space.toString() + box);
        } else {
            System.out.println("---->" + box);
        }
    }

    private static String boxSize(Object box) {

        if (box instanceof Box) {
            return "[" + ((Box)box).getSize() + "]";
        } else {
            return "";
        }
    }
}
