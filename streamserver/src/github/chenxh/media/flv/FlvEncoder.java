package github.chenxh.media.flv;

import github.chenxh.media.UnsignedDataOutput;
import github.chenxh.media.flv.script.metadata.FlvMetaData;
import github.chenxh.media.flv.script.metadata.MetaDataEncoder;
import github.chenxh.media.flv.struct.TagHeadImpl;

import java.io.ByteArrayOutputStream;
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
    public byte[] encodeMetaDataTag(FlvMetaData metaData) throws IOException{
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        UnsignedDataOutput output = new UnsignedDataOutput(byteStream);
        byte[] bytes = new MetaDataEncoder().encode(metaData);

        TagHeadImpl head = new TagHeadImpl();
        head.init(ITagTrunk.SCRIPT_DATA, bytes.length, 0, 0);
        
        encodeTag(head, bytes, output);

        return byteStream.toByteArray();
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
    
    public int encodePreTagSize (long tagSize, UnsignedDataOutput output) throws IOException {
        output.writeUI32(tagSize);
        
        return 4;
    }
    
    public int encodeTag(ITagHead head, byte[] bytes, UnsignedDataOutput output) throws IOException{
        output.resetWriten();

        // head
        encodeTagHead(head, output);
        
        // Data
        encodeTagData(head, bytes, output);
        
        return (int)(output.writen());
    }

    
    public void encodeTagHead(ITagHead head, UnsignedDataOutput output) throws IOException {

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
    
    public void encodeTagData(ITagHead head, byte[] data, UnsignedDataOutput output) throws IOException {
        if (data.length >= head.getDataSize()) {
            output.write(data, 0, (int)head.getDataSize());
        } else {
            output.write(data);
        }
    }
}
