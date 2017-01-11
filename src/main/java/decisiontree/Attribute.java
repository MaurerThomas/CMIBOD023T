package decisiontree;

import java.util.HashMap;
import java.util.Map;

public class Attribute {

    private String name;
    private Map<String, ValueAttribute> valueAttributeMap = new HashMap<>();
    private int index;

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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
