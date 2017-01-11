package decisiontree;

import util.DataReader;

public class DecisionTreeAlgorithm {
    private DataReader dataReader = new DataReader(15, 5);

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
        DecisionTreeAlgorithm decisionTreeAlgorithm = new DecisionTreeAlgorithm();
        decisionTreeAlgorithm.init();
    }

    private void init() {
        Object[][] data = dataReader.getData();

    }

    private void classify() {

    }

    private void calculateEntropy() {

    }


}
