package com.thunisoft.mediax.core.http;

import java.util.Arrays;

import com.thunisoft.mediax.http.HttpRange;

public class TestHttpRange {
    public static void main(String[] args) {
        // 区间在总长度里面
        HttpRange r1 = HttpRange.newInstance(50, 20, 200);
        assertTrue(r1.startPosition(), 50);
        assertTrue(r1.endPosition(), 69);
        assertTrue(r1.length(), 20);
        assertTrue(r1.fullLength(), 200);
        
        // 区间不在里面

        HttpRange r2 = HttpRange.newInstance(50, 20, 60);
        assertTrue(r2.startPosition(), 50);
        assertTrue(r2.endPosition(), 59);
        assertTrue(r2.length(), 10);  // 这个一定要对！
        assertTrue(r2.fullLength(), 60);
        
        HttpRange r3 = HttpRange.newInstance(0, 180, 200);
        HttpRange r4 = HttpRange.parse(r3.toRangeHeader(), 200);
        System.out.println(r3.equals(r4));
        
        
        HttpRange r5 = HttpRange.newInstance(0, 180, 170);
        HttpRange r6 = HttpRange.parse(r5.toRangeHeader(), 170);
        System.out.println(r5.equals(r6));
        
        HttpRange r7 = HttpRange.parse(r6.toContentRangeHeader());
        System.out.println(r7 + " equals " + r6 + " is "+ r7.equals(r6));
        
        HttpRange r8 = HttpRange.newInstance(0, 10, 20);
        HttpRange r9 = HttpRange.newInstance(10, 10, 20);
        HttpRange[] arr1 = new HttpRange[]{r9, r8};
        Arrays.sort(arr1);
        System.out.println(arr1[0].equals(r8));
        
        HttpRange[] r10 = HttpRange.split(1024 * 10 + 1, 1024);
        System.out.println(r10.length + ", " + Arrays.toString(r10));
        
    }
    
    private static void assertTrue(Number o1, Number o2) {
        if (o1.longValue() != o2.longValue()) {
            throw new RuntimeException(o1 + "!=" + o2);
        }
    }
}
