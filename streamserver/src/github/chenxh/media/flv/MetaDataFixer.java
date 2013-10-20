package github.chenxh.media.flv;

import github.chenxh.media.UnsignedDataInput;
import github.chenxh.media.UnsignedDataOutput;
import github.chenxh.media.UnsupportMediaTypeException;
import github.chenxh.media.flv.script.metadata.FlvMetaData;
import github.chenxh.media.flv.script.metadata.KeyFrames;
import github.chenxh.media.flv.script.metadata.MetaDataDecoder;
import github.chenxh.media.flv.tags.TagDataVistorAdapter;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

/**
 * flv 文件metadata修复工具
 * 
 * @author xiuheng chen chenxiuheng@gmail.com
 * 
 */
public class MetaDataFixer {

    private FlvEncoder encoder = new FlvEncoder();

    /**
     * 把本地文件修复成目标文件
     * 
     * @param source
     * @param dest
     * @throws IOException
     */
    public void fix(File source, File dest) throws IOException {
        
        File tempFile = new File(Math.random() + ".flv");
        
        // metadata 可能在文件的任何一个位置，
        // 所以先遍历一遍文件，单独把 metadata tag 从文件列表中拿出来
        FlvMetaData metaData = copyIgnoreMetedata(source, tempFile);
        
        
        // 从没有metadata的新文件中，获取 keyframes
        KeyFrames keyFrames = getKeyFrames(tempFile);

        
        // 把metadata编码，重新校正 keyframes 中的文件索引
        // 校正算法：metadata Tag size + preTagSize(4)
        keyFrames.updateKeyFrames(metaData);
        byte[] metaDataBytes = encoder.encodeMetaDataTag(metaData);
        keyFrames.moveFilePosition(metaDataBytes.length + 4);
        
        // 以文件头+metadata+tag[] 的顺序输出 flv 文件
        write2NewFile(tempFile, dest, metaData);

        // 删除临时文件
        tempFile.delete();
    }

    private FlvMetaData copyIgnoreMetedata(File src, File dest)
            throws IOException {
        boolean ignoreMetaDataTag = true;
        boolean ignoreCopyFlvSignature = false;

        return copy(src, dest, ignoreMetaDataTag, ignoreCopyFlvSignature);
    }

    private KeyFrames getKeyFrames(File tempFile) throws IOException {
        return new FlvDecoder().decodeKeyFrames(tempFile);
    }

    private void write2NewFile(File src, File dest, FlvMetaData metaData)
            throws FileNotFoundException, IOException,
            UnsupportMediaTypeException, EOFException {
        UnsignedDataOutput output = null;
        try {
            output = new UnsignedDataOutput(new FileOutputStream(dest));
            encoder.encodeSignature(metaData.getSignature(), output);
            encoder.encodePreTagSize(0, output);
            int preTagSize = encoder.encodeMetaData(metaData, output);
            encoder.encodePreTagSize(preTagSize, output);
            append(src, output, true, true);
        } finally {
            IOUtils.closeQuietly(output);
        }
    }

    private FlvMetaData copy(File from, File destFile,
            boolean ignoreMetaDataTag, boolean ignoreCopyFlvSignature)
            throws FileNotFoundException, IOException,
            UnsupportMediaTypeException, EOFException {
        UnsignedDataOutput output = new UnsignedDataOutput(
                new FileOutputStream(destFile));

        try {
            return append(from, output, ignoreMetaDataTag,
                    ignoreCopyFlvSignature);
        } finally {
            IOUtils.closeQuietly(output);
        }
    }

    private FlvMetaData append(File from, UnsignedDataOutput dest,
            boolean ignoreMetaDataTag, boolean ignoreCopyFlvSignature)
            throws IOException, UnsupportMediaTypeException, EOFException {
        CopyTags copyVisitor = new CopyTags(dest, encoder);
        copyVisitor.setIgnoreCopyMetaDataTag(ignoreMetaDataTag);
        copyVisitor.setIgnoreCopyFlvSignature(ignoreCopyFlvSignature);
        new FlvDecoder().decode(from, null, copyVisitor);
        return copyVisitor.metaData;
    }

