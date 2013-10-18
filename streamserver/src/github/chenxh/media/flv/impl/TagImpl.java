package github.chenxh.media.flv.impl;

import github.chenxh.media.flv.ITagTrunk;
import github.chenxh.media.flv.ITagData;
import github.chenxh.media.flv.ITagHead;


public class TagImpl implements ITagTrunk {
    private long preTagSize;
    private ITagHead head;
    private ITagData data;

    public TagImpl(ITagHead head, ITagData data) {
        this(head, data, 0);
    }

    public TagImpl(ITagHead head, ITagData data, long preTagSize) {
        this.head = head;
        this.data = data;
        this.preTagSize = preTagSize;
    }
    @Override
    public ITagData getData() {
        return data;
    }

    @Override
    public long getDataSize() {
        return head.getDataSize();
    }

    @Override
    public long getHeadSize() {
        return head.getHeadSize();
    }
    
    
    public long getPreTagSize() {
        return preTagSize;
    }

    @Override
    public int getTagType() {
        return head.getTagType();
    }

    @Override
    public long size() {
        return head.size();
    }

    public void setPreTagSize(long preTagSize) {
        this.preTagSize = preTagSize;
    }

    @Override
    public long getTimestamp() {
        return head.getTimestamp();
    }

}
