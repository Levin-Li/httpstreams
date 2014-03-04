package com.thunisoft.mediax.stream.flv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
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

    public FlvStreamer(File file) throws FileNotFoundException {
        super(file);
    }

    public String contentType() {
        return "video/x-flv";
    }

    @Override
    public void write(OutputStream out) throws IOException {
        FileInputStream fin = new FileInputStream(file());
        try {
            FileChannel ch = fin.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 8);

            int bytes = 0;
            while ((bytes = ch.read(buffer)) >= 0) {
                buffer.flip();
                out.write(buffer.array(), buffer.arrayOffset(), bytes);

                buffer.clear();
            }
        } finally {
            IOUtils.closeQuietly(fin);
        }
    }

    @Override
    public void write(OutputStream out, double startAt) throws IOException {
        FlvFile flvFile = null;
        try {
            flvFile = new FlvFile(file());

            ByteBuffer b = flvFile.getFlvHead();
            WritableByteChannel outChannel = Channels.newChannel(out);
            
            // flv head
            outChannel.write(b);
            outChannel.write(ByteBuffer.wrap(ByteBufferUtils.wrapUInt32(b.position())));

            // flv data
            FileChannel fch = flvFile.seek((long)startAt);
            long count = fch.size() - fch.position();
            fch.transferTo(fch.position(), count, outChannel);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            IOUtils.closeQuietly(flvFile);
        }
    }

}
