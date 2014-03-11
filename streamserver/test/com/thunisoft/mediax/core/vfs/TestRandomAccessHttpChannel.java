package com.thunisoft.mediax.core.vfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;

import com.thunisoft.mediax.core.vfs.RandomAccessChannel;
import com.thunisoft.mediax.core.vfs.VFS;
import com.thunisoft.mediax.core.vfs.http.RandomAccessHttpChannelImpl;

public class TestRandomAccessHttpChannel extends TestCase {
    RandomAccessChannel rch;
    FileChannel fch;

    @Override
    protected void setUp() throws Exception {
        String localUrl = "http://localhost:8080/streamer/mp4/test1.mp4";
        rch = VFS.getRandomAccessChannel(localUrl);

        File file = new File(
                "E:/programs/Workspaces/MyEclipse 8.6/streamer/web/mp4/test1.mp4");
        fch = new FileInputStream(file).getChannel();
    }

    public void testReadByteBuffer() throws IOException {
        ByteBuffer b1 = ByteBuffer
                .allocate(RandomAccessHttpChannelImpl.RANGE_LENGTH * 2 - 5);
        ByteBuffer b2 = ByteBuffer
                .allocate(RandomAccessHttpChannelImpl.RANGE_LENGTH * 2 - 5);

        // at start
        for (int i = 0; i < 10; i++) {
            fch.position(i * RandomAccessHttpChannelImpl.RANGE_LENGTH + 4);
            rch.position(i * RandomAccessHttpChannelImpl.RANGE_LENGTH + 4);

            b1.clear();
            b2.clear();

            int rst1 = fch.read(b1);
            int rst2 = rch.read(b2);
            assertEquals(rst1, rst2);
            assertEquals(fch.position(), rch.position());
            assertEquals(b1, b2);
        }

        // at end side
        for (long i = fch.size() - 5; i < fch.size(); i++) {
            fch.position(i);
            rch.position(i);

            b1.clear();
            b2.clear();

            int rst1 = fch.read(b1);
            int rst2 = rch.read(b2);
            assertEquals(rst1, rst2);
            assertEquals(fch.position(), rch.position());
            assertEquals(b1, b2);
        }
    }
    
    public void testReadByteBuffer_01() throws IOException {
        ByteBuffer b1 = ByteBuffer
                .allocate(RandomAccessHttpChannelImpl.RANGE_LENGTH * 2 - 5);
        ByteBuffer b2 = ByteBuffer
                .allocate(RandomAccessHttpChannelImpl.RANGE_LENGTH * 2 - 5);

        // at start
        for (int i = 0; i < 10; i++) {
            fch.position(i);
            rch.position(i);

            b1.clear();
            b2.clear();
            b1.put((byte)1);
            b2.put((byte)1);

            int rst1 = fch.read(b1);
            int rst2 = rch.read(b2);
            assertEquals(rst1, rst2);
            assertEquals(fch.position(), rch.position());
            assertEquals(b1, b2);
        }

        // at end side
        for (long i = fch.size() - 5; i < fch.size(); i++) {
            fch.position(i);
            rch.position(i);

            b1.clear();
            b2.clear();
            b1.put((byte)1);
            b2.put((byte)1);

            int rst1 = fch.read(b1);
            int rst2 = rch.read(b2);
            assertEquals(rst1, rst2);
            assertEquals(fch.position(), rch.position());
            assertEquals(b1, b2);
        }
    }

    public void testMap() throws IOException {
        int size = RandomAccessHttpChannelImpl.RANGE_LENGTH + 1;

        // at start
        for (int i = 0; i < 10; i++) {
            fch.position(i);
            rch.position(i);

            ByteBuffer b1 = fch.map(MapMode.READ_ONLY, i, size);
            ByteBuffer b2 = rch.map(i, size);

            assertEquals(fch.position(), rch.position());
            assertEquals(b1, b2);
        }

        // at end side
        for (long i = fch.size() - size - 10; i < fch.size() - size; i++) {
            fch.position(i);
            rch.position(i);

            ByteBuffer b1 = fch.map(MapMode.READ_ONLY, i, size);
            ByteBuffer b2 = rch.map(i, size);

            assertEquals(fch.position(), rch.position());
            assertEquals(b1, b2);
        }
    }

    public void testTransferTo() {

    }

    @Override
    protected void tearDown() throws Exception {

        IOUtils.closeQuietly(rch);
        IOUtils.closeQuietly(fch);
    }
}
