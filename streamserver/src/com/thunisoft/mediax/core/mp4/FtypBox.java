package com.thunisoft.mediax.core.mp4;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import com.thunisoft.mediax.core.ByteBufferUtils;

public class FtypBox extends Box {

    private String majorBrand;
    private String minorVersion;
    private String[] compatibleBrands;
    
    public FtypBox(long boxSize, long boxHeadSize, BoxType type,
            String extendType) {
        super(boxSize, boxHeadSize, type, extendType);
    }

    @Override
    public void parseData(ByteBuffer data, Mp4Reader reader) throws IOException {
        majorBrand = ByteBufferUtils.read4cc(data);
        minorVersion = ByteBufferUtils.read4cc(data);
        
        compatibleBrands = new String[data.remaining()/4];
        for (int i = 0; i < compatibleBrands.length; i++) {
            compatibleBrands[i] = ByteBufferUtils.read4cc(data);
        }

        reader.next();
    }
    
    
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        
        b.append("Ftyp {");
        b.append(" major: ").append(majorBrand);
        b.append(", minor:").append(minorVersion);
        b.append(", compatibleBrands:").append(Arrays.toString(compatibleBrands));
        b.append("}");
        
        return b.toString();
    }
}
