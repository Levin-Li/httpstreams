package org.httpstreams.flv;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.EndianUtils;
import org.apache.commons.io.input.ProxyInputStream;

/**
 * �� InputStream ������ǿ��֧�ֶ��ֽڵ� int ���͵�ת����
 * 
 * @author chenxiuheng@gmail.com
 *
 */
public class StructureInputStream extends ProxyInputStream {

	final private byte[] ui8 = new byte[1];
	final private byte[] ui16 = new byte[2];
	final private byte[] ui24 = new byte[3];
	final private byte[] ui32 = new byte[4];
	final private byte[] ui64 = new byte[8];
	

	public StructureInputStream (String file) throws FileNotFoundException {
		this(new FileInputStream(file));
	}
	
	public StructureInputStream(InputStream proxy) {
		super(new BufferedInputStream(proxy));
	}

	public StructureInputStream(byte[] data) {
		super(new ByteArrayInputStream(data));
	}

    /**
     * ��������н�ȡָ�����ȵ�������
     * 
     * @param inStream
     * @param length
     * @return
     * @throws IOException
     */
	public static StructureInputStream subInputStream (InputStream inStream, long length) throws IOException {
		// ����Ҫ��ȡ�����ݿ���������
		// �ڶ����ݽ��д����ʱ�򣬺ܿ��ܳ���
		// �����������Խ��ͳ���������ļ���ɵ�Ӱ��
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[2048];

		long counter = length;
		int readed = -1;
		while((readed = inStream.read(buffer, 0, (int)Math.min(counter, buffer.length)))!=-1) {
			counter -= readed;
			outStream.write(buffer, 0, readed);
			
			if (counter < 1) {
				return new StructureInputStream(outStream.toByteArray());
			}
		}

		return new StructureInputStream(outStream.toByteArray());
	}
	
	/**
	 * Ԥ��ȡ 3 �ֽ�
	 * @return
	 * @throws IOException
	 */
	public int preReadUI24 () throws IOException {
	    mark(3);
	    try {
	        return readUI24();
	    } finally {
	        reset();
	    }
	}

	public int readUI8 () throws IOException {
		return readUInt(ui8);
	}
	
	/**
     * Ԥ��ȡ 1 �ֽ�
     * @return
     * @throws IOException
     */
    public boolean isEnd () {
        try {
            mark(1);
            try {
                int nextByte = read();
                return -1 == nextByte;
            } finally {
                reset();
            }
        } catch (IOException e) {
            return true;
        }
    }
	
	public int readUI16 () throws IOException {
		return readUInt(ui16);
	}
	
	public int readSI16() throws IOException {
		int value = readUI16();
		
		return EndianUtils.swapInteger(value);
	}

	public int readUI24 () throws IOException {
		return readUInt(ui24);
	}
	
	public long readUI32 () throws IOException {
		byte[] bytes = ui32;
		readActual(bytes);
		
		long value = bytes[0];
		for(int i = 1; i < bytes.length ; i ++) {
			value = ((long)(value << 8)) | (0xff & bytes[i]);
		}
		return value;
	}
	
	private int readUInt (byte[] bytes) throws IOException {
		final int length = read(bytes);
		if (length != bytes.length) {
		    return -1;
		}
		
		int value = bytes[0];
		for(int i = 1; i < length ; i ++) {
			value = (value << 8) | (0xff & bytes[i]);
		}
		return value;
	}
	
	public double readDouble () throws IOException {
		long d = readLong();
		
		return Double.longBitsToDouble(d);
		
	}

	public long readLong() throws IOException {
		int length = read(ui64);
		if (length != ui64.length) {
		    return -1;
		}
		
		long d = ui64[0];
		for (int i = 1; i < ui64.length; i++) {
			d = (d<<8)|(0xff & ui64[i]);
		}
		return d;
	}
	
	/**
	 * ���û�ж�ȡ����  bytes �ȳ��������ֽڣ����׳��쳣
	 * @param bytes
	 * @throws IOException
	 */
	public void readActual(byte[] bytes) throws IOException {
		int readed = read(bytes);
		
		if (readed != bytes.length) {
			throw new IOException("need [" + bytes.length + "] but actual is [" + readed + "]");
		}
	}
	
	
	@Override
	public long skip(long ln) throws IOException {
		long remained = ln;  // ʣ����Ҫ���Ե��ֽ�����
		long skipped;        // �����Ѿ��������ֽ�����
		do {
			skipped = super.skip(remained);
			remained -= skipped;
		} while (remained > 0 && skipped > 0);
		
		
		// �����ļ���ʽ�����⣬���ܻ�����ļ���ǰ������������ʵ�ʺ��Ե��ֽ����������������ֽ���
		return ln - remained;
	}

    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

}
