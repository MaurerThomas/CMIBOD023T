package Bayes;

class TargetClassFeature {
    private String name;
    private int frequency;
    private int totalTargetClassesFrequency;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getTotalTargetClassesFrequency() {
        return totalTargetClassesFrequency;
    }

    public void setTotalTargetClassesFrequency(int totalTargetClassesFrequency) {
        this.totalTargetClassesFrequency = totalTargetClassesFrequency;
    }
}
