package decisiontree;

import util.DataReader;

import java.util.Map;

public class DecisionTreeAlgorithm {
    private static final int TARGET_CLASS = 4;
    private static final String CLASS_ONE = "Yes";
    private static final String CLASS_TWO = "No";
    private double priorProbabilityFirstClass = 0;
    private double priorProbabilitySecondClass = 0;
    private DataReader dataReader = new DataReader(15, 5);
    private static final double LN2 = Math.log(2);

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
        calculatePriorProbability(data, TARGET_CLASS);
        calculateEntropyForOneAttribute(priorProbabilityFirstClass, priorProbabilitySecondClass);

        //Read CSV
        //Collect all attributes (outlook, humidity etc) from data
        //Set all valueAttributes invalueAttributeMap

        //Foreach attribute in attributes
            //Get valueAttributeMap
            //Foreach valueAttribute in valueAttributeMap
                //Set Frequency valueAttribute
            //End foreach
        // End foreach

        //calculateEntropyForOneAttribute()
        //Set entropy for valueAttribute

        //Foreach attribute in attributes
            //calculateEntropyForTwoAttributes()
        //End foreach
    }

    private void classify() {

    }


    private double calculateEntropyForOneAttribute(double frequencyOne, double frequencyTwo) {
        double entropy;
        double percentageOne = frequencyOne / (frequencyOne + frequencyTwo);
        double percentageTwo = frequencyTwo / (frequencyOne + frequencyTwo);

        entropy = -(percentageOne*log2(percentageOne))-(percentageTwo*log2(percentageTwo));

        return entropy;
    }

    private double calculateEntropyForTwoAttributes(Attribute attribute) {
        Map<String, ValueAttribute> attributeMap = attribute.getValueAttributeMap();
        double entropy = 0;

        for (Map.Entry<String, ValueAttribute> mapEntry : attributeMap.entrySet()) {
            ValueAttribute valueAttribute = mapEntry.getValue();
            entropy += valueAttribute.getFrequency() * valueAttribute.getEntropy();
        }
        return entropy;
    }

    private static double log2(double value) {
        return Math.log(value) / LN2;
    }

    private void calculatePriorProbability(Object[][] array, int columnIndex) {
        // Calculate prior probability
        for (int i = 1; i < array.length; i++) {
            if (CLASS_ONE.equals(array[i][columnIndex])) {
                priorProbabilityFirstClass += 1;
            } else if (CLASS_TWO.equals(array[i][columnIndex])) {
                priorProbabilitySecondClass += 1;
            }
        }
    }


}
