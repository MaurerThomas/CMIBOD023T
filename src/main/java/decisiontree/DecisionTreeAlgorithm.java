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
    private double priorProbabilityAll = 0;

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

    public void init() {
        priorProbabilityAll = (double) data.length - 1;

        calculatePriorProbability(data, TARGET_CLASS);

        Map<String, Integer> calculateFrequency = calculateFrequency(twoDArrayToList(data));

        setAllAttributes();

        setValueAttributesForAttributes();

        setFrequencyForValueAttributes(calculateFrequency);

        setTargetClassFrequenciesForEveryValueAttribute();

        setEntropyForValueAttributes();

        calculateAttributeGain();

    }

    Attribute getMaximumInformationGain(List<Attribute> attributes) {
        Attribute bestAttribute = null;
        double bestGain = Double.NEGATIVE_INFINITY;

        for (Attribute attribute : attributes) {
            if (attribute.getGain() > bestGain) {
                bestGain = attribute.getGain();
                bestAttribute = attribute;
            }
        }
        return bestAttribute;
    }

    private void calculateAttributeGain() {
        for (Attribute attribute : attributes) {
            Double attributeEntropy = calculateEntropyForTwoAttributes(attribute);
            Double targetClassEntropy = calculateEntropyForOneAttribute(priorProbabilityFirstClass, priorProbabilitySecondClass);
            attribute.setGain(targetClassEntropy - attributeEntropy);
        }
    }

    private void setEntropyForValueAttributes() {
        for (int i = 1; i < data.length; i++) {
            for (int j = 0; j < data[i].length - 1; j++) {
                Map<String, ValueAttribute> valueAttributeMap = attributes.get(j).getValueAttributeMap();
                for (Map.Entry<String, ValueAttribute> mapEntry : valueAttributeMap.entrySet()) {
                    ValueAttribute valueAttribute = mapEntry.getValue();
                    Double frequencyYes = valueAttribute.getTargetClassesFrequency().get(CLASS_ONE);
                    Double frequencyNo = valueAttribute.getTargetClassesFrequency().get(CLASS_TWO);

                    Double entropy = calculateEntropyForOneAttribute(frequencyNo, frequencyYes);
                    valueAttribute.setEntropy(entropy);
                }
            }
        }
    }

    private void setTargetClassFrequenciesForEveryValueAttribute() {
        // Calculate frequency for every valueAttribute for every class
        for (int i = 1; i < data.length; i++) {
            for (int j = 0; j < data[i].length - 1; j++) {
                ValueAttribute valueAttribute = attributes.get(j).getValueAttributeMap().get(data[i][j].toString());
                if (data[i][TARGET_CLASS].equals(CLASS_ONE)) {
                    valueAttribute.getTargetClassesFrequency().put(CLASS_ONE, valueAttribute.getTargetClassesFrequency().get(CLASS_ONE) + 1d);
                } else {
                    valueAttribute.getTargetClassesFrequency().put(CLASS_TWO, valueAttribute.getTargetClassesFrequency().get(CLASS_TWO) + 1d);
                }
            }
        }
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
                    valueAttribute.getTargetClassesFrequency().put(CLASS_ONE, 0d);
                    valueAttribute.getTargetClassesFrequency().put(CLASS_TWO, 0d);
                    attribute.getValueAttributeMap().put(valueAttribute.getName(), valueAttribute);

                }
            }
        }
    }

    private void setAllAttributes() {
        //Collect all attributes from data
        List<Object> attributesFromData = Arrays.asList(data[0]);

        for (int i = 0; i < attributesFromData.size(); i++) {
            if (i != TARGET_CLASS) {
                Attribute attribute = new Attribute();
                attribute.setName(attributesFromData.get(i).toString());
                attributes.add(attribute);
            }
        }

    }

    private double calculateEntropyForOneAttribute(double frequencyOne, double frequencyTwo) {
        double entropy = 0;
        if (frequencyOne > 0 && frequencyTwo > 0) {
            double percentageOne = frequencyOne / (frequencyOne + frequencyTwo);
            double percentageTwo = frequencyTwo / (frequencyOne + frequencyTwo);

            entropy = -(percentageOne * log2(percentageOne)) - (percentageTwo * log2(percentageTwo));
        }


        return entropy;
    }

    private double calculateEntropyForTwoAttributes(Attribute attribute) {
        Map<String, ValueAttribute> attributeMap = attribute.getValueAttributeMap();
        double entropy = 0;

        for (Map.Entry<String, ValueAttribute> mapEntry : attributeMap.entrySet()) {
            ValueAttribute valueAttribute = mapEntry.getValue();
            entropy += (valueAttribute.getFrequency() / priorProbabilityAll) * valueAttribute.getEntropy();
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

    public List<Attribute> getAttributes() {
        return attributes;
    }
}
