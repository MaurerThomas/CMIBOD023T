package decisiontree;

import java.util.HashMap;
import java.util.Map;

public class TreeNode extends Node {
    private Map<String, TreeNode> children = new HashMap<>();


    public TreeNode() {
    }

    public Map<String, TreeNode> getChildren() {
        return children;
    }

    public void setChildren(Map<String, TreeNode> children) {
        this.children = children;
    }


}
