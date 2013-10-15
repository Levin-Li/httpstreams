package github.chenxh.media;

import java.io.IOException;

/**
 * 不支持的媒体类型
 * 
 * @author chenxh
 *
 */
public class UnsupportMediaTypeException extends IOException {
    public UnsupportMediaTypeException(String targetType) {
        super("desired type[" + targetType + "]");
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

}
