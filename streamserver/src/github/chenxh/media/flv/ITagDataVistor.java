package github.chenxh.media.flv;

import github.chenxh.media.UnsignedDataInput;

import java.io.EOFException;
import java.io.IOException;

public interface ITagDataVistor {

    public abstract ITagData readVideoData(ITagHead header, UnsignedDataInput inStream) throws IOException, EOFException;

    public abstract ITagData readAudioData(ITagHead tagHeader, UnsignedDataInput inStream) throws IOException, EOFException;

    public abstract ITagData readScriptData(ITagHead header, UnsignedDataInput inStream) throws IOException, EOFException;

    public abstract ITagData readOtherData(ITagHead header, UnsignedDataInput inStream) throws IOException, EOFException;

    /**
     * �������ǩ֮���Ƿ��жϼ�������
     * 
     * @param tag
     * @return  true ��ʾ�ж� 
     * @throws IOException
     * @throws EOFException
     */
    public abstract boolean interruptAfter(ITag tag) throws IOException, EOFException;
}