package com.thunisoft.mediax.core.vfs.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.WritableByteChannel;
import java.nio.channels.spi.AbstractInterruptibleChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thunisoft.mediax.core.vfs.RandomAccessChannel;
import com.thunisoft.mediax.http.HttpRange;

public class RandomAccessHttpChannelImpl extends AbstractInterruptibleChannel implements
        RandomAccessChannel {
    public static final int RANGE_LENGTH = 1024 * 128;

    private HttpFileObject file;

    private long position;

    private HttpFileSystem fileSystem;
    final private HttpRange range;

    private ByteBuffer buffer;

    public RandomAccessHttpChannelImpl(HttpFileObject file, HttpFileSystem fileSystem)
            throws IOException {
        super();
        this.file = file;
        this.fileSystem = fileSystem;

        range = HttpRange.newInstance(0, lengthOfRange(), file.length());
        buffer = fileSystem.getRangeData(file, range);
    }

    @Override
    public long length() {
        return file.length();
    }

    @Override
    public long position() {
        return position;
    }

    @Override
    public void position(long newPosition) throws IOException {
        if (newPosition < 0) {
            throw new IllegalArgumentException("new position is negative!");
        }

        if (!isOpen()) {
            throw new ClosedChannelException();
        }

        position = newPosition;

        // position is in buffer
        if (!range.include(newPosition)) {
            range.setStart(newPosition - newPosition % range.length());
            buffer = fileSystem.getRangeData(file, range);
        }

        // update buffer position
        long bufferPosition = newPosition - range.startPosition();
        if (bufferPosition <= buffer.limit()) {
            buffer.position((int)bufferPosition);
        } else {
            throw new IOException("position [" + newPosition + "]不存在!");
        }
    }

    private long lengthOfRange() {
        return RANGE_LENGTH;
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        return readFull(dst);
    }

    public int readFull(ByteBuffer dst) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(dst.remaining());
        WritableByteChannel sink = Channels.newChannel(outStream);

        do {
            long readed = transferTo(position(), dst.remaining(), sink);
            if (readed < 0) {
                break;
            }
        } while (dst.remaining() > 0);

        dst.put(outStream.toByteArray(), 0, (int) outStream.size());
        return outStream.size() > 0 ? outStream.size() : (isEOF() ? -1 : 0);
    }

    @Override
    public ByteBuffer map(long startPosition, long size) throws IOException {
        long lastPosition = position();
        try {
            if (startPosition + size > length()) {
                throw new IOException("拒绝访问!");
            }

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

        if (isEOF()) {
            return -1;
        }

        long read = 0;
        long remaining = count;
        while (remaining > 0 && isNotEOF()) {
            read = copy(buffer, remaining, sink);

            position(position + read);
            remaining -= read;

            if (read < 1) {
                break;
            }
        }

        return count - remaining;

    }

    private long copy(ByteBuffer from, long count, WritableByteChannel sink) throws IOException {
        long bytesRead = 0;
        try {
            begin();
            if (from.remaining() <= count) {
                bytesRead = sink.write(from);
            } else {
                ByteBuffer duplicated = from.duplicate();
                duplicated.limit((int) (from.position() + count));
                bytesRead = sink.write(duplicated);
            }
        } finally {
            end(bytesRead > 0);
        }

        return bytesRead;
    }

    private boolean isNotEOF() {
        return (position < length());
    }

    private boolean isEOF() {
        return !isNotEOF();
    }

    @Override
    protected void implCloseChannel() throws IOException {
        logger.info("close {}", file);
    }


    @Override
    public String toString() {
        return "RandomAccessHttpChannelImpl {position:" + position() + ", length:" + length()
                + ", range: " + range + "}";
    }

    private static Logger logger = LoggerFactory.getLogger(RandomAccessHttpChannelImpl.class);
}
