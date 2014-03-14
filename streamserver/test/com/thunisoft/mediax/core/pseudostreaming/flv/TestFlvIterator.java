package com.thunisoft.mediax.core.pseudostreaming.flv;

import java.io.File;
import java.nio.ByteBuffer;

import junit.framework.TestCase;

import com.thunisoft.mediax.core.codec.flv.FlvIterator;
import com.thunisoft.mediax.core.codec.flv.TagDecoder;
import com.thunisoft.mediax.core.codec.flv.tag.Tag;

public class TestFlvIterator extends TestCase {
    private FlvIterator iterator;
    
    private TagDecoder tagDecoder = new TagDecoder();
    
    protected void setUp() throws Exception {
        String pathname = "C:/thunisoft/Apache2.2/htdocs/flv/kuiba-0001.flv";
        File file = new File(pathname);
        iterator = new FlvIterator(file);
        
    };
    
    @Override
    protected void tearDown() throws Exception {
        iterator.close();
    }
    
    
    public void testListTags() throws Exception {
        while(iterator.hasNext()) {
            ByteBuffer bytes = iterator.next();
            
            Tag tag = tagDecoder.decodeTag(bytes);
            System.out.println(tag);
            
        }
    }
}
