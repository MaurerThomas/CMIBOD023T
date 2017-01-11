package decisiontree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeNode extends Node {
    private List<Attribute> attributes = new ArrayList<>();
    private Map<String, TreeNode> children = new HashMap<>();

    public TreeNode(List<Attribute> attributes) {
        this.attributes = attributes;
    }
}
