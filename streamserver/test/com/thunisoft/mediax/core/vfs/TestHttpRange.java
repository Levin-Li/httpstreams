package com.thunisoft.mediax.core.vfs;

import java.util.Arrays;

import junit.framework.TestCase;

import com.thunisoft.mediax.http.HttpRange;

public class TestHttpRange extends TestCase {
    
    public  void test() {
        // 区间在总长度里面
        HttpRange r1 = HttpRange.newInstance(50, 20, 200);
        assertEquals(r1.startPosition(), 50);
        assertEquals(r1.endPosition(), 69);
        assertEquals(r1.length(), 20);
        assertEquals(r1.fullLength(), 200);
        
        // 区间不在里面

        HttpRange r2 = HttpRange.newInstance(50, 20, 60);
        assertEquals(r2.startPosition(), 50);
        assertEquals(r2.endPosition(), 59);
        assertEquals(r2.length(), 10);  // 这个一定要对！
        assertEquals(r2.fullLength(), 60);
        
        HttpRange r3 = HttpRange.newInstance(0, 180, 200);
        HttpRange r4 = HttpRange.parse(r3.toRangeHeader(), 200);
        assertEquals(r3, r4);
        
        
        HttpRange r5 = HttpRange.newInstance(0, 180, 170);
        HttpRange r6 = HttpRange.parse(r5.toRangeHeader(), 170);
        assertEquals(r5, r6);
        
        HttpRange r7 = HttpRange.parse(r6.toContentRangeHeader());
        assertEquals(r7 , r6);
        
        HttpRange r8 = HttpRange.newInstance(0, 10, 20);
        HttpRange r9 = HttpRange.newInstance(10, 10, 20);
        HttpRange[] arr1 = new HttpRange[]{r9, r8};
        Arrays.sort(arr1);
        assertEquals(arr1[0], r8);
        
        HttpRange[] r10 = HttpRange.split(1024 * 10 + 1, 1024);
        assertEquals(r10.length, 11);
        
    }
    
}
