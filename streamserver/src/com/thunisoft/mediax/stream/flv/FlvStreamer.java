package com.thunisoft.mediax.stream.flv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

import org.apache.commons.io.IOUtils;

import com.thunisoft.mediax.core.ByteBufferUtils;
import com.thunisoft.mediax.core.flv.FlvFile;
import com.thunisoft.mediax.stream.AbstractFileStreamer;

/**
 * flv 流化
 * 
 * @since V1.0 2014-3-3
 * @author chenxh
 */
public class FlvStreamer extends AbstractFileStreamer {

    
    public FlvStreamer(File file, double startAt) throws FileNotFoundException {
        super(file, startAt);
    }

    public String contentType() {
        return "video/x-flv";
    }

    @Override
    public void transfer(WritableByteChannel outChannel) throws IOException {
        if (startAt() > 0) {
            write(outChannel, startAt());
        } else {
            write(outChannel);
        }
        
    }

    private void write(WritableByteChannel outChannel) throws IOException {
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file());
            
            fin.getChannel().transferTo(0, file().length(), outChannel);
        } finally {
            IOUtils.closeQuietly(fin);
        }
        
    }

    public void write(WritableByteChannel outChannel, double startAt) throws IOException {
        FlvFile flvFile = null;
        try {
            flvFile = new FlvFile(file());

            ByteBuffer b = flvFile.getFlvHead();
            
            // flv head
            outChannel.write(b);
            outChannel.write(ByteBuffer.wrap(ByteBufferUtils.wrapUInt32(b.position())));

            // flv data
            FileChannel fch = flvFile.seek((long)startAt);
            long count = fch.size() - fch.position();
            fch.transferTo(fch.position(), count, outChannel);
        } finally {
            IOUtils.closeQuietly(flvFile);
        }
    }

}
