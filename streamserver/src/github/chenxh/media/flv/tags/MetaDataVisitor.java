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
 * 只读取 MetaData 信息
 * 
 * @author chenxh
 * 
 */
public class MetaDataVisitor extends TagDataVistorAdapter {
    // 最多处理  8MB 的metadata
    private static final int MAX_SCRIPT_DATASIZE = 1024 * 1024 * 8;

    private Logger logger = LoggerFactory.getLogger(getClass());

    private boolean interruptAfterFirstTag;
    private FlvMetaData metaData;
    
    public MetaDataVisitor() {
        this(true);
    }
    
    /**
     * interruptAfterFirstTag=true，表示读取第一个 tag 的记录就中断，
     *     如果 flv 文件都能保持把 metadata 放在头部，则处理效率最高
     * 
     * interruptAfterFirstTag=false, 表示一直遍历文件，直到出现 metadata Tag
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

        // 把所有脚本都读取出来
        // 避免多度或者少读，造成后面数据处理错误
        byte[] script = new byte[(int) header.getDataSize()];
        inStream.readFully(script);
        
        // 对脚本数据进行解析
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
