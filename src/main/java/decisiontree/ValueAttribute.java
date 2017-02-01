package decisiontree;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ValueAttribute {
    private final Map<String, Integer> targetClassesFrequency = new HashMap<>();
    private int index;
    private double entropy;
    private int frequency;
    private String name;

    public int getIndex() {
        return index;
    }

    public void setIndex(int name) {
        this.index = name;
    }

    public double getEntropy() {
        return entropy;
    }

    public void setEntropy(double entropy) {
        this.entropy = entropy;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public Map<String, Integer> getTargetClassesFrequency() {
        return targetClassesFrequency;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return 31 * Objects.hashCode(name) + index;
    }
}
