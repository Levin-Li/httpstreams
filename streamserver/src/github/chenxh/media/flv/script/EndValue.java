package github.chenxh.media.flv.script;

public 
final class EndValue {
    private static final EndValue instance = new EndValue();
    private EndValue(){};
    
    @Override
    public String toString() {
        return "END";
    }
    
    public static EndValue getInstance() {
        return instance;
    }
}
