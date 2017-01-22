package decisiontree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Tree {
    private DecisionTreeAlgorithm decisionTreeAlgorithm = new DecisionTreeAlgorithm();
    private List<Attribute> attributes = new ArrayList<>();
    private TreeNode root = new TreeNode();

    //    Begin

//    Load learning sets first, create decision tree root
//    node 'rootNode', add learning set S into root node
//    as its subset.

//   For rootNode, we compute
//   Entropy(rootNode.subset) first
//      If Entropy(rootNode.subset)==0,
//          then
//      rootNode.subset consists of records
//      all with the same value for the
//      categorical attribute, return a leaf node
//      with decision attribute: attribute value.

//    If Entropy(rootNode.subset)!=0,
//      then
//    compute information gain for each
//    attribute left(have not been used in
//    splitting), find attribute A with
//    Maximum(Gain(S,A)). Create child
//    treeNodes of this rootNode and add to
//    rootNode in the decision tree.
//    For each child of the rootNode, apply
//    ID3(S,A,V) recursively until reach
//    node that has entropy=0 or reach
//    leaf node.

//    End ID3.

    public static void main(String[] args) {
        Tree tree = new Tree();
        tree.init();
    }

    private void init() {
        decisionTreeAlgorithm.init();
        attributes = decisionTreeAlgorithm.getAttributes();
        buildDecisionTree(root);

    }

    private void buildDecisionTree(TreeNode currentNode) {
        Attribute bestAttribute = decisionTreeAlgorithm.getMaximumInformationGain(attributes);
        if (bestAttribute == null) {
            currentNode.setLeaf(true);
        }
        if (bestAttribute != null) {
            currentNode.setLabel(bestAttribute.getName());
        }
        attributes.remove(bestAttribute);

        Map<String, ValueAttribute> valueAttributeMap = bestAttribute.getValueAttributeMap();

        for (Map.Entry<String, ValueAttribute> entry : valueAttributeMap.entrySet()) {
            ValueAttribute currentValueAttribute = entry.getValue();
            String currentValueAttributeName = entry.getKey();

            if (currentValueAttribute.getEntropy() <= 0.0) {
                TreeNode leafNode = new TreeNode();
                leafNode.setLabel(currentValueAttributeName);
                leafNode.setLeaf(true);
            } else {
                Object[][] subset = decisionTreeAlgorithm.getSubsetForValueAttribute(currentValueAttribute);
                //decisionTreeAlgorithm.calculateAttributeGainForSubset(subset, currentValueAttribute);

                TreeNode newNode = new TreeNode();
                //currentNode.getChildren().put(currentValueAttributeName, newNode);
                // Subset meegeven, bijvoorbeeld in ValueAttribute
                buildDecisionTree(newNode);
            }
        }
    }
}
