package github.chenxh.media.flv;

import github.chenxh.media.IDataTrunk;
import github.chenxh.media.UnsignedDataInput;

import java.io.EOFException;
import java.io.IOException;

public interface ITagDataVistor {

    public abstract ITagData readVideoData(FlvSignature flv, ITagHead header, UnsignedDataInput inStream) throws IOException, EOFException;

    public abstract ITagData readAudioData(FlvSignature flv, IDataTrunk tagHeader, UnsignedDataInput inStream) throws IOException, EOFException;

    public abstract ITagData readScriptData(FlvSignature flv, IDataTrunk header, UnsignedDataInput inStream) throws IOException, EOFException;

    public abstract ITagData readOtherData(FlvSignature flv, IDataTrunk header, UnsignedDataInput inStream) throws IOException, EOFException;

    /**
     * �������ǩ֮���Ƿ��жϼ�������
     * 
     * @param tag
     * @return  true ��ʾ�ж� 
     * @throws IOException
     * @throws EOFException
     */
    public abstract boolean interruptAfterTag(ITagTrunk tag) throws IOException, EOFException;

    public boolean interruptAfterSignature(FlvSignature signature) throws IOException, EOFException;
}