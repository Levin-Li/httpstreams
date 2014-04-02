package com.thunisoft.videox.mp4.stream;



public interface ChunkStream {

    public abstract boolean hasNext();

    public abstract Chunk next();

    public abstract void remove();

}
