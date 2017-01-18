package decisiontree;

import java.util.HashMap;
import java.util.Map;

public class ValueAttribute {

    private String name;
    private double entropy;
    private double frequency;
    private Map<String, Double> frequencyMap = new HashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getEntropy() {
        return entropy;
    }

    public void setEntropy(double entropy) {
        this.entropy = entropy;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public Map<String, Double> getFrequencyMap() {
        return frequencyMap;
    }

    public void setFrequencyMap(Map<String, Double> frequencyMap) {
        this.frequencyMap = frequencyMap;
    }
}
