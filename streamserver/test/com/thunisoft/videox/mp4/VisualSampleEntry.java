package com.thunisoft.videox.mp4;

import java.util.HashSet;
import java.util.Set;


/**
 * <h1>4cc = "{@value #TYPE_MP4V}" || "{@value #TYPE_S263}" || "{@value #TYPE_AVC1}" || "{@value #TYPE_AVC3}" || "{@value #TYPE_DRMI}"</h1>
 * Contains information common to all visual tracks.
 * <pre>
 * aligned(8) abstract class SampleEntry (unsigned int(32) format)
 *   extends Box(format){
 *    const unsigned int(8)[6] reserved = 0;
 *    unsigned int(16) data_reference_index;
 * }
 * <pre>
 * class VisualSampleEntry(codingname) extends AbstractSampleEntry (codingname){
 *  unsigned int(16) pre_defined = 0;
 *  const unsigned int(16) reserved = 0;
 *  unsigned int(32)[3] pre_defined = 0;
 *  unsigned int(16) width;
 *  unsigned int(16) height;
 *  template unsigned int(32) horizresolution = 0x00480000; // 72 dpi
 *  template unsigned int(32) vertresolution = 0x00480000; // 72 dpi
 *  const unsigned int(32) reserved = 0;
 *  template unsigned int(16) frame_count = 1;
 *  string[32] compressorname;
 *  template unsigned int(16) depth = 0x0018;
 *  int(16) pre_defined = -1;
 * }
 * </pre>
 *
 * Format-specific information is appened as boxes after the data described in ISO/IEC 14496-12 chapter 8.16.2.
 */
public interface VisualSampleEntry  {
    public static final String TYPE_MP4V = "mp4v";
    public static final String TYPE_S263 = "s263";
    public static final String TYPE_AVC1 = "avc1";
    public static final String TYPE_AVC3 = "avc3";
    public static final String TYPE_DRMI = "drmi";
    
    public static final Set<String> TYPES = new HashSet<String>(){{
        add(TYPE_MP4V);
        add(TYPE_S263);
        add(TYPE_AVC1);
        add(TYPE_AVC3);
        add(TYPE_DRMI);
    }};
    
    public static final int entryContent = 0;
}
