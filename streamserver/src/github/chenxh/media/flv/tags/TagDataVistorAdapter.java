package github.chenxh.media.flv.tags;

import java.io.EOFException;
import java.io.IOException;

import github.chenxh.media.UnsignedDataInput;
import github.chenxh.media.flv.ITag;
import github.chenxh.media.flv.ITagData;
import github.chenxh.media.flv.ITagDataVistor;
import github.chenxh.media.flv.ITagHead;

public class TagDataVistorAdapter  implements ITagDataVistor {

    public TagDataVistorAdapter() {
        super();
    }

    @Override
    public ITagData readScriptData(ITagHead header, UnsignedDataInput inStream) throws IOException {
        return skipDatas(header, inStream);
    }

    @Override
    public ITagData readAudioData(ITagHead header, UnsignedDataInput inStream) throws IOException {
        return skipDatas(header, inStream);
    }

    @Override
    public ITagData readOtherData(ITagHead header, UnsignedDataInput inStream) throws IOException {
        return skipDatas(header, inStream);
    }

    @Override
    public ITagData readVideoData(ITagHead header, UnsignedDataInput inStream) throws IOException {
        return skipDatas(header, inStream);
    }

    /**
     * ºöÂÔÄÚÈÝ
     * 
     * @param header
     * @param inStream
     * @return
     * @throws IOException
     */
    private ITagData skipDatas(ITagHead header, UnsignedDataInput inStream) throws IOException {
        inStream.skipBytes(header.getBodySize());
        return null;
    }

    @Override
    public boolean interruptAfter(ITag tag) throws IOException, EOFException {
        return false;
    }

}