package decisiontree;

public abstract class Node {
    private boolean isLeaf;
    private int index;
    private String targetClass;

    public boolean isLeaf() {
        return this.isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int label) {
        this.index = label;
    }

    public String getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(String targetClass) {
        this.targetClass = targetClass;
    }
}
