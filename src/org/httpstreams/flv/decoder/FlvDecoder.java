package org.httpstreams.flv.decoder;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.httpstreams.flv.FlvSupports;
import org.httpstreams.flv.StructureInputStream;
import org.httpstreams.flv.decoder.tags.data.TagData;
import org.httpstreams.flv.decoder.tags.data.scripts.ScriptData;


/**
 * 解析 FLV 文件
 * 
 * @author chenxh
 * 
 */
public class FlvDecoder {
    protected Log logger = LogFactory.getLog(getClass());

    public Flv decode(String flvFile) throws IOException {
        FlvHeaderDecoder decoder = new FlvHeaderDecoder(flvFile);
        return decoder;
    }


    /**
     * Flv 文件头部信息解析
     * 
     * @author chenxiuheng@gmail.com
     */
    public class FlvHeaderDecoder implements Flv {

        private int version = -1;
        private boolean hasVideo;
        private boolean hasAudio;
        private long dataOffset;
        private byte[] data;

        final private String filename;
        /**
         * @param inStream
         * @throws IOException
         */
        public FlvHeaderDecoder(String filename)
                throws IOException {
            this.filename = filename;
            
            StructureInputStream inStream = new StructureInputStream(filename);
            try {
                decode(inStream);
            } finally {
                IOUtils.closeQuietly(inStream);
            }
        }

        private void decode(StructureInputStream inStream) throws IOException {

            // ‘FLV’
            if ('F' != inStream.readUI8() 
                    || 'L' != inStream.readUI8()
                    || 'V' != inStream.readUI8()) {
                throw new IllegalArgumentException(inStream + "不是 FLV 文件");
            }

            // version
            this.version = inStream.readUI8();

            // flv header flag
            byte flag = (byte) inStream.readUI8();
            this.hasAudio = ByteUtils.isTrue(flag, 6);
            this.hasVideo = ByteUtils.isTrue(flag, 8);

            // 读取剩余的头部信息
            this.dataOffset = inStream.readUI32();
            if (dataOffset != FlvSupports.FLV_HEADER_LENGTH) {
                this.data = new byte[(int) dataOffset
                        - FlvSupports.FLV_HEADER_LENGTH];
                inStream.read(data);
            } else {
                this.data = ArrayUtils.EMPTY_BYTE_ARRAY;
            }
        }

        public String toString() {
            StringBuilder b = new StringBuilder();
            b.append("flv:{");
            b.append("filename:\"").append(filename).append("\"");
            b.append(", version:").append(version);
            b.append(", hasAudio:").append(hasAudio);
            b.append(", hasVideo:").append(hasVideo);
            b.append(", dataOffset:").append(dataOffset);
            b.append(", length:").append(getHeadLength());
            b.append("}");
            return b.toString();
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.httpstreams.flv.decoder.Flv#getVersion()
         */
        public int getVersion() {
            return version;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.httpstreams.flv.decoder.Flv#hasVideo()
         */
        public boolean hasVideo() {
            return hasVideo;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.httpstreams.flv.decoder.Flv#hasAudio()
         */
        public boolean hasAudio() {
            return hasAudio;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.httpstreams.flv.decoder.Flv#length()
         */
        public long getHeadLength() {
            return dataOffset;
        }

        @Override
        public FlvTagIterator getTagIterator() throws IOException {
            StructureInputStream inStream = new StructureInputStream(filename);
            this.decode(inStream);

            return new FlvTagIterator(this, inStream);
        }
        
        /**
         * 跳转到指定时间戳上
         * @param timestamp 毫秒级的时间戳
         * @return
         * @throws IOException 
         */
        public FlvTagIterator seek (double timestamp) throws IOException {
            FlvTagIterator itr = getTagIterator();
            
            if (!itr.hasNext()) {
                return null;
            }
            
            FlvTag tag = itr.next();
            if (!tag.isScript()) {
                return null;
            }
            
            TagData data = tag.getData();
            ScriptData scriptData = (ScriptData)data;
            if (!scriptData.isMetaData()) {
                return null;
            }
            
            long position = scriptData.getFilePosition(timestamp);
            if (position != -1) {
                itr.skip(position);
            }
            
            return itr;
        }
    }

}
