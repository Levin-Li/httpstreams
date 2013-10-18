package github.chenxh.media.flv.tags;

import github.chenxh.media.UnsignedDataInput;
import github.chenxh.media.flv.FlvSignature;
import github.chenxh.media.flv.ITagData;
import github.chenxh.media.flv.ITagHead;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class TagHeadVisitor extends TagDataVistorAdapter {
    private List<ITagHead> tagHeads = new LinkedList<ITagHead>();

    @Override
    public ITagData readAudioData(FlvSignature flv, ITagHead header,
            UnsignedDataInput inStream) throws IOException {
        tagHeads.add(header);
        return super.readAudioData(flv, header, inStream);
    }

    @Override
    public ITagData readOtherData(FlvSignature flv, ITagHead header,
            UnsignedDataInput inStream) throws IOException {
        tagHeads.add(header);
        return super.readOtherData(flv, header, inStream);
    }

    @Override
    public ITagData readScriptData(FlvSignature flv, ITagHead header,
            UnsignedDataInput inStream) throws IOException {
        tagHeads.add(header);
        return super.readScriptData(flv, header, inStream);
    }

    @Override
    public ITagData readVideoData(FlvSignature signature, ITagHead header,
            UnsignedDataInput inStream) throws IOException {
        tagHeads.add(header);
        return super.readVideoData(signature, header, inStream);
    }

    public List<ITagHead> getTagHeads() {
        return tagHeads;
    }
    
    
}
