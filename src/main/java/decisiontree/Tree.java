package decisiontree;

import util.DataReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static decisiontree.DecisionTreeAlgorithm.getTargetClass;

public class Tree {
    private final DecisionTreeAlgorithm decisionTreeAlgorithm = new DecisionTreeAlgorithm();
    private final TreeNode root = new TreeNode();
    private final DataReader dataReader = new DataReader("mushrooms.csv");

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
        List<Attribute> attributes;
        double correct = 0;
        Object[][] dataset = dataReader.getData();
        Object[][] trainingData = dataReader.createTrainingDataSet();
        attributes = decisionTreeAlgorithm.getAllAttributes(dataReader.getHeader());

        decisionTreeAlgorithm.init(trainingData, attributes);

        buildDecisionTree(root, trainingData, attributes);
        root.printTree(root, dataReader.getHeader(), "");

        for (int i = 1; i < dataset.length; i++) {
            Object indexedClass = classify(Arrays.asList(dataset[i]));
            if (indexedClass != null) {
                if (indexedClass.equals(dataset[i][getTargetClass()])) {
                    correct++;
                }
            }
        }
        System.out.println("Accuracy: " + (correct / (dataset.length - 1)));
    }

    private Object classify(List<Object> features) {
        TreeNode treeNode = root;

        while (treeNode != null) {
            if (treeNode.isLeaf()) {
                return treeNode.getTargetClass();
            }

            Object feature = features.get(treeNode.getIndex());
            treeNode = treeNode.getChildren().get(feature.toString());

        }
        throw new NullPointerException("Cannot classify tree. Need more data");
    }

    private boolean hasMultipleTargetClasses(Object[][] dataset) {
        Object c = null;

        for (Object[] row : dataset) {
            Object currentClass = row[getTargetClass()];
            if (c == null) {
                c = currentClass;
            } else if (currentClass != c) {
                return true;
            }
        }

        return false;
    }

    private void buildDecisionTree(TreeNode currentNode, Object[][] dataset, List<Attribute> attributes) {
        if (!hasMultipleTargetClasses(dataset)) {
            currentNode.setLeaf(true);
            currentNode.setTargetClass(dataset[0][getTargetClass()].toString());
            return;
        }
        Attribute bestAttribute = decisionTreeAlgorithm.getMaximumInformationGain(attributes);

        if (bestAttribute != null) {
            currentNode.setIndex(bestAttribute.getIndex());

            for (ValueAttribute currentValueAttribute : bestAttribute.getValueAttributeSet()) {

                if (currentValueAttribute.getFrequency() == 0) {
                    continue;
                }
                if (currentValueAttribute.getEntropy() <= 0.0) {
                    TreeNode leafNode = new TreeNode();
                    leafNode.setIndex(currentValueAttribute.getIndex());
                    leafNode.setLeaf(true);
                    leafNode.setTargetClass(decisionTreeAlgorithm.getTargetClassForLeaf(currentValueAttribute, dataset));
                    if (leafNode.getTargetClass() != null) {
                        currentNode.getChildren().put(currentValueAttribute.getName(), leafNode);
                    }
                } else {
                    Object[][] subset = decisionTreeAlgorithm.getSubsetForValueAttribute(currentValueAttribute, dataset);
                    TreeNode newNode = new TreeNode();
                    List<Attribute> attributeSubset = new ArrayList<>(attributes);
                    attributeSubset.remove(bestAttribute);
                    decisionTreeAlgorithm.init(subset, attributeSubset);
                    currentNode.getChildren().put(currentValueAttribute.getName(), newNode);

                    // Subset meegeven
                    buildDecisionTree(newNode, subset, attributeSubset);
                }
            }
        }
    }
}
