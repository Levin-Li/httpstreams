package github.chenxh.media;

import java.io.DataOutputStream;
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

    public void writeUI8(long v) throws IOException{
        out.write((int)((v >>>  0) & 0xFF));
        incCount(1);
    }
    public void writeUI16(int v) throws IOException{
        out.write((int)((v >>>  8) & 0xFF));
        out.write((int)((v >>>  0) & 0xFF));
        incCount(2);
    }
    
    public void writeUI16(long v) throws IOException{
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
    
    public void writeUI24(long v) throws IOException{
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

    private byte writeBuffer[] = new byte[8];
    /**
     * Writes a <code>long</code> to the underlying output stream as eight
     * bytes, high byte first. In no exception is thrown, the counter 
     * <code>written</code> is incremented by <code>8</code>.
     *
     * @param      v   a <code>long</code> to be written.
     * @exception  IOException  if an I/O error occurs.
     * @see        java.io.FilterOutputStream#out
     */
    public final void writeLong(long v) throws IOException {
        writeBuffer[0] = (byte)(v >>> 56);
        writeBuffer[1] = (byte)(v >>> 48);
        writeBuffer[2] = (byte)(v >>> 40);
        writeBuffer[3] = (byte)(v >>> 32);
        writeBuffer[4] = (byte)(v >>> 24);
        writeBuffer[5] = (byte)(v >>> 16);
        writeBuffer[6] = (byte)(v >>>  8);
        writeBuffer[7] = (byte)(v >>>  0);
        out.write(writeBuffer, 0, 8);
    incCount(8);
    }

    /**
     * Converts the double argument to a <code>long</code> using the 
     * <code>doubleToLongBits</code> method in class <code>Double</code>, 
     * and then writes that <code>long</code> value to the underlying 
     * output stream as an 8-byte quantity, high byte first. If no 
     * exception is thrown, the counter <code>written</code> is 
     * incremented by <code>8</code>.
     *
     * @param      v   a <code>double</code> value to be written.
     * @exception  IOException  if an I/O error occurs.
     * @see        java.io.FilterOutputStream#out
     * @see        java.lang.Double#doubleToLongBits(double)
     */
    public final void writeDouble(double v) throws IOException{
        writeLong(Double.doubleToLongBits(v));
    }

    @Override
    public void write(byte[] b) throws IOException {
        out.write(b);
        
        incCount(b.length);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        out.write(b, off, len);

        incCount(len);
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
        
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
    public int writen() {
        return written;
    }

    /**
     * 重新计数
     */
    public void resetWriten() {
        written = 0;
    }
}
