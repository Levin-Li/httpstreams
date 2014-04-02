package com.thunisoft.videox.mp4.stream;

import java.io.IOException;
import java.util.List;



public interface Chunk {
    public long getTimeStart();

    public long getTimeEnd();

    public long getTimeScale();

    public abstract List<Sample> getSamples() throws IOException;

}
