package com.thunisoft.videox.flv;

import java.nio.ByteBuffer;

public class FLVHead {
    
    private boolean hasVideo = true;
    private boolean hasAudio = true;
            
    
    private static int HAS_VIDEO = 1;
    private static int HAS_AUDIO = 4;

    
    public FLVHead () {
        
    }
    
    public ByteBuffer getData() {
        ByteBuffer data = ByteBuffer.allocate(9);
        
        data.put((byte)'F');
        data.put((byte)'L');
        data.put((byte)'V');
        
        // version
        data.put((byte)(1));

        // has video and has audio
        int flag = 0;
        if (hasAudio) {
            flag |= HAS_AUDIO;
        }
        if (hasVideo) {
            flag |= HAS_VIDEO;
        }
        data.put((byte)(flag));

        // header size
        data.putInt(9);
        
        data.flip();
        return data;
    }

    public boolean hasVideo() {
        return hasVideo;
    }

    public void hasVideo(boolean hasVideo) {
        this.hasVideo = hasVideo;
    }

    public boolean hasAudio() {
        return hasAudio;
    }

    public void hasAudio(boolean hasAudio) {
        this.hasAudio = hasAudio;
    }
}
