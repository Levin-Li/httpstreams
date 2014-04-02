package com.thunisoft.videox.mp4.stream;

import java.io.IOException;
import java.nio.ByteBuffer;



public interface Sample {

    public abstract ByteBuffer getData() throws IOException;

    public abstract boolean isKeyFrame();

    public abstract int getLength();

    public abstract long getTimestamp();

    public abstract long getTimeScale();

    public long getCompositeTime();

    public int getFlvTagType();
}
