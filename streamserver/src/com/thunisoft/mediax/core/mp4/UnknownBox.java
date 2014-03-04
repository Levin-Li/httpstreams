package com.thunisoft.mediax.core.mp4;


public class UnknownBox extends Box {

    public UnknownBox(long boxSize, long boxHeadSize, BoxType type,
            String extendType) {
        super(boxSize, boxHeadSize, type, extendType);
    }

}
