package github.chenxh.media.flv;

import github.chenxh.media.IDataTrunk;
import github.chenxh.media.UnsignedDataInput;

import java.io.EOFException;
import java.io.IOException;

public interface ITagDataVistor  extends IDataVisitor{

    public abstract ITagData readVideoData(ITagHead header, UnsignedDataInput inStream) throws IOException, EOFException;

    public abstract ITagData readAudioData(IDataTrunk tagHeader, UnsignedDataInput inStream) throws IOException, EOFException;

    public abstract ITagData readScriptData(IDataTrunk header, UnsignedDataInput inStream) throws IOException, EOFException;

    public abstract ITagData readOtherData(IDataTrunk header, UnsignedDataInput inStream) throws IOException, EOFException;

    /**
     * �������ǩ֮���Ƿ��жϼ�������
     * 
     * @param tag
     * @return  true ��ʾ�ж� 
     * @throws IOException
     * @throws EOFException
     */
    public abstract boolean interruptAfter(ITagTrunk tag) throws IOException, EOFException;
}