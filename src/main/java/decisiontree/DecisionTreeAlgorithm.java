package decisiontree;

import util.DataReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DecisionTreeAlgorithm {
    private static final int TARGET_CLASS = 4;
    private static final String CLASS_ONE = "Yes";
    private static final String CLASS_TWO = "No";
    private static final double LN2 = Math.log(2);
    private double priorProbabilityFirstClass = 0;
    private double priorProbabilitySecondClass = 0;
    private DataReader dataReader = new DataReader(15, 5);
    private List<Attribute> attributes = new ArrayList<>();
    private Object[][] data = dataReader.getData();

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

    private static double log2(double value) {
        return Math.log(value) / LN2;
    }

    private static Map<String, Integer> calculateFrequency(List<String> terms) {
        return terms.parallelStream().
                flatMap(s -> Arrays.asList(s.split(" ")).stream()).
                collect(Collectors.toConcurrentMap(String::toString, w -> 1, Integer::sum));
    }

    private static <T> List<T> twoDArrayToList(Object[][] twoDArray) {
        List<T> list = new ArrayList<>();
        for (Object[] array : twoDArray) {
            list.addAll(Arrays.asList((T[]) array));
        }
        return list;
    }

    private void init() {
        calculatePriorProbability(data, TARGET_CLASS);

        calculateEntropyForOneAttribute(priorProbabilityFirstClass, priorProbabilitySecondClass);

        setAllAttributes();

        setValueAttributesForAttributes();

        Map<String, Integer> calculateFrequency = calculateFrequency(twoDArrayToList(data));

        setFrequencyForValueAttributes(calculateFrequency);


        // Calculate frequency for every valueAttribute for every class
        int countYes = 0;
        int countNo = 0;
        for (int i = 1; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {

                if (data[i][j].equals(mapEntry.getKey()) && data[i][TARGET_CLASS].equals(CLASS_ONE)) {
                    countYes += 1;
                } else {
                    countNo += 1;
                }

            }
        }

        //calculateEntropyForOneAttribute()
        //Set entropy for valueAttribute

        //Foreach attribute in attributes
            //calculateEntropyForTwoAttributes()
        //End foreach
    }

    private void setFrequencyForValueAttributes(Map<String, Integer> calculateFrequency) {
        //Foreach attribute in attributes
        for (Attribute attribute : attributes) {
            //Get valueAttributeMap
            //Foreach valueAttribute in valueAttributeMap
            for (Map.Entry<String, ValueAttribute> mapEntry : attribute.getValueAttributeMap().entrySet()) {
                //Set Frequency valueAttribute
                int frequency = calculateFrequency.get(mapEntry.getKey());
                mapEntry.getValue().setFrequency(frequency);
            }
        }
    }

    private void setValueAttributesForAttributes() {
        // Skip attributes from dataset
        // Set all valueAttributes in valueAttributeMap
        for (int i = 1; i < data.length; i++) {
            for (int j = 0; j < attributes.size(); j++) {
                if (i != TARGET_CLASS) {
                    ValueAttribute valueAttribute = new ValueAttribute();
                    Attribute attribute = attributes.get(j);

                    valueAttribute.setName(data[i][j].toString());
                    attribute.getValueAttributeMap().put(valueAttribute.getName(), valueAttribute);

                }
            }
        }
    }

    private void setAllAttributes() {
        //Collect all attributes from data
        for (int i = 0; i < data[0].length; i++) {
            if (i != TARGET_CLASS) {
                Attribute attribute = new Attribute();
                attribute.setName(data[i].toString());
                attributes.add(attribute);
            }
        }
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
