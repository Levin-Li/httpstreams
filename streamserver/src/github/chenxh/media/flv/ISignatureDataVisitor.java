package github.chenxh.media.flv;

import java.io.IOException;

import github.chenxh.media.UnsignedDataInput;

public interface ISignatureDataVisitor {
    /**
     * ��ȡ  FLV �ļ�ͷ����
     * 
     * @param inStream
     * @throws IOException
     */
    public void readSignatureData(FlvSignature flv, UnsignedDataInput inStream) throws IOException;
}
