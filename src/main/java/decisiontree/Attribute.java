package decisiontree;

import java.util.HashSet;
import java.util.Set;

public class Attribute {

    private int index;
    private Set<ValueAttribute> valueAttributeMap = new HashSet<>();
    private double gain;

    public int getIndex() {
        return index;
    }

    public void setIndex(int name) {
        this.index = name;
    }

    public Set<ValueAttribute> getValueAttributeSet() {
        return valueAttributeMap;
    }

    public double getGain() {
        return gain;
    }

    public void setGain(double gain) {
        this.gain = gain;
    }
}
