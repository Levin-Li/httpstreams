package com.thunisoft.videox.mp4;

import java.util.HashSet;
import java.util.Set;

public interface AudioSampleEntry {
    public static final String TYPE1 = "samr";
    public static final String TYPE2 = "sawb";
    public static final String TYPE3 = "mp4a";
    public static final String TYPE4 = "drms";
    public static final String TYPE5 = "alac";
    public static final String TYPE7 = "owma";
    public static final String TYPE8 = "ac-3"; /* ETSI TS 102 366 1.2.1 Annex F */
    public static final String TYPE9 = "ec-3"; /* ETSI TS 102 366 1.2.1 Annex F */
    public static final String TYPE10 = "mlpa";
    public static final String TYPE11 = "dtsl";
    public static final String TYPE12 = "dtsh";
    public static final String TYPE13 = "dtse";
    
    public static final Set<String> TYPES = new HashSet<String>(){{
        add(TYPE1);
        add(TYPE2);
        add(TYPE3);
        add(TYPE4);
        add(TYPE5);
        // add(TYPE6);
        add(TYPE7);
        add(TYPE8);
        add(TYPE9);
        add(TYPE10);
        add(TYPE11);
        add(TYPE12);
        add(TYPE13);
    }};
}
