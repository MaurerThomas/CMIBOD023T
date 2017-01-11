package decisiontree;

public abstract class Node {
    private boolean isLeaf;
    private String label;
    private int index;

    public boolean isLeaf() {
        return this.isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
