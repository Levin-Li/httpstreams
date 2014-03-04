package com.thunisoft.mediax.core.mp4;

public enum BoxType {
    ftyp("ftyp"),
    uuid("uuid"),
    yqoo("yqoo"),
    moov("moov"),
    mdat("mdat");

    private String descript;
    private BoxType(String type) {
        this.descript = type;
    }

    public String value() {
        return descript;
    }

    @Override
    public String toString() {
        return descript;
    }
    
    public static boolean isUUID(String type) {
        return uuid.descript.equals(type);
    }
    
    
    public boolean equals(String type) {
        return value().equals(type);
    }
}
