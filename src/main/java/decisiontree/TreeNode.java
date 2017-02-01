package decisiontree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeNode extends Node {
    private Map<String, TreeNode> children = new HashMap<>();

    public TreeNode() {
    }

    public Map<String, TreeNode> getChildren() {
        return children;
    }

    public void printTree(TreeNode node, List<String> header, String headSpacing) {
        String spacing = headSpacing;
        String nodeName = header.get(node.getIndex());
        System.out.println(spacing + nodeName);
        spacing = spacing + "\t";

        for (Map.Entry<String, TreeNode> nodeMapEntry : node.getChildren().entrySet()) {
            if (nodeMapEntry.getValue().isLeaf()) {
                String leafName = nodeMapEntry.getKey() + " - " + nodeMapEntry.getValue().getTargetClass();
                System.out.println(spacing + leafName);
            } else {
                headSpacing = spacing;
                printTree(nodeMapEntry.getValue(), header, headSpacing);
            }
        }
    }
}
