package github.chenxh.media.flv;

import github.chenxh.media.UnsignedDataInput;
import github.chenxh.media.flv.script.metadata.KeyFrames;
import github.chenxh.media.flv.tags.TagHeadVisitor;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TestTagList {
    public static void main(String[] args) throws IOException {
        FlvDecoder decoder = new FlvDecoder();

        File file = new File(ITestFiles.ROOT_DIR, ITestFiles.FILE);

        UnsignedDataInput inStream = null;
        TagHeadVisitor tagHeadVisitor = new TagHeadVisitor();
        try {
            inStream = new UnsignedDataInput(file);
            decoder.decode(inStream, null, tagHeadVisitor);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != inStream) {
                inStream.close();
            }
        }
        
        System.out.println();
        List<ITagHead> tagHeads = tagHeadVisitor.getTagHeads();
        for (ITagHead tag : tagHeads) {
            System.out.println(tag);
        }
    }
}
