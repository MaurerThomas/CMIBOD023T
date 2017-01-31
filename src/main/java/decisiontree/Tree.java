package decisiontree;

import util.DataReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static decisiontree.DecisionTreeAlgorithm.getTargetClass;

public class Tree {
    private DecisionTreeAlgorithm decisionTreeAlgorithm = new DecisionTreeAlgorithm();
    private List<Attribute> attributes = new ArrayList<>();
    private TreeNode root = new TreeNode();
    private DataReader dataReader = new DataReader();

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
        double correct = 0;
        Object[][] dataset = dataReader.getData();
        Object[][] trainingData = dataReader.createTrainingDataSet();

        decisionTreeAlgorithm.init(trainingData, dataReader.getHeader());
        attributes = decisionTreeAlgorithm.getAttributes();

        buildDecisionTree(root, trainingData);
        //root.printTree();

        for (int i = 1; i < dataset.length; i++) {
            Object indexedClass = classify(Arrays.asList(dataset[i]), dataReader.getHeader());
            if (indexedClass != null) {
                if (indexedClass.equals(dataset[i][getTargetClass()])) {
                    correct++;
                }
            }
        }
        System.out.println("Accuracy: " + (correct / (dataset.length - 1)));
    }

    private Object classify(List<Object> features, List<String> header) {
        TreeNode treeNode = root;
        while (treeNode != null) {
            if (treeNode.isLeaf()) {
                return treeNode.getTargetClass();
            }
            int index = header.indexOf(treeNode.getLabel());
            if (index < 0) {
                throw new IllegalArgumentException(treeNode.getLabel() + " not found");

            }
            Object feature = features.get(index);
            treeNode = treeNode.getChildren().get(feature.toString());

        }
        throw new NullPointerException("Cannot classify tree. Need more data");
    }

    private void buildDecisionTree(TreeNode currentNode, Object[][] dataset) {
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
                leafNode.setTargetClass(decisionTreeAlgorithm.getTargetClassForLeaf(currentValueAttribute, dataset));
                currentNode.getChildren().put(currentValueAttributeName, leafNode);
            } else {
                Object[][] subset = decisionTreeAlgorithm.getSubsetForValueAttribute(currentValueAttribute, dataset);
                decisionTreeAlgorithm.init(subset, dataReader.getHeader());
                TreeNode newNode = new TreeNode();
                currentNode.getChildren().put(currentValueAttributeName, newNode);
                // Subset meegeven
                buildDecisionTree(newNode, subset);
            }
        }
    }
}
