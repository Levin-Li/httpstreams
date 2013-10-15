package github.chenxh.media.flv.tags;

import java.io.EOFException;
import java.io.IOException;

import github.chenxh.media.IDataTrunk;
import github.chenxh.media.UnsignedDataInput;
import github.chenxh.media.flv.ITagTrunk;
import github.chenxh.media.flv.ITagData;
import github.chenxh.media.flv.ITagDataVistor;
import github.chenxh.media.flv.ITagHead;

public class TagDataVistorAdapter  implements ITagDataVistor {

    public TagDataVistorAdapter() {
        super();
    }

    @Override
    public ITagData readScriptData(IDataTrunk header, UnsignedDataInput inStream) throws IOException {
        return skipDatas(header, inStream);
    }

    @Override
    public ITagData readAudioData(IDataTrunk header, UnsignedDataInput inStream) throws IOException {
        return skipDatas(header, inStream);
    }

    @Override
    public ITagData readOtherData(IDataTrunk header, UnsignedDataInput inStream) throws IOException {
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
    private ITagData skipDatas(IDataTrunk header, UnsignedDataInput inStream) throws IOException {
        inStream.skipBytes(header.getDataSize());
        return null;
    }

    @Override
    public boolean interruptAfter(ITagTrunk tag) throws IOException, EOFException {
        return false;
    }

}