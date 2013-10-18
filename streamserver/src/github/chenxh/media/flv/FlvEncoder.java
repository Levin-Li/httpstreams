package github.chenxh.media.flv;

import github.chenxh.media.UnsignedDataOutput;
import github.chenxh.media.flv.impl.TagHeadImpl;
import github.chenxh.media.flv.script.FlvMetaData;

import java.io.IOException;

public class FlvEncoder {
    /**
     * byte
     * @param signature
     * @return  how many bytes output
     * @throws IOException 
     */
    public void encodeSignature(FlvSignature signature, UnsignedDataOutput output) throws IOException {
        // FLV
        output.writeUI8((int)'F');
        output.writeUI8((int)'L');
        output.writeUI8((int)'V');
        
        // version
        output.writeUI8(signature.getVersion());
        
        // typeFlags
        output.writeUI8(signature.getTypeFlags());
        
        // header size
        output.writeUI32(FlvSignature.MIN_HEAD_SIZE);
    }
    
    public int encodeTag(FlvMetaData metaData, UnsignedDataOutput output) throws IOException{
        byte[] bytes = metaData.getRawBytes();

        TagHeadImpl head = new TagHeadImpl();
        head.init(ITagTrunk.SCRIPT_DATA, bytes.length, 0, 0);
        
        return encodeTag(head, bytes, output);
    }
    
    public int encodeTag(ITagHead head, byte[] bytes, UnsignedDataOutput output) throws IOException{
        long start = output.writen();

        // TagType
        output.writeUI8(head.getTagType());
        
        // DataSize
        output.writeUI24(head.getDataSize());
        
        // timstamp
        output.writeUI24(head.getTimestamp());
        output.writeUI8((head.getTimestamp() >> 24));
        
        // streamId
        output.writeUI24(head.getStreamId());
        
        // Data
        output.write(bytes);
        
        return (int)(output.writen() - start);
    }
}
