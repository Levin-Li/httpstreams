package com.thunisoft.mediax.core.codec.flv;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;

import org.slf4j.LoggerFactory;

import com.thunisoft.mediax.core.utils.ByteUtils;
import com.thunisoft.mediax.core.vfs.RandomAccessChannel;
import com.thunisoft.mediax.core.vfs.local.RandomAccessFileChannelImpl;

public class FlvIterator implements Iterator<ByteBuffer>, Closeable {
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(FlvIterator.class);
    private RandomAccessChannel channel;


    final private ByteBuffer fileHead;
    
    final private ByteBuffer preTagSize = ByteBuffer.allocate(4);
    private ByteBuffer preTag = ByteBuffer.allocate(FlvConsts.TAG_HEAD_LENGTH);

    public FlvIterator(RandomAccessChannel ch) throws IOException {
        this.channel = ch;

        this.fileHead = readFileHeader(ch);
    }
    
    public FlvIterator(File file) throws IOException {
        this(new RandomAccessFileChannelImpl(file));
    }

    @Override
    public boolean hasNext() {
        try {
            if (channel.isOpen()) {
                return channel.position() + (FlvConsts.PRETAG_LENGTH + FlvConsts.TAG_HEAD_LENGTH) < channel
                                                                                       .length();
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public ByteBuffer next() {
        try {
            int preSize = preTagSize(channel);
            logger.debug("preTagSize: {}", preSize);

            // next data
            logger.debug("tagPosition: {}", channel.position());
            ByteBuffer tagData = readTag(channel);

            return tagData;
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }


    private ByteBuffer readFileHeader(RandomAccessChannel ch) throws IOException {
        long startPosition = ch.position();

        ByteBuffer bytes = ByteBuffer.allocate(9);
        ch.read(bytes);
        bytes.flip();
        String typeFlag = ByteUtils.read3cc(bytes);
        if (!"flv".equalsIgnoreCase(typeFlag)) {
            throw new IOException("It's not flv file");
        }

        int version = ByteUtils.readUInt8(bytes);
        int flags = ByteUtils.readUInt8(bytes);
        long headSize = ByteUtils.readUInt32(bytes);


        // head
        ch.position(startPosition);
        ByteBuffer content = ByteBuffer.allocate(ByteUtils.long2Int(headSize));
        ch.readFull(content);

        content.flip();
        return content;
    }


    private int preTagSize(RandomAccessChannel ch) throws IOException {
        ByteBuffer data = clearAndReadFull(ch, preTagSize, preTagSize.capacity());
        return data.getInt();
    }

    private ByteBuffer readTag(RandomAccessChannel ch) throws IOException {
        final long startPosition = ch.position();
        
        if (preTag.capacity() < FlvConsts.TAG_HEAD_LENGTH) {
            preTag = ByteBuffer.allocate(FlvConsts.TAG_HEAD_LENGTH);
        } else {
            preTag.clear();
        }

        // tag head
        ByteBuffer tagHead =(ByteBuffer)preTag.limit(FlvConsts.TAG_HEAD_LENGTH);
        ch.read(tagHead);
        tagHead.flip();
        int type = ByteUtils.readUInt8(tagHead);
        int dataSize = ByteUtils.readUInt24(tagHead);

        // clear tag, tagSize = headsize + datasize
        int tagSize = FlvConsts.TAG_HEAD_LENGTH + dataSize;
        if (preTag.capacity() < tagSize) {
            preTag = ByteBuffer.allocate(tagSize);
        }
        
        // back to start of tag
        ch.position(startPosition);
        return clearAndReadFull(ch, preTag, tagSize);
    }

    private ByteBuffer clearAndReadFull(RandomAccessChannel ch, ByteBuffer buffer, int limit) throws IOException {
        buffer.clear();
        buffer.limit(limit);
        
        ch.readFull(buffer);
        
        buffer.flip();
        
        return buffer;
    }
    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove()");
    }

    public ByteBuffer getFileHead() {
        return fileHead;
    }
    
    @Override
    public void close() throws IOException {
        channel.close();
    }
}
