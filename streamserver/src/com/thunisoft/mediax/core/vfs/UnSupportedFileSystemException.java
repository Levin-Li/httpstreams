package com.thunisoft.mediax.core.vfs;

public class UnSupportedFileSystemException extends RuntimeException {
    /**   */
    private static final long serialVersionUID = 1L;

    public UnSupportedFileSystemException(String protocol) {
        super("unsupported protocol [" + protocol + "]");
    }
}
