package github.chenxh.media.flv;

import github.chenxh.media.UnsignedDataInput;
import github.chenxh.media.UnsupportMediaTypeException;
import github.chenxh.media.flv.impl.TagHeadImpl;
import github.chenxh.media.flv.impl.TagImpl;
import github.chenxh.media.flv.script.FlvMetaData;
import github.chenxh.media.flv.script.KeyFrames;
import github.chenxh.media.flv.tags.KeyFrameVisitor;
import github.chenxh.media.flv.tags.MetaDataVisitor;
import github.chenxh.media.flv.tags.TagDataVistorAdapter;

import java.io.EOFException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * Flv ������
 * 
 * @author chenxh
 * 
 */
public class FlvDecoder {
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * ��ȡ flv ý����Ϣ��
     * 
     * inStream ��ǰ
     * @param inStream
     * @return
     * @throws IOException
     */
    public FlvMetaData decodeMetaData(UnsignedDataInput inStream) throws IOException {
        MetaDataVisitor metaDataVisitor = new MetaDataVisitor();
        decode(inStream, null, metaDataVisitor);

        return metaDataVisitor.getMetaData();
    }

    /**
     * �Ӹ���  Tag �����ɹؼ�֡
     * 
     * @param inStream
     * @return
     * @throws IOException
     */
    public KeyFrames decodeKeyFrames(UnsignedDataInput inStream) throws IOException  {
        KeyFrameVisitor frameVisitor = new KeyFrameVisitor();

        decode(inStream, null, frameVisitor);
        return frameVisitor.getKeyFrames();
    }
    
    public FlvSignature decode(UnsignedDataInput inStream,
            IFileHeadDataVisitor headDataVisitor, ITagDataVistor tagDataVisitor) throws IOException, UnsupportMediaTypeException, EOFException {

        // ��ȡ  flv ͷ������
        if (null == headDataVisitor) {
            headDataVisitor = defaultFileHeadVisitor;
        }
        FlvSignature header = readFileHead(inStream);
        headDataVisitor.readFileHeadData(header, inStream);
        
        // ����Ҫ��ȡ�Ժ������
        if (null == tagDataVisitor) {
            return header;
        }

        long firstTagSize = inStream.readUI32();
        if (firstTagSize != 0) {
            logger.warn("size of first tag is not 0!");
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
            if (tagDataVisitor.interruptAfter(curTag)) {
                break;
            }
            previousTagSize = inStream.readUI32();
        }while(true);
        return header;
    }
    
    /**
     * ��ȡ flv �ļ���ͷ����Ϣ��
     * @param inStream
     * @return
     * @throws IOException
     * @throws UnsupportMediaTypeException if not actual flv type 
     */
    private FlvSignature readFileHead(UnsignedDataInput inStream) throws IOException, UnsupportMediaTypeException {
        // �ļ�����
        byte[] fileType = new byte[3];
        inStream.read(fileType);
        
        String signature = new String(fileType);
        if (!"FLV".equalsIgnoreCase(signature)) {
            throw new UnsupportMediaTypeException("FLV");
        }

        // �汾������
        int version = inStream.readUI8();
        int typeFlags = inStream.readUI8();
        long headerSize = inStream.readUI32();
        FlvSignature header = new FlvSignature(signature, version, typeFlags, (int)headerSize);

        return header;
    }
    
    /**
     * ��ȡ��һ�� Tag ͷ����Ϣ
     * 
     * @param inStream
     * @param flv TODO
     * @return null if no another datas
     * @throws IOException 
     */
    private TagImpl readTag(UnsignedDataInput inStream, ITagDataVistor dataVisitor, FlvSignature flv) throws IOException {
        int tagType = inStream.read();
        if (-1 == tagType) { // �Ѿ����ļ�ĩβ��
            return  null;
        }
        
        // read tag head
        int dataSize = inStream.readUI24();
        int timestamp = inStream.readUI24();
        int timstampExtended = inStream.readUI8();
        int streamId = inStream.readUI24();
        long realTimestamp = ((long)timstampExtended << 24) | timestamp;
        TagHeadImpl head = new TagHeadImpl(tagType, dataSize, realTimestamp, streamId);

        // read tag data
        ITagData data = head.readData(flv, dataVisitor, inStream);

        return new TagImpl(head, data);
    }

    private static final class DefaultFileHeadVisitor implements IFileHeadDataVisitor {
        @Override
        public void readFileHeadData(FlvSignature flv, UnsignedDataInput inStream) throws IOException {
            if (flv.getDataSize() > 0) { 
                long bodysize = flv.getDataSize();
                inStream.skipBytes(bodysize);
            }
        }
    }
    private static final IFileHeadDataVisitor defaultFileHeadVisitor = new  DefaultFileHeadVisitor();
    
    
    
    private static final class DefaultTagDataVisitor extends TagDataVistorAdapter {
        @Override
        public boolean interruptAfter(ITagTrunk tag) throws IOException, EOFException {
            return true;
        }
    }
    
    
}
