package github.chenxh.media.flv;

import java.io.IOException;

import github.chenxh.media.UnsignedDataOutput;

public class FlvEncoder {
    /**
     * byte
     * @param signature
     * @return  how many bytes output
     * @throws IOException 
     */
    public int encodeSignature(FlvSignature signature, UnsignedDataOutput output) throws IOException {
        int curCount = output.countOfOutput();

        // FLV
        output.writeUI8((int)'F');
        output.writeUI8((int)'L');
        output.writeUI8((int)'V');
        
        // version
        output.writeUI8(signature.getVersion());
        
        // typeFlags
        output.writeUI8(signature.getTypeFlags());
        
        // header size
        output.writeUI32(FlvSignature.MIN_HEAD_SIZE);

        return output.countOfOutput() - curCount;
    }
    
    
}
