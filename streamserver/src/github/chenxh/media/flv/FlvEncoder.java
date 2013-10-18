package github.chenxh.media.flv;

import github.chenxh.media.UnsignedDataOutput;
import github.chenxh.media.flv.script.metadata.FlvMetaData;
import github.chenxh.media.flv.script.metadata.MetaDataEncoder;
import github.chenxh.media.flv.struct.TagHeadImpl;

import java.io.IOException;

public class FlvEncoder {
    /**
     * 写出 flv 文件头
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
    
    /**
     * 写出 scriptdata， 内容为 metadata 信息
     * 
     * @param metaData
     * @param output
     * @return
     * @throws IOException
     */
    public int encodeMetaData(FlvMetaData metaData, UnsignedDataOutput output) throws IOException{
        byte[] bytes = new MetaDataEncoder().encode(metaData);

        TagHeadImpl head = new TagHeadImpl();
        head.init(ITagTrunk.SCRIPT_DATA, bytes.length, 0, 0);
        
        return encodeTag(head, bytes, output);
    }
    
    public int encodeTag(ITagHead head, byte[] bytes, UnsignedDataOutput output) throws IOException{
        long start = output.writen();
        encodeTagHead(head, output);
        
        // Data
        output.write(bytes);
        
        return (int)(output.writen() - start);
    }

    private void encodeTagHead(ITagHead head, UnsignedDataOutput output) throws IOException {

        // TagType
        output.writeUI8(head.getTagType());
        
        // DataSize
        output.writeUI24(head.getDataSize());
        
        // timstamp
        output.writeUI24(head.getTimestamp());
        output.writeUI8((head.getTimestamp() >> 24));
        
        // streamId
        output.writeUI24(head.getStreamId());
    }
}
