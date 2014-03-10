package com.thunisoft.mediax.http;

import org.apache.commons.lang.StringUtils;

import com.thunisoft.mediax.core.HttpHeaders;

/**
 * [start, end] 的一个数据区间
 * <p>
 * 
 * @author chenxiuheng@gmail.com
 * @since V1.0
 * @Date 2014-3-8
 */
public class HttpRange implements Comparable<HttpRange> {
    private long start;
    private long length;

    final private long fullLength;

    public static final HttpRange EMPTY = newInstance(0, 0, 0);
    
    /**
     * 
     * @param start
     * @param rangeLength
     * @param fullLength
     * @throw {@link IllegalArgumentException} if (start + length > fullLength)
     */
    private HttpRange(long start, long rangeLength, long fullLength) {
        this.start = start;
        this.length = rangeLength;
        this.fullLength = fullLength;

        if (start + length > fullLength) {
            throw new IllegalArgumentException("(" + start + "+" + length
                    + ") > " + fullLength);
        }
    }

    public static HttpRange[] split(long fullLength, long rangeLength) {
        int segments = (int)(fullLength / rangeLength);
        if (segments * rangeLength < fullLength) {
            segments ++;
        }

        HttpRange[] ranges = new HttpRange[segments];
        for (int i = 0; i < ranges.length; i++) {
            ranges[i] = newInstance(i * rangeLength, rangeLength, fullLength);
        }

        return ranges;
    }
    
    /**
     * HTTP Range 响应片段。参考: <a href="http://greenbytes.de/tech/webdav/draft-ietf-httpbis-p5-range-latest.html#header.content-range"
     * > http header range</a>
     * @param contentRange 格式如：bytes 42-1233/1234
     * @return
     * @author chenxiuheng@gmail.com
     * @since V1.0
     * @Date 2014-3-8
     */
    public static HttpRange parse(String contentRange) {
        String[] units = StringUtils.split(contentRange, " ");
        if (!HttpHeaders.Values.BYTES.equals(units[0])) {
            throw new IllegalArgumentException("byte unit is not 'bytes'");
        }

        String[] contents = units[1].split("/");
        if (contents == null || contents.length != 2){
            throw new IllegalArgumentException(contentRange + " is illegal!");
        }

        return parse(contents[0], Long.parseLong(contents[1]));
    }
    
    
    /**
     * 
     * 一个 HTTP Range 请求片段。参考: <a href="http://greenbytes.de/tech/webdav/draft-ietf-httpbis-p5-range-latest.html#header.range"
     * > http header range</a>
     * 
     * <pre>
     * 示例一：
     *   请求第一个100 字节， range内容为： 0-99
     *   请求第二个100 字节， range内容为： 100-199 
     * 示例二：
     *   请求从 100字节及以后的所有字节： 100-   
     * 示例三：
     *   请求最后的 100 字节： -100
     * </pre>
     * 
     * @param range
     * @param maxLength
     * @return
     * @author chenxiuheng@gmail.com
     * @since V1.0
     * @Date 2014-3-8
     */
    public static HttpRange parse(String range, long fullLength) {
        final long maxEnd = fullLength - 1;

        long start;
        long end;

        String[] positions = StringUtils.split(range, '-');
        if (null == positions || positions.length != 2) {
            throw new IllegalArgumentException("[" + range + "] 不包含分隔符 '-' !");
        }

        String firstPosition = positions[0];
        String secondPosition = positions[1];
        if (StringUtils.isEmpty(firstPosition)
                && StringUtils.isEmpty(secondPosition)) {
            throw new IllegalArgumentException("[" + range + "] 不能只包含 '-' !");
        }

        if (StringUtils.isEmpty(secondPosition)) { // 100-
            start = Long.parseLong(firstPosition);
            end = maxEnd;
        } else if (StringUtils.isEmpty(firstPosition)) { // -100
            start = maxEnd - Long.parseLong(secondPosition);
            end = maxEnd;
        } else { // 100-199
            start = Long.parseLong(firstPosition);
            end = Long.parseLong(secondPosition);
        }

        return newInstance(start, end - start + 1, fullLength);
    }

    public static HttpRange newInstance(long start, long length, long fullLength) {
        HttpRange r = new HttpRange(start,
                Math.min(length, fullLength - start), fullLength);

        return r;
    }
    /**
     * @return bytes in the range
     * @author chenxiuheng@gmail.com
     * @since V1.0
     * @Date 2014-3-8
     */
    public long length() {
        return length;
    }

    /**
     * all linked range length
     * 
     * @return
     * @author chenxiuheng@gmail.com
     * @since V1.0
     * @Date 2014-3-8
     */
    public long fullLength() {
        return fullLength;
    }

    public long startPosition() {
        return start;
    }

    public long endPosition() {
        return start + length - 1;
    }

    public boolean include(long position){
        return start <= position && position < start + length;
    }

    /**
     * 
     * @param onePointInRange
     * @since V1.0 2014-3-10
     * @author chenxh
     */
    public void updateStart(long onePointInRange) {
        start = onePointInRange - onePointInRange % length();
    }

    public String toContentRangeHeader() {
        return HttpHeaders.Values.BYTES + " " + startPosition() + "-" + endPosition()
                + "/" + fullLength();
    }

    public String toRangeHeader() {
        return startPosition() + "-" + endPosition();
    }

    /**
     * @return  this range
     * @since V1.0 2014-3-10
     * @author chenxh
     */
    public HttpRange nextRange() {
        start += length;
        
        return this;
    }
    
    @Override
    public HttpRange clone() {
        return new HttpRange(start, length, fullLength); 
    }

    @Override
    public String toString() {
        return "Range (" + toContentRangeHeader() + ")";
    }

    @Override
    public int compareTo(HttpRange o) {
        if (startPosition() == o.startPosition()) {
            return 0;
        } else {
            return startPosition() < o.startPosition() ? -1 : 1;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof HttpRange)) {
            return false;
        }
        if (this == obj) {
            return true;
        }

        HttpRange range = (HttpRange) obj;
        return this.startPosition() == range.startPosition() 
                && this.length() == range.length()
                && this.fullLength() == range.fullLength();

    }
}
