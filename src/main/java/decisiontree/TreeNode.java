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

    public void printTree() {
        System.out.println(this.getLabel());
        for (Map.Entry<String, TreeNode> mapEntry : children.entrySet()) {
            String nodeName = mapEntry.getKey();
            TreeNode node = mapEntry.getValue();

            if (node.getChildren().isEmpty()) {
                //this valueAttribute has no children and directly goes to a class.
                System.out.println("\t" + mapEntry.getKey() + " - " + mapEntry.getValue().getTargetClass());
            } else {
                System.out.println("\t" + nodeName);
                System.out.println("\t\t" + node.getLabel());
            }

            for (Map.Entry<String, TreeNode> nodeMapEntry : node.getChildren().entrySet()) {
                if (nodeMapEntry.getValue().isLeaf()) {
                    nodeName = nodeMapEntry.getKey() + " - " + nodeMapEntry.getValue().getTargetClass();
                }
                System.out.println("\t\t\t" + nodeName);
            }
        }
    }

}