    private static class CopyTags extends TagDataVistorAdapter {
        private UnsignedDataOutput output;
        private FlvEncoder encoder;
        
        
        /**
         * true 表示忽略  metadata 不做拷贝
         */
        private boolean ignoreCopyMetaDataTag = false;
        private FlvMetaData metaData= null;

        /**
         * 忽略拷贝 flv 文件头
         */
        private boolean ignoreCopyFlvSignature = false;

        /**
         * @param output
         */
        private CopyTags(UnsignedDataOutput output, FlvEncoder encoder) {
            super();
            this.output = output;
            this.encoder = encoder;
        }

        @Override
        public boolean interruptAfterSignature(FlvSignature signature)
                throws IOException, EOFException {
            if (!ignoreCopyFlvSignature) {
                encoder.encodeSignature(signature, output);
                encoder.encodePreTagSize(0, output);
            }
            return false;
        }
        

        @Override
        public ITagData readAudioData(FlvSignature flv, ITagHead header,
                UnsignedDataInput inStream) throws IOException {
            return copyTag(flv, header, inStream);
        }
        
        @Override
        public ITagData readOtherData(FlvSignature flv, ITagHead header,
                UnsignedDataInput inStream) throws IOException {
            return copyTag(flv, header, inStream);
        }
        
        @Override
        public ITagData readScriptData(FlvSignature flv, ITagHead header,
                UnsignedDataInput inStream) throws IOException {
            if (!ignoreCopyMetaDataTag) {
                return copyTag(flv, header, inStream);
            } else {
                byte[] buffer = new byte[(int)header.getDataSize()];
                int length = inStream.readEnough(buffer);
                if (buffer.length != length) {
                    throw new IllegalArgumentException("脚本信息丢失");
                }
                
                FlvMetaData flvMetaData = new MetaDataDecoder().decode(buffer);
                if (null == flvMetaData) {
                    // 不是 metadata
                    writeTag(header, buffer);
                } else {
                    // 是 metadata信息，忽略
                    this.metaData = flvMetaData;
                }
                
                return null;
            }
        }
        
        @Override
        public ITagData readVideoData(FlvSignature signature, ITagHead header,
                UnsignedDataInput inStream) throws IOException {
            return copyTag(signature, header, inStream);
        }

        private ITagData copyTag(FlvSignature signature, ITagHead header,
                UnsignedDataInput inStream) throws IOException {

            // 重新申请内存空间
            byte[] buffer = ensureCapacity(header);

            int readed = inStream.readEnough(buffer, 0, (int)header.getDataSize());
            if (readed != header.getDataSize()) {
                logger.warn("非法的 Tag[{}]，期望长度[{}]与实际读取到的长度[{}]不相等",header, header.getDataSize(), readed);
            }
            
            writeTag(header, buffer);
            return null;
        }

        private void writeTag(ITagHead header, byte[] tagData) throws IOException {
            output.resetWriten();
            
            // Tag Head
            encoder.encodeTagHead(header, output);
            
            // Tag Data
            encoder.encodeTagData(header, tagData, output);
            
            // size ot this tag
            int preTagSize = (int)output.writen();
            encoder.encodePreTagSize(preTagSize, output);
        }

        private byte[] ensureCapacity(ITagHead header) {
            if (null == data || data.length < header.getDataSize()) {
                data = new byte[(int)(1 + 1.25 * header.getDataSize())];
            }
            
            return data;
        }
        
        private byte[] data = null;

        public final void setIgnoreCopyMetaDataTag(boolean ignoreMetaDataTag) {
            this.ignoreCopyMetaDataTag = ignoreMetaDataTag;
        }

        public final void setIgnoreCopyFlvSignature(boolean ignoreCopyFlvSignature) {
            this.ignoreCopyFlvSignature = ignoreCopyFlvSignature;
        }
    }

    private static Logger logger = org.slf4j.LoggerFactory.getLogger(MetaDataFixer.class);
}
