package github.chenxh.media.flv;

import github.chenxh.media.UnsignedDataInput;
import github.chenxh.media.UnsupportMediaTypeException;
import github.chenxh.media.flv.script.metadata.FlvMetaData;
import github.chenxh.media.flv.script.metadata.KeyFrames;
import github.chenxh.media.flv.struct.TagImpl;
import github.chenxh.media.flv.tags.KeyFrameVisitor;
import github.chenxh.media.flv.tags.MetaDataVisitor;
import github.chenxh.media.flv.tags.TagHeadElement;

import java.io.EOFException;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
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
     * 读取 flv 媒体信息，
     * 
     * inStream 当前
     * @param inStream
     * @return
     * @throws IOException
     */
    public FlvMetaData decodeMetaData(UnsignedDataInput inStream) throws IOException {
        MetaDataVisitor metaDataVisitor = new MetaDataVisitor();
        
        try {
            decode(inStream, null, metaDataVisitor);
            
            inStream.close();
        } catch (Exception e) {
            logger.warn("解析 metadata 错误，原因:" + e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(inStream);
        }

        return metaDataVisitor.getMetaData();
    }

    /**
     * 从各个  Tag 中生成关键帧
     * 
     * @param inStream
     * @return
     * @throws IOException
     */
    public KeyFrames decodeKeyFrames(UnsignedDataInput inStream) throws IOException  {
        KeyFrameVisitor frameVisitor = new KeyFrameVisitor();

        try {
            decode(inStream, null, frameVisitor);
            
            inStream.close();
        } catch (Exception e) {
            logger.warn("解析关键帧错误，原因:" + e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(inStream);
        }

        return frameVisitor.getKeyFrames();
    }
    
    public FlvSignature decode(UnsignedDataInput inStream,
            ISignatureDataVisitor signatureDataVisitor, ITagDataVistor tagDataVisitor) throws IOException, UnsupportMediaTypeException, EOFException {

        // 读取  flv 头部内容
        if (null == signatureDataVisitor) {
            signatureDataVisitor = defaultFileHeadVisitor;
        }
        FlvSignature header = readFileHead(inStream);
        signatureDataVisitor.readSignatureData(header, inStream);

        // 不需要读取以后的内容
        if (null == tagDataVisitor
                || tagDataVisitor.interruptAfterSignature(header)) {
            return header;
        }

        long firstTagSize = inStream.readUI32();
        if (firstTagSize != 0) {
            logger.warn("size of first tag is {}, normal is 0!", firstTagSize);
        }

        long previousTagSize = firstTagSize;
        TagImpl curTag = null;
        do {
            // read tag
            curTag = readTag(inStream, tagDataVisitor, header);
            if (null == curTag) {
                break;
            }

            // previous tag size, and update this tag size as preTagSize
            curTag.setPreTagSize(previousTagSize);
            
            // 
            if (tagDataVisitor.interruptAfterTag(curTag)) {
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

        return header;
    }
    
    /**
     * 读取下一个 Tag 头部信息
     * 
     * @param inStream
     * @param flv TODO
     * @return null if no another datas
     * @throws IOException 
     */
    private TagImpl readTag(UnsignedDataInput inStream, ITagDataVistor dataVisitor, FlvSignature flv) throws IOException {
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
        TagHeadElement head = new TagHeadElement(tagType, dataSize, realTimestamp, streamId);

        // read tag data
        ITagData data = head.accept(flv, dataVisitor, inStream);

        return new TagImpl(head, data);
    }

    private static final class DefaultFileHeadVisitor implements ISignatureDataVisitor {
        @Override
        public void readSignatureData(FlvSignature flv, UnsignedDataInput inStream) throws IOException {
            if (flv.getDataSize() > 0) { 
                long bodysize = flv.getDataSize();
                inStream.skipBytes(bodysize);
            }
        }
    }
    private static final ISignatureDataVisitor defaultFileHeadVisitor = new  DefaultFileHeadVisitor();
}
