package Bayes;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class Feature {
    private final Map<TargetClassFeature, Integer> targetClassFrequencies = new HashMap<>();
    private String name;
    private int index;
    private int totalFrequency;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotalFrequency(int totalFrequency) {
        this.totalFrequency = totalFrequency;
    }

    public Map<TargetClassFeature, Integer> getTargetClassFrequencies() {
        return targetClassFrequencies;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int hashCode() {
        return 31 * Objects.hashCode(name);
    }

}
