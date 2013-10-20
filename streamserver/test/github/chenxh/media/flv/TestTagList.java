package github.chenxh.media.flv;

import github.chenxh.media.flv.tags.TagHeadVisitor;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;

public class TestTagList {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(TestTagList.class);
    
    public static void main(String[] args) throws IOException {
        FlvDecoder decoder = new FlvDecoder();

        File file = new File(ITestFiles.ROOT_DIR, ITestFiles.FILE);

        TagHeadVisitor tagHeadVisitor = new TagHeadVisitor();
        try {
            decoder.decode(file, null, tagHeadVisitor);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        System.out.println();
        List<ITagHead> tagHeads = tagHeadVisitor.getTagHeads();
        for (ITagHead tag : tagHeads) {
            logger.warn("{}", tag);
        }
    }
}
