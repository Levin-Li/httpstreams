package org.httpstreams.flv.decoder.tags.data;

import java.io.IOException;

import org.httpstreams.flv.StructureInputStream;


public interface TagData {

    /**
     * 实际被使用的字节数
     * 
     * @param inStream
     * @param packetSize 数据包的大小
     * @throws IOException
     */
    public void read(StructureInputStream inStream, long packetSize) throws IOException;
}