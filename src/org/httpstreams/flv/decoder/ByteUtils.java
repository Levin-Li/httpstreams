package org.httpstreams.flv.decoder;



public class ByteUtils {
	/**
	 * 
	 * @param value
	 * @param bitOffset  byte 中的第几位，1 表示第一位
	 * @return
	 */
	public static boolean isTrue (byte value, int bitOffset) {
		final int bit = (value >>> (8-bitOffset)) & 1;
		return 1==bit;
	}
	
    public static byte[] str2bytes(String s) {
        byte[] bytes = new byte[s.length()];
        
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte)s.charAt(i);
        }

        return bytes;
    }
    
    public static String toHex (byte n) {
        char[] b = new char[2];
        
        int first = (n & 0xf0 ) >> 4 ;
        int second = (n & 0xf );
        
        b[0] = (char) (first < 10 ? (first + '0'):(first-10 + 'A'));
        b[1] = (char) (second < 10 ? (second + '0'):(second-10 + 'A'));
        return String.valueOf(b);
    }
    
    public static void main(String[] args) {
        String str = "Hello my Name is Peter.";
        
        for (byte  b : str2bytes(str)) {
            System.out.print(toHex(b) + " ");
        }
    }
}
