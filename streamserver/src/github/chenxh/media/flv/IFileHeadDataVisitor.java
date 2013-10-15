package github.chenxh.media.flv;

import java.io.IOException;

import github.chenxh.media.UnsignedDataInput;

public interface IFileHeadDataVisitor extends IDataVisitor {
    /**
     * 读取  FLV 文件头
     * 
     * @param inStream
     * @throws IOException
     */
    public void readFileHeadData(FlvSignature flv, UnsignedDataInput inStream) throws IOException;
}
