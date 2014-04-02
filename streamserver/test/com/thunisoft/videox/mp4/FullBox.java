package com.thunisoft.videox.mp4;

public class FullBox extends Box {
    private long version;
    private long flags;

    public FullBox(Box copy) {
        super(copy);
    }

    public long version() {
        return version;
    }

    public void version(long version) {
        this.version = version;
    }

    public long flags() {
        return flags;
    }

    public void flags(long flags) {
        this.flags = flags;
    }

}
