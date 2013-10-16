package github.chenxh.media;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class UnsignedDataOutput extends FilterOutputStream {
    /**
     * The number of bytes written to the data output stream so far. 
     * If this counter overflows, it will be wrapped to Integer.MAX_VALUE.
     */
    protected int written;

    public UnsignedDataOutput(OutputStream out) {
        super(out);
    }

    public void writeUI8(int v) throws IOException{
        out.write((int)((v >>>  0) & 0xFF));
        incCount(1);
    }

    public void writeUI16(int v) throws IOException{
        out.write((int)((v >>>  8) & 0xFF));
        out.write((int)((v >>>  0) & 0xFF));
        incCount(2);
    }
    
    public void writeUI24(int v) throws IOException{
        out.write((int)((v >>> 16) & 0xFF));
        out.write((int)((v >>>  8) & 0xFF));
        out.write((int)((v >>>  0) & 0xFF));
        incCount(3);
    }
    
    public void writeUI32(int v) throws IOException{
        out.write((v >>> 24) & 0xFF);
        out.write((v >>> 16) & 0xFF);
        out.write((v >>>  8) & 0xFF);
        out.write((v >>>  0) & 0xFF);
        incCount(4);
    }
    
    public void writeUI32(long v) throws IOException{
        out.write((int)((v >>> 24) & 0xFF));
        out.write((int)((v >>> 16) & 0xFF));
        out.write((int)((v >>>  8) & 0xFF));
        out.write((int)((v >>>  0) & 0xFF));

        incCount(4);
    }

    @Override
    public void write(byte[] b) throws IOException {
        super.write(b);
        
        incCount(b.length);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        super.write(b, off, len);

        incCount(len);
    }

    @Override
    public void write(int b) throws IOException {
        super.write(b);
        
        incCount(1);
    }
    /**
     * Increases the written counter by the specified value
     * until it reaches Integer.MAX_VALUE.
     */
    private void incCount(int value) {
        int temp = written + value;
        if (temp < 0) {
            temp = Integer.MAX_VALUE;
        }
        written = temp;
    }

    
    /**
     * 已经输出的字节数
     * 
     * @return
     */
    public int countOfOutput() {
        return written;
    }

    /**
     * 重新计数
     */
    public void resetOutputCount() {
        written = 0;
    }
}
