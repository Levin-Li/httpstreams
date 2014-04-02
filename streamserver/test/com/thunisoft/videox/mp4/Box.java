package com.thunisoft.videox.mp4;

public class Box {
    public static final String QUERY_DOT = ".";
    
    private String name;
    private long size;
    
    private long headSize = -1;
    private long position = -1;
    
    public Box(){}
    public Box(long size, String name){
        this.size = size;
        this.name = name;
    }
    
    public Box(Box copy) {
        this.name = copy.name;
        this.size = copy.size;
        this.headSize = copy.headSize;
        this.position = copy.position;
    }
    
    public String type() {
        return name;
    }
    
    public void type(String name) {
        this.name = name;
    }

    public long headSize() {
        return headSize;
    }
    
    public void headSize(long headsize) {
        this.headSize = headsize;
    }
    
    public long size() {
        return size;
    }
    
    public void size(long size) {
        this.size = size;
    }
    
    public void position(long position) {
        this.position = position;
    }
    
    public long position() {
        return position;
    }
    
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        
        b.append("Box[type=").append(name);
        if (position() >= 0) {
            b.append(", p=").append(position());
        }
        b.append(", size=").append(size);
        b.append("]");
        
        return b.toString();
    }
}
