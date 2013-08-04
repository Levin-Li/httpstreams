package org.httpstreams.flv.decoder.tags.data.scripts.elements;

import org.httpstreams.flv.FlvSupports;

public class ValueFactory {
    public static Value getEmptyValue(int type) {
        final Value entry;

        switch (type) {
            case FlvSupports.SCRIPT_0_Number :
                entry = new NumberValue();
                break;
            case FlvSupports.SCRIPT_1_Boolean :
                entry = new BooleanValue();
                break;
            case FlvSupports.SCRIPT_2_String :
                entry = new StringValue();
                break;
            case FlvSupports.SCRIPT_3_Object:
                entry = new ScriptObject();
                break;
            case FlvSupports.SCRIPT_5_Null:
                entry = new NullValue();
                break;
            case FlvSupports.SCRIPT_8_ECMAArray:
                entry = new ECMAArrayValue();
                break;
            case FlvSupports.SCRIPT_10_StrictArray:
                entry = new StrictArrayValue();
                break;
            case FlvSupports.SCRIPT_11_Date :
                entry = new TimestampValue();
                break;
            case FlvSupports.SCRIPT_12_LongString :
                entry = new LongStringValue();
                break;
            default :
                throw new IllegalArgumentException("Unsupport Type[" + type
                        + "]");
        }
        return entry;
    }
}
