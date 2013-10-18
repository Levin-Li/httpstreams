package github.chenxh.media.flv.tags;

import java.io.EOFException;
import java.io.IOException;

import github.chenxh.media.UnsignedDataInput;
import github.chenxh.media.flv.FlvSignature;
import github.chenxh.media.flv.ITagTrunk;
import github.chenxh.media.flv.ITagData;
import github.chenxh.media.flv.ITagDataVistor;
import github.chenxh.media.flv.ITagHead;
import github.chenxh.media.flv.impl.TagHeadImpl;

/**
 * 访问者模式的访问元素
 * 
 * @author chenxh
 *
 */
public class TagHeadElement extends TagHeadImpl {
    protected TagHeadElement(){}
    
    public TagHeadElement(int tagType,
            int dataSize,
            long timestamp,
            int streamId) {
        super();
        init(tagType, dataSize, timestamp, streamId);
    }

    /**
     * 
     * @param flv 
     * @param decoder
     * @param dataInput
     * @throws IOException 
     * @throws EOFException 
     */
    public ITagData accept(FlvSignature flv, ITagDataVistor decoder, UnsignedDataInput dataInput) throws EOFException, IOException {
        switch (getTagType()) {
            case ITagTrunk.VIDEO:
                return decoder.readVideoData(flv, this, dataInput);
            case ITagTrunk.AUDIO:
                return decoder.readAudioData(flv, this, dataInput);
            case ITagTrunk.SCRIPT_DATA:
                return decoder.readScriptData(flv, this, dataInput);
            default:
                return decoder.readOtherData(flv, this, dataInput);
        }
    }
}
