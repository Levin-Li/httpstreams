package github.chenxh.media.flv.script.metadata;

import github.chenxh.media.flv.script.AbstractDynamicObject;
import github.chenxh.media.flv.script.EcmaArray;


/**
 * 保存在片头的描点消息
 * 
 * @author chenxh
 *
 */
public class CuePoint {
    public static final CuePoint[] EMPTY_ARRAY = new CuePoint[0];

    private String name;
    private double time;
    private EcmaArray parameters;
    
    /**
     * like "navigation"
     */
    private String type;

    public CuePoint (EcmaArray octstream) {

        this.name = octstream.getString("name");
        this.time = octstream.getDouble("time");
        this.parameters = octstream.getEcmaArray("parameters");
        this.type = octstream.getString("type");
    }

    public CuePoint(String name, double time, EcmaArray parameters, String type) {
        this.name = name;
        this.time = time;
        this.parameters = parameters;
        this.type = type;
    }
    
    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
    public double getTime() {
        return time;
    }
    public AbstractDynamicObject getParams() {
        return parameters;
    }
    
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();

        appendTo(b);

        return b.toString();
    }
    
    public void appendTo(StringBuilder b) {
        b.append("{");
        b.append("name:").append(getName()).append(", ");
        b.append("time:").append(getTime()).append(", ");
        b.append("type:").append(getType()).append(", ");
        b.append("parameters:").append(getParams());
        b.append("}");
    }
}
