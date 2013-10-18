package github.chenxh.media.flv;

import github.chenxh.media.IDataTrunk;


public interface ITagHead extends IDataTrunk {


    public long getTimestamp();

    public int getStreamId();
}