package com.thunisoft.mediax.core.amf;

import java.io.File;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;

import com.thunisoft.mediax.core.codec.amf.AMF0Decoder;
import com.thunisoft.mediax.core.codec.amf.AMF0Encoder;
import com.thunisoft.mediax.core.codec.amf.AMFObject;
import com.thunisoft.mediax.core.codec.amf.Entry;
import com.thunisoft.mediax.core.codec.flv.FlvMetaData;
import com.thunisoft.mediax.core.codec.flv.tag.Tag;
import com.thunisoft.mediax.core.pseudostreaming.flv.FlvDecoder;
import com.thunisoft.mediax.core.pseudostreaming.flv.FlvDecoder.TagIterator;

public class TestAmf extends TestCase {
    
    private AMF0Encoder encoder = new AMF0Encoder();
    private AMF0Decoder decoder = new AMF0Decoder();

    
    
    public void testDecode() throws DecoderException, EncoderException {
        FlvDecoder flvDecoder = new FlvDecoder();

        // String pathname = "D:/Thunisoft/MyEclipse/workspaces/统一音视频平台/red5-client/03.flv";
        String pathname = "C:/thunisoft/Apache2.2/htdocs/flv/kuiba-0001.flv";
        FlvMetaData metadata = getMetaData(flvDecoder, pathname);
        
        ByteBuffer bytes = encoder.encode(new Object[]{"onMetaData", metadata.getMetadata()});
        Object[] rst = decoder.decode(bytes);
        ByteBuffer b = null;

        ByteBuffer reEncoded = encoder.encode(rst);
        assertEquals(bytes, reEncoded);
        
    }


    private static FlvMetaData getMetaData(FlvDecoder decoder, String pathname) throws DecoderException, EncoderException {
        TagIterator iterator = decoder.decode(new File(pathname));

        while (iterator.hasNext()) {
            Tag tag = nextTag(iterator);

            System.out.println(tag);
            if (tag instanceof FlvMetaData) {
                return (FlvMetaData)tag;
            }

        }
        
        throw new DecoderException("没有找到Metadata数据");
    }
    
    private static Tag nextTag(TagIterator iterator) {

        try {
            Tag tag = iterator.nextTag();
            return tag;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    

    public void testString() throws EncoderException, DecoderException {
        String value = "onMetaData";
        
        ByteBuffer bytes = encoder.encode(value);
        Object[] rst = decoder.decode(bytes);
        assertEquals(value, rst[0]);
    }
    
    public void testDouble() throws EncoderException, DecoderException {
        int value = 1;
        

        ByteBuffer bytes = encoder.encode(value);
        Object[] rst = decoder.decode(bytes);
        

        assertEquals(value, ((Number)rst[0]).intValue());
    }
    
    public void testArray() throws EncoderException, DecoderException {
        double[] values = new double[]{1,2,3,4,5,6};
        
        Object[] object = new Object[]{"onMetaData", values};
        
        ByteBuffer bytes = encoder.encode(object);
        Object[] rst = decoder.decode(bytes);
        
        assertEquals(object.length, rst.length);
        assertEquals("onMetaData", rst[0]);
        
        assertTrue(rst[1].getClass().isArray());
        
        int length = Array.getLength(rst[1]);
        for (int i = 0; i < length; i++) {
            assertEquals(values[i], ((Number)Array.get(rst[1], i)).doubleValue());
        }
    }
    
    public void testList() throws EncoderException, DecoderException {
        List<Double> values = new ArrayList<Double>();
        values.add(Double.MIN_VALUE);
        values.add(2.0);
        values.add(3.0);
        values.add(Double.MAX_VALUE);
        
        
        Object[] object = new Object[]{"onMetaData", values};
        
        ByteBuffer bytes = encoder.encode(object);
        Object[] rst = decoder.decode(bytes);
        
        assertEquals(object.length, rst.length);
        assertEquals("onMetaData", rst[0]);
        
        assertTrue(rst[1].getClass().isArray());
        
        int length = Array.getLength(rst[1]);
        for (int i = 0; i < length; i++) {
            assertEquals(values.get(i), ((Number)Array.get(rst[1], i)).doubleValue());
        }
    }
    
    public void testMapAndTimestamp() throws EncoderException, DecoderException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("biterate", 441000);
        map.put("lastModified", new Timestamp(System.currentTimeMillis()));
        map.put("creator", null);
        
        Object[] object = new Object[]{"onMetaData", map};
        
        ByteBuffer bytes = encoder.encode(object);
        Object[] rst = decoder.decode(bytes);
        
        assertEquals(object.length, rst.length);
        assertEquals("onMetaData", rst[0]);
        
        assertTrue(rst[1] instanceof AMFObject);
        
        AMFObject amfObject = (AMFObject)rst[1];
        assertEquals(amfObject.size(), map.size());
        for (Entry entry : amfObject) {
            String key = entry.getName();
            
            if (entry.getValue() instanceof Number) {
                Number actualValue = (Number) entry.getValue();
                Number exceptValue = (Number) map.get(key);

                assertEquals(actualValue.doubleValue(), exceptValue.doubleValue());
            } else if (entry.getValue() instanceof Date) {
                Date actualValue = (Date) entry.getValue();
                Date exceptValue = (Date) map.get(key);
                assertEquals(actualValue.getTime(), exceptValue.getTime());
            } else {
                Object actualValue = (Object) entry.getValue();
                Object exceptValue = (Object) map.get(key);
                assertEquals(actualValue, exceptValue);
            }
        }
    }
}
