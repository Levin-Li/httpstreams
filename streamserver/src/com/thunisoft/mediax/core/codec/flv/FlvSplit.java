package com.thunisoft.mediax.core.codec.flv;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thunisoft.mediax.core.codec.amf.AMF0Codec;
import com.thunisoft.mediax.core.codec.flv.tag.Tag;
import com.thunisoft.mediax.core.codec.flv.tag.VideoTag;

public class FlvSplit {
    final private Logger logger = LoggerFactory.getLogger(getClass());
    
    private TagDecoder tagDecoder = new TagDecoder();
    
    public void split(String src, String target) throws Exception {
        FlvIterator itr = new FlvIterator(new File(src));
        try {
            // metadata
            ByteBuffer meta = itr.next();
            FlvMetaData metadata = (FlvMetaData) tagDecoder.decodeTag(meta);
            
            // h264Config
            List<ByteBuffer> h264Config = new ArrayList<ByteBuffer>();

            int segIndex = 0;
            while(itr.hasNext()) {
                String path = FilenameUtils.getFullPath(target);
                String name = FilenameUtils.getBaseName(target);
                String suffix = FilenameUtils.getExtension(target);

                File newFile = new File(path, name + "_" + (segIndex++) + "." + suffix);
                FileOutputStream out = null;
                try {
                    newFile.createNewFile();
                    
                    out = new FileOutputStream(newFile);

                    long start =  itr.position();
                    createNewFile(out.getChannel(), itr, metadata, h264Config, 60 * 60 * 1000);

                    long end = itr.position();
                    logger.info("[{}, {}), {}KB", new Object[]{start, end, (end - start)/1024});
                    out.close();
                } finally {
                    IOUtils.closeQuietly(out);
                }
            }
            
        } finally {
            itr.close();
        }
    }
    
    private void createNewFile(FileChannel target, FlvIterator itr, FlvMetaData meta, List<ByteBuffer> h264Config, int nextDurations) throws DecoderException, IOException, EncoderException {
        // flv head
        writeTag(target, itr.getFileHead());
        
        // metadata
        ByteBuffer metaTag = AMF0Codec.encode(new Object[]{"onMetaData", meta.getMetadata()});
        ByteBuffer taghead = ByteBuffer.allocate(11);
        
        // type + datasize
        taghead.putInt((18 << 24) + metaTag.remaining());
        // timestamp
        taghead.putInt(0);
        // streamId
        taghead.put((byte)0);
        taghead.putShort((short)0);
        taghead.flip();
        
        ByteBuffer tagData = ByteBuffer.allocate(taghead.remaining() + metaTag.remaining());
        tagData.put(taghead);
        tagData.put(metaTag);
        tagData.flip();
        writeTag(target, tagData);
        
        // h264 tags
        for (ByteBuffer byteBuffer : h264Config) {
            writeTag(target, byteBuffer);
        }

        // tags until duration end
        boolean sholdInterupt = false;
        long firstTimestamp = -1;
        Tag tag;
        do {
            ByteBuffer data = itr.next();
            tag = tagDecoder.decodeTag(data.asReadOnlyBuffer());

            if (firstTimestamp < 0 && tag.getTimestamp() != 0) {
                firstTimestamp = tag.getTimestamp();
            } else if (tag instanceof VideoTag
                    && tag.getTimestamp() - firstTimestamp > nextDurations) {
                sholdInterupt = true;
            }
            
            if (sholdInterupt && tag.isKey()) {
                // 退回这次读取的 tag
                itr.back();
                break;
            } else {
                writeTag(target, data);
            }
        } while(itr.hasNext());
        
    }
    
    private void writeTag(WritableByteChannel target, ByteBuffer data) throws IOException {
        data.mark();
        do {
            target.write(data);
        } while (data.remaining() > 0);
        data.reset();

        ByteBuffer size = ByteBuffer.allocate(4);
        size.putInt(data.remaining());
        size.flip();

        target.write(size);
    }
}
