package org.httpstreams;

import java.util.Arrays;

import org.apache.commons.io.EndianUtils;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long i = 637534208L;

		byte[] bytes = new byte[8];
		EndianUtils.writeSwappedLong(bytes, 0, i);
		System.out.println(Arrays.toString(bytes));
		
		System.out.println(38L << 8 << 8 << 8);
		
		printAsHex("audiocodecid");
		printAsHex("audiocodecid");
	}

    private static void printAsHex(String s) {
        char[] chars = s.toCharArray();
		for (char c : chars) {
		    int item = (byte)c;
            System.out.print(" " + toHex(item / 16) + toHex(item % 16));
        }
		
		System.out.println();
    }
	
	private static char toHex (int n) {
	    return (char) (n < 10 ? (n + '0'):(n-10 + 'A'));
	}

}
