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
 * Flv ������
 * 
 * @author chenxh
 * 
 */
public class FlvDecoder {
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * ���ļ����н��룬��ȡ�ļ���Ԫ����
     * 
     * @param inStream
     * @return
     * @throws IOException 
     */
    public FlvMetaData decode(InputStream inStream) throws IOException {
        // ʹ�û���������
        final BufferedInputStream bInStream;
        if (inStream instanceof BufferedInputStream) {
            bInStream = (BufferedInputStream) inStream;
        } else {
            bInStream = new BufferedInputStream(inStream);
        }

        // ���ΰ�װ��ʹ�����ݽṹ
        UnsignedDataInput di = new UnsignedDataInput(bInStream);
        return decode(di);
    }

    /**
     * ��ȡ flv ý����Ϣ��
     * 
     * inStream ��ǰ
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

        // �����������ݣ��ݲ�֧�ֽ�����ֱ������
        skipBodyOf(header, inStream);

        return header;
    }
    
    /**
     * ��ȡ��һ�� Tag ͷ����Ϣ
     * 
     * @param inStream
     * @return null if no another datas
     * @throws IOException 
     */
    private FlvTagImpl readTag(UnsignedDataInput inStream, ITagDataVistor dataVisitor) throws IOException {
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
