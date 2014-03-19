package com.thunisoft.mediax.core.m3u8live;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class M3U8Container {
    private String stream;
    
    private int sequenceNumber = 1;
    private String m3u8File;
    private int maxSegments = 3;
    
    private List<Segment> segments = new LinkedList<Segment>();
    
    private static final Logger log = LoggerFactory.getLogger(M3U8Container.class);
    public M3U8Container(String stream, String file) {
        this.stream = stream;
        this.m3u8File = file;

        FileUtils.deleteQuietly(new File(m3u8File));
    }
    
    public void setup() throws IOException {
        updateFile();
    }
    
    public void append(String segment, long duration) throws IOException {
        segments.add(new Segment(segment, duration, sequenceNumber ++));
        
       // while(segments.size() > maxSegments) {
       //     segments.remove(0);
        //}

        updateFile();
    }
    
    
    /*
     * EXT-X-MEDIA-SEQUENCE
     * Each media file URI in a Playlist has a unique sequence number.  The sequence number 
     * of a URI is equal to the sequence number of the URI that preceded it plus one. The 
     * EXT-X-MEDIA-SEQUENCE tag indicates the sequence number of the first URI that appears
     * in a Playlist file.
     * 
        #EXTM3U
        #EXT-X-ALLOW-CACHE:NO
        #EXT-X-MEDIA-SEQUENCE:0
        #EXT-X-TARGETDURATION:10
        #EXTINF:10,
        http://media.example.com/segment1.ts
        #EXTINF:10,
        http://media.example.com/segment2.ts
        #EXTINF:10,
        http://media.example.com/segment3.ts
        #EXT-X-ENDLIST
        
        Using one large file, testing with ipod touch, this worked (149 == 2:29)
        #EXTM3U
        #EXT-X-TARGETDURATION:149
        #EXT-X-MEDIA-SEQUENCE:0
        #EXTINF:149, no desc
        out0.ts
        #EXT-X-ENDLIST
        
        Using these encoding parameters:
        ffmpeg -i test.mp4 -re -an -vcodec libx264 -b 96k -flags +loop -cmp +chroma -partitions +parti4x4+partp8x8+partb8x8 
        -subq 5 -trellis 1 -refs 1 -coder 0 -me_range 16 -keyint_min 25 -sc_threshold 40 -i_qfactor 0.71 -bt 200k -maxrate 96k 
        -bufsize 96k -rc_eq 'blurCplx^(1-qComp)' -qcomp 0.6 -qmin 10 -qmax 51 -qdiff 4 -level 30 -aspect 320:240 -g 30 -async 2 
        -s 320x240 -f mpegts out.ts

        Suggested by others for 128k
        ffmpeg -d -i 'rtmp://123.123.117.16:1935/live/abcdpc2 live=1' -re -g 250 -keyint_min 25 -bf 0 -me_range 16 -sc_threshold 40 -cmp 256 -coder 0 -trellis 0 -subq 6 -refs 5 -r 25 -c:a libfaac -ab:a 48k -async 1 -ac:a 2 -c:v libx264 -profile baseline -s:v 320x180 -b:v 96k -aspect:v 16:9 -map 0 -ar 22050 -vbsf h264_mp4toannexb -flags -global_header -f segment -segment_time 10 -segment_format mpegts /dev/shm/stream128ios%09d.ts 2>/dev/null
     */
    private void updateFile() throws IOException {
        
        // for the m3u8 content
        StringBuilder sb = new StringBuilder();
        appendLine(sb, "#EXTM3U");
        appendLine(sb, "#EXT-X-ALLOW-CACHE:NO");
        if (!segments.isEmpty()) {
            Segment seg = segments.get(0);
            
            appendLine(sb, "#EXT-X-MEDIA-SEQUENCE:" + seg.getSequenceNumber());
            appendLine(sb, "#EXT-X-TARGETDURATION:" + seg.getDuration());
        }


        for (Segment segment : segments) {
            appendLine(sb, "#EXTINF:" + segment.getDuration() + " duration:" + segment.getDuration());
            appendLine(sb, segment.getUri());
        }

        appendLine(sb, "#EXT-X-ENDLIST");
        final String content = sb.toString();
        
        log.debug("Playlist for: {}\n{}", stream, content);        
        FileUtils.write(new File(m3u8File), content);
    }
    
    private M3U8Container appendLine(StringBuilder b, String line) {
        b.append(line).append("\r\n");
        return this;
    }

    public static final class Segment  {
        private String uri;
        private long durationInMillSeconds;
        private int sequenceNumber;

        public Segment(String url, long duration, int sequenceNumber) {
            super();
            this.uri = url;
            this.durationInMillSeconds = duration;
            this.sequenceNumber = sequenceNumber;
        }
        public String getUri() {
            return uri;
        }
        public long getDurationInMillSeconds() {
            return durationInMillSeconds;
        }
        
        public int getDuration() {
            return (int)Math.ceil(((double)durationInMillSeconds) / 1000);
        }
        
        public int getSequenceNumber() {
            return sequenceNumber;
        }
    }

    public void setMaxSegments(int maxSegments) {
        this.maxSegments = maxSegments;
    }

    public String getM3u8File() {
        return m3u8File;
    }
}
