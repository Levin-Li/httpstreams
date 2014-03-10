package com.thunisoft.mediax.core.vfs.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.channels.spi.AbstractInterruptibleChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thunisoft.mediax.core.vfs.RandomAccessChannel;
import com.thunisoft.mediax.http.HttpRange;

public class RandomAccessHttpChannelImpl extends AbstractInterruptibleChannel implements
        RandomAccessChannel {
    private static final int RANGE_LENGTH = 1024 * 128;

    private HttpFileObject file;

    private HttpFileSystem fileSystem;
    private HttpRange range;

    private ByteBuffer buffer;

    public RandomAccessHttpChannelImpl(HttpFileObject file, HttpFileSystem fileSystem)
            throws IOException {
        super();
        this.file = file;
        this.fileSystem = fileSystem;
        this.buffer = ByteBuffer.allocate(RANGE_LENGTH);

        updateRangeAndBuffer(0);
    }


    private void updateRangeAndBuffer(long rangeIndex) throws IOException {
        range =
                HttpRange.newInstance(rangeIndex * buffer.capacity(), buffer.capacity(),
                        file.length());

        buffer = fileSystem.getRangeData(file, range);
    }

    @Override
    public long length() throws IOException {
        return file.length();
    }

    @Override
    public long position() throws IOException {
        return range.startPosition() + buffer.position();
    }

    @Override
    public void position(long newPosition) throws IOException {
        if (newPosition >= length() || newPosition < 0) {
            throw new IOException("position [" + newPosition + "] is out of channel boundary");
        }

        // position is in buffer
        if (position() <= newPosition && newPosition < range.startPosition() + buffer.limit()) {
            buffer.position((int) (newPosition - range.startPosition()));
        } else {
            // 新位置不在缓冲区里面，需要重新请求
            updateRangeAndBuffer(newPosition / lengthOfRange());

            // 再次设置位置
            position(newPosition);
        }
    }


    private long lengthOfRange() {
        return range.length();
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(dst.remaining());
        WritableByteChannel sink = Channels.newChannel(outStream);

        long readed = transferTo(position(), dst.remaining(), sink);
        dst.put(outStream.toByteArray(), 0, (int) readed);

        return (int) readed;
    }

    @Override
    public ByteBuffer map(long startPosition, long size) throws IOException {
        long lastPosition = position();
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream((int) size);
            WritableByteChannel sink = Channels.newChannel(outStream);
            transferTo(startPosition, size, sink);

            ByteBuffer rst = ByteBuffer.wrap(outStream.toByteArray(), 0, outStream.size());
            return rst;
        } finally {
            position(lastPosition);
        }
    }

    @Override
    public long transferTo(final long startPosition, long count, WritableByteChannel sink)
            throws IOException {
        position(startPosition);

        long read = 0;
        long remaining = count;
        while (remaining > 0) {
            read = transfer(buffer, count, sink);
            if (buffer.remaining() == 0) {
                position(position());
            }

            remaining -= read;

            if (position() == length()) {
                break;
            }
        }

        return count - remaining;

    }

    private long transfer(ByteBuffer from, long count, WritableByteChannel sink) throws IOException {
        long bytesRead = 0;
        try {
            begin();
            if (from.remaining() > count) {
                ByteBuffer duplicated = from.duplicate();
                duplicated.limit((int) (from.position() + count));
                bytesRead = sink.write(duplicated);
            } else {
                bytesRead = sink.write(from);
            }
        } finally {
            end(bytesRead > 0);
        }

        return bytesRead;
    }

    @Override
    protected void implCloseChannel() throws IOException {
        logger.info("close {}", file);
    }

    private static Logger logger = LoggerFactory.getLogger(RandomAccessHttpChannelImpl.class);
}
