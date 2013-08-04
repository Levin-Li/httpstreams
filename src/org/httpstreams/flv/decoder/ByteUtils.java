package org.httpstreams.flv.decoder;



public class ByteUtils {
	/**
	 * 
	 * @param value
	 * @param bitOffset  byte �еĵڼ�λ��1 ��ʾ��һλ
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
    
    public static char toHex (byte n) {
        return (char) (n < 10 ? (n + '0'):(n-10 + 'A'));
    }
}
