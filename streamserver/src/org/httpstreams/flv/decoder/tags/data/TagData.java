package org.httpstreams.flv.decoder.tags.data;

import java.io.IOException;

import org.httpstreams.flv.StructureInputStream;


public interface TagData {

    /**
     * ʵ�ʱ�ʹ�õ��ֽ���
     * 
     * @param inStream
     * @param packetSize ���ݰ��Ĵ�С
     * @throws IOException
     */
    public void read(StructureInputStream inStream, long packetSize) throws IOException;
}