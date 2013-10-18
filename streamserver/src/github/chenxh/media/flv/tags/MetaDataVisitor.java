package github.chenxh.media.flv.tags;

import github.chenxh.media.UnsignedDataInput;
import github.chenxh.media.flv.FlvSignature;
import github.chenxh.media.flv.ITagData;
import github.chenxh.media.flv.ITagHead;
import github.chenxh.media.flv.ITagTrunk;
import github.chenxh.media.flv.script.metadata.FlvMetaData;
import github.chenxh.media.flv.script.metadata.MetaDataDecoder;

import java.io.EOFException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ֻ��ȡ MetaData ��Ϣ
 * 
 * @author chenxh
 * 
 */
public class MetaDataVisitor extends TagDataVistorAdapter {
    // ��ദ��  8MB ��metadata
    private static final int MAX_SCRIPT_DATASIZE = 1024 * 1024 * 8;

    private Logger logger = LoggerFactory.getLogger(getClass());

    private boolean interruptAfterFirstTag;
    private FlvMetaData metaData;
    
    public MetaDataVisitor() {
        this(true);
    }
    
    /**
     * interruptAfterFirstTag=true����ʾ��ȡ��һ�� tag �ļ�¼���жϣ�
     *     ��� flv �ļ����ܱ��ְ� metadata ����ͷ��������Ч�����
     * 
     * interruptAfterFirstTag=false, ��ʾһֱ�����ļ���ֱ������ metadata Tag
     * 
     * @param interruptAfterFirstTag
     */
    public MetaDataVisitor(boolean interruptAfterFirstTag) {
        this.interruptAfterFirstTag = interruptAfterFirstTag;
    }

    @Override
    public ITagData readScriptData(FlvSignature flv, ITagHead header, UnsignedDataInput inStream) throws IOException {
        if (header.getDataSize() > MAX_SCRIPT_DATASIZE){ 
            throw new IllegalArgumentException("ScriptData is too big to analysis, maxScriptSize is " +  MAX_SCRIPT_DATASIZE);
        }

        // �����нű�����ȡ����
        // �����Ȼ����ٶ�����ɺ������ݴ������
        byte[] script = new byte[(int) header.getDataSize()];
        inStream.readFully(script);
        
        // �Խű����ݽ��н���
        metaData = parseMetaData(flv, script);
        metaData.setRawBytes(script);

        return metaData;
    }
    
    private FlvMetaData parseMetaData(FlvSignature flv, byte[] script) throws IOException{
        MetaDataDecoder decoder = new MetaDataDecoder();
        FlvMetaData flvmetaData = decoder.decode(script);
        
        flvmetaData.setRawBytes(script);
        return flvmetaData;
    }


    @Override
    public boolean interruptAfterTag(ITagTrunk tag) throws IOException, EOFException {
        return interruptAfterFirstTag || null != metaData;
    }

    public FlvMetaData getMetaData() {
        return metaData;
    }

}
