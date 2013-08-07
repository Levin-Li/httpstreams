package org.httpstreams.flv.decoder;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;

import org.httpstreams.flv.StructureInputStream;
import org.httpstreams.flv.decoder.tags.FlvTagSupport;


public class FlvTagIterator implements Iterator<FlvTag>, Closeable {
    private FlvTag preTag;
    private StructureInputStream inStream;
    
    private long preTagSize = 0;
    private long filePosition = 0;

    /**
     * @param preTag
     * @param flvBodyStream Flv body
     * @throws IOException 
     */
    public FlvTagIterator(Flv flv, StructureInputStream flvBodyStream) throws IOException {
        this.preTag = new FlvTagSupport(flv);
        
        this.inStream = flvBodyStream;
        filePosition = flv.getHeadLength();
        
        readPreTagSize(flvBodyStream);
    }

    private void readPreTagSize(StructureInputStream inStream) {
        try {
            preTagSize = inStream.readUI32();
            filePosition += 4;
        } catch (IOException e) {
        }
    }

    public boolean hasNext() {
        return !inStream.isEnd();
    }

    public FlvTag next() {
        FlvTag tag = preTag.next(inStream);
        filePosition += tag.getTagSize();
        
        readPreTagSize(inStream);
        return tag;
    }
    
    public void remove() {
        throw new UnsupportedOperationException(
                "unsupported method FlvTagIteroter#remove()");

    }
    public void close() throws IOException {
        inStream.close();
    }

    public void skip(long filePosition) throws IOException {
        inStream.skip(filePosition - this.filePosition);
        this.filePosition = filePosition;
    }

    public final long getPreTagSize() {
        return preTagSize;
    }

    public final StructureInputStream getInputStream() {
        return inStream;
    }
}
