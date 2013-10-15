package github.chenxh.media.flv.impl;

import github.chenxh.media.flv.ITag;
import github.chenxh.media.flv.ITagData;
import github.chenxh.media.flv.ITagHead;


public class FlvTagImpl implements ITag {
    private long preTagSize;
    private ITagHead head;
    private ITagData data;

    public FlvTagImpl(ITagHead head, ITagData data) {
        this(head, data, 0);
    }

    public FlvTagImpl(ITagHead head, ITagData data, long preTagSize) {
        this.head = head;
        this.data = data;
        this.preTagSize = preTagSize;
    }
    @Override
    public ITagData getData() {
        return data;
    }

    @Override
    public long getBodySize() {
        return head.getBodySize();
    }

    @Override
    public long getTagHeadSize() {
        return head.getTagHeadSize();
    }
    
    
    public long getPreTagSize() {
        return preTagSize;
    }

    @Override
    public int getType() {
        return head.getType();
    }

    @Override
    public long size() {
        return head.size();
    }

    public void setPreTagSize(long preTagSize) {
        this.preTagSize = preTagSize;
    }

}
