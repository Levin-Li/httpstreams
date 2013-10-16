package github.chenxh.media.flv;

import java.io.IOException;

import github.chenxh.media.UnsignedDataInput;

public interface ISignatureDataVisitor {
    /**
     * 读取  FLV 文件头内容
     * 
     * @param inStream
     * @throws IOException
     */
    public void readSignatureData(FlvSignature flv, UnsignedDataInput inStream) throws IOException;
}
