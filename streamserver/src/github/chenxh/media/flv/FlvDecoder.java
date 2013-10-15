package github.chenxh.media.flv;

import github.chenxh.media.UnsignedDataInput;
import github.chenxh.media.UnsupportMediaTypeException;
import github.chenxh.media.flv.impl.FlvTagHead;
import github.chenxh.media.flv.impl.FlvTagImpl;
import github.chenxh.media.flv.script.FlvMetaData;
import github.chenxh.media.flv.tags.MetaDataVisitor;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * Flv 解码器
 * 
 * @author chenxh
 * 
 */
public class FlvDecoder {
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 对文件进行解码，获取文件的元数据
     * 
     * @param inStream
     * @return
     * @throws IOException 
     */
    public FlvMetaData decode(InputStream inStream) throws IOException {
        // 使用缓冲输入流
        final BufferedInputStream bInStream;
        if (inStream instanceof BufferedInputStream) {
            bInStream = (BufferedInputStream) inStream;
        } else {
            bInStream = new BufferedInputStream(inStream);
        }

        // 二次包装，使用数据结构
        UnsignedDataInput di = new UnsignedDataInput(bInStream);
        return decode(di);
    }

    /**
     * 读取 flv 媒体信息，
     * 
     * inStream 当前
     * @param inStream
     * @return
     * @throws IOException
     */
    public FlvMetaData decode(UnsignedDataInput inStream) throws IOException {
        MetaDataVisitor metaDataVisitor = new MetaDataVisitor();
        decode(inStream, metaDataVisitor);

        return metaDataVisitor.getMetaData();
    }

    public FlvSignature decode(UnsignedDataInput inStream, ITagDataVistor tagDataVisitor) throws IOException {
        FlvSignature header = readFileHead(inStream);
        long firstTagSize = inStream.readUI32();
        if (firstTagSize != 0) {
            logger.warn("size of first tag is not 0!");
        }

        long previousTagSize = firstTagSize;
        FlvTagImpl curTag = null;
        do {
            // read tag
            curTag = readTag(inStream, tagDataVisitor);
            if (null == curTag) {
                break;
            }

            // previous tag size, and update this tag size as preTagSize
            curTag.setPreTagSize(previousTagSize);
            
            // 
            if (tagDataVisitor.interruptAfter(curTag)) {
                break;
            }
            previousTagSize = inStream.readUI32();
        }while(true);
        
        return header;
    }
    
    /**
     * 读取 flv 文件的头部信息，
     * @param inStream
     * @return
     * @throws IOException
     * @throws UnsupportMediaTypeException if not actual flv type 
     */
    private FlvSignature readFileHead(UnsignedDataInput inStream) throws IOException, UnsupportMediaTypeException {
        // 文件类型
        byte[] fileType = new byte[3];
        inStream.read(fileType);
        
        String signature = new String(fileType);
        if (!"FLV".equalsIgnoreCase(signature)) {
            throw new UnsupportMediaTypeException("FLV");
        }

        // 版本和内容
        int version = inStream.readUI8();
        int typeFlags = inStream.readUI8();
        long headerSize = inStream.readUI32();
        FlvSignature header = new FlvSignature(signature, version, typeFlags, (int)headerSize);

        // 还有其他内容，暂不支持解析，直接跳过
        skipBodyOf(header, inStream);

        return header;
    }
    
    /**
     * 读取下一个 Tag 头部信息
     * 
     * @param inStream
     * @return null if no another datas
     * @throws IOException 
     */
    private FlvTagImpl readTag(UnsignedDataInput inStream, ITagDataVistor dataVisitor) throws IOException {
        int tagType = inStream.read();
        if (-1 == tagType) { // 已经到文件末尾了
            return  null;
        }
        
        // read tag head
        int dataSize = inStream.readUI24();
        int timestamp = inStream.readUI24();
        int timstampExtended = inStream.readUI8();
        int streamId = inStream.readUI24();
        long realTimestamp = ((long)timstampExtended << 24) | timestamp;
        FlvTagHead head = new FlvTagHead(tagType, dataSize, realTimestamp, streamId);

        // read tag data
        ITagData data = head.readData(dataVisitor, inStream);

        return new FlvTagImpl(head, data);
    }

    private static void skipBodyOf(ITagHead tag, UnsignedDataInput inStream) throws IOException {
        if (tag.getBodySize() > 0) { 
            long bodysize = tag.getBodySize();
            inStream.skipBytes(bodysize);
        }
    }
    
}
