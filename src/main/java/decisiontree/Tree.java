package decisiontree;

import java.util.ArrayList;
import java.util.List;

public class Tree {
    private DecisionTreeAlgorithm decisionTreeAlgorithm = new DecisionTreeAlgorithm();
    private List<Attribute> attributes = new ArrayList<>();
    private TreeNode root;


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
//    nodes of this rootNode and add to
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
        train();

    }

    public void train() {
        Attribute rootAttribute = decisionTreeAlgorithm.getMaximumInformationGain(attributes);

        TreeNode root = new TreeNode(attributes, rootAttribute);



    }

}
