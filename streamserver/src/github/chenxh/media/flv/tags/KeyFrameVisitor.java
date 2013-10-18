package github.chenxh.media.flv.tags;

import github.chenxh.media.UnsignedDataInput;
import github.chenxh.media.flv.FlvSignature;
import github.chenxh.media.flv.ITagData;
import github.chenxh.media.flv.ITagHead;
import github.chenxh.media.flv.ITagTrunk;
import github.chenxh.media.flv.script.metadata.KeyFrames;

import java.io.EOFException;
import java.io.IOException;

/**
 * 生成关键帧列表
 * 
 * @author chenxh
 *
 */
public class KeyFrameVisitor extends TagDataVistorAdapter {
    private KeyFrames keyFrames = new KeyFrames();

    private long newTagPosition;
    public KeyFrameVisitor(FlvSignature flv) {
        newTagPosition = flv.size();
    }
    
    public KeyFrameVisitor() {
        // 9 字节的头， 4 字节表示上一个字节的长度
        newTagPosition = 9 + 4;
    }

    @Override
    public boolean interruptAfterSignature(FlvSignature signature) throws IOException, EOFException {
        newTagPosition = signature.size();
        newTagPosition += 4;
        
        return false;
    }
    
    @Override
    public ITagData readVideoData(FlvSignature signature, ITagHead header, UnsignedDataInput inStream) throws IOException {
        long dataSize = header.getDataSize();

        // 只读取第一个字节
        int b = inStream.readUI8();
        inStream.skipBytes(dataSize - 1);

        int frameType = b >> 4;
        int codecId = b & 0xf;

        if (frameType == 1) { // 是关键帧
            double timeInSecond = 1.0 * header.getTimestamp() / 1000;
            keyFrames.appendKeyFrame(newTagPosition, timeInSecond);
        }
        return null;
    }
    

    @Override
    public boolean interruptAfterTag(ITagTrunk tag) throws IOException, EOFException {
        newTagPosition += 4; // 每个 tag 之前，都会有 4 字节用来记录上一个  tag 的大小 
        newTagPosition += tag.size();
        
        return false;
    }
    public KeyFrames getKeyFrames() {
        return keyFrames;
    }


}
