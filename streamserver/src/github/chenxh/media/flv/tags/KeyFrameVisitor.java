package github.chenxh.media.flv.tags;

import github.chenxh.media.UnsignedDataInput;
import github.chenxh.media.flv.FlvSignature;
import github.chenxh.media.flv.IFileHeadDataVisitor;
import github.chenxh.media.flv.ITagData;
import github.chenxh.media.flv.ITagHead;
import github.chenxh.media.flv.ITagTrunk;
import github.chenxh.media.flv.script.KeyFrames;

import java.io.EOFException;
import java.io.IOException;

/**
 * ���ɹؼ�֡�б�
 * 
 * @author chenxh
 *
 */
public class KeyFrameVisitor extends TagDataVistorAdapter implements IFileHeadDataVisitor {
    private KeyFrames keyFrames = new KeyFrames();

    private long newTagPosition;
    public KeyFrameVisitor(FlvSignature flv) {
        newTagPosition = flv.size();
    }
    
    public KeyFrameVisitor() {
        // 9 �ֽڵ�ͷ�� 4 �ֽڱ�ʾ��һ���ֽڵĳ���
        newTagPosition = 9 + 4;
    }

    @Override
    public void readFileHeadData(FlvSignature flv, UnsignedDataInput inStream) throws IOException {
        newTagPosition = flv.size();
        newTagPosition += 4;
    }

    @Override
    public ITagData readVideoData(ITagHead header, UnsignedDataInput inStream) throws IOException {
        long dataSize = header.getDataSize();

        // ֻ��ȡ��һ���ֽ�
        int b = inStream.readUI8();
        inStream.skipBytes(dataSize - 1);

        int frameType = b >> 4;
        int codecId = b & 0xf;

        if (frameType == 1) { // �ǹؼ�֡
            double timeInSecond = 1.0 * header.getTimestamp() / 1000;
            keyFrames.appendKeyFrame(newTagPosition, timeInSecond);
        }
        return null;
    }
    
    @Override
    public boolean interruptAfter(ITagTrunk tag) throws IOException, EOFException {
        newTagPosition += 4; // ÿ�� tag ֮ǰ�������� 4 �ֽ�������¼��һ��  tag �Ĵ�С 
        newTagPosition += tag.size();
        
        return false;
    }
    public KeyFrames getKeyFrames() {
        return keyFrames;
    }


}
