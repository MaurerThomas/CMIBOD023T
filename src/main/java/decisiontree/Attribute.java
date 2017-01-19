package decisiontree;

import java.util.HashMap;
import java.util.Map;

public class Attribute {

    private String name;
    private Map<String, ValueAttribute> valueAttributeMap = new HashMap<>();
    private double gain;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, ValueAttribute> getValueAttributeMap() {
        return valueAttributeMap;
    }

    public void setValueAttributeMap(Map<String, ValueAttribute> valueAttributeMap) {
        this.valueAttributeMap = valueAttributeMap;
    }

    public double getGain() {
        return gain;
    }

    public void setGain(double gain) {
        this.gain = gain;
    }
}
