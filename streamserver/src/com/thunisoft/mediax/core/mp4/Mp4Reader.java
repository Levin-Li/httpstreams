package com.thunisoft.mediax.core.mp4;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.thunisoft.mediax.core.ByteBufferUtils;

public class Mp4Reader implements Closeable {
    private final FileChannel fch;

    private Box preBox;

    public Mp4Reader(File file) throws IOException {
        fch = new FileInputStream(file).getChannel();
    }
    

    public boolean hasNext() throws IOException{
        if (null != preBox) {
            skipBox(preBox);
        }

        return fch.position() + 1 < fch.size();
    }
    
    public Box next() throws IOException {
        if (null != preBox) {
            skipBox(preBox);
        }

        return preBox = nextBox(fch);
    }

    private Box nextBox(FileChannel fch) throws IOException {
        final long position = fch.position();

        final long boxSize;
        final String type;
        final String extendType;
        final long boxHeadSize;
        
        ByteBuffer buffer = null;
        buffer = ByteBufferUtils.read(fch, 4);
        long rawSize = ByteBufferUtils.readUInt32(buffer);
        
        buffer = ByteBufferUtils.read(fch, 4);
        type = ByteBufferUtils.read4cc(buffer);

        if (rawSize == 1) {
            buffer = ByteBufferUtils.read(fch, 8);
            boxSize = ByteBufferUtils.readUInt64(buffer);
        } else if (rawSize == 0) {
            boxSize = fch.size() - position;
        } else {
            boxSize = rawSize;
        }
        
        if (BoxType.isUUID(type)) {
            buffer = ByteBufferUtils.read(fch, 16);
            extendType = ByteBufferUtils.readString(buffer);
        } else {
            extendType = null;
        }
        
        boxHeadSize = fch.position() - position;
        Box box = newInstance(BoxType.valueOf(type), boxSize, boxHeadSize, extendType);
        box.setFilePosition(position);
        return box;
    }
    
    public void parseBox(Box box) throws IOException {
        fch.position(box.getFilePosition() + box.getBoxHeadSize());
        
        long dataSize = box.getBoxSize() - box.getBoxHeadSize();
        ByteBuffer data = ByteBufferUtils.read(fch, ByteBufferUtils.long2Int(dataSize));
        
        box.parseData(data, this);
    }
    
    public void skipBox(Box box) throws IOException {
        fch.position(box.getFilePosition() + box.getBoxSize());
    }

    public static Box newInstance(BoxType type, long boxSize, long boxHeadSize, String extendType) {
        switch (type) {
            case ftyp :
                return new FtypBox(boxSize, boxHeadSize, type, extendType);
            default :
                return new UnknownBox(boxSize, boxHeadSize, type, extendType);
        }
    }
    
    @Override
    public void close() throws IOException {
        fch.close();
    }

}
