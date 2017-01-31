package decisiontree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DecisionTreeAlgorithm {
    private static final int TARGET_CLASS = 0;
    private static final String CLASS_ONE = "e";
    private static final String CLASS_TWO = "p";
    private static final double LN2 = Math.log(2);
    private double priorProbabilityFirstClass = 0;
    private double priorProbabilitySecondClass = 0;
    private List<Attribute> attributes = new ArrayList<>();
    private double priorProbabilityAll = 0;
    private boolean runFirstTime = true;

    static int getTargetClass() {
        return TARGET_CLASS;
    }

    private static double log2(double value) {
        return Math.log(value) / LN2;
    }

    private static List<Map<String, Integer>> calculateFrequency(Object[][] dataset, int headerSize) {
        List<Map<String, Integer>> frequencies = new ArrayList<>();
        for (int i = 0; i < headerSize; i++) {
            frequencies.add(new HashMap<>());
        }

        for (int i = 0; i < dataset.length; i++) {
            for (int j = 0; j < dataset[i].length; j++) {
                Map<String, Integer> column = frequencies.get(j);
                if (column.containsKey(dataset[i][j].toString())) {
                    column.put(dataset[i][j].toString(), column.get(dataset[i][j].toString()) + 1);
                } else {
                    column.put(dataset[i][j].toString(), 1);
                }

            }
        }
        return frequencies;
    }

    void init(Object[][] dataset, List<String> header) {

        List<Map<String, Integer>> calculateFrequency = calculateFrequency(dataset, header.size());

        priorProbabilityAll = (double) dataset.length;

        calculatePriorProbability(dataset, TARGET_CLASS);

        if (runFirstTime) {
            priorProbabilityAll = (double) dataset.length - 1;
            dataset = setAllAttributes(dataset, header);
            setValueAttributesForAttributes(dataset);
            runFirstTime = false;
        }

        setFrequencyForValueAttributes(calculateFrequency, header);

        setTargetClassFrequenciesForEveryValueAttribute(dataset);

        setEntropyForValueAttributes(dataset);

        calculateAttributeGain();

        clearTargetClassesFrequency();

        priorProbabilityFirstClass = 0;
        priorProbabilitySecondClass = 0;
    }

    private void calculatePriorProbability(Object[][] dataset, int columnIndex) {
        // Calculate prior probability
        for (int i = 0; i < dataset.length; i++) {
            if (CLASS_ONE.equals(dataset[i][columnIndex])) {
                priorProbabilityFirstClass += 1;
            } else if (CLASS_TWO.equals(dataset[i][columnIndex])) {
                priorProbabilitySecondClass += 1;
            }
        }
    }

    private Object[][] setAllAttributes(Object[][] dataset, List<String> header) {
        for (int i = 0; i < header.size(); i++) {
            if (i != TARGET_CLASS) {
                Attribute attribute = new Attribute();
                attribute.setName(header.get(i));
                attributes.add(attribute);
            }
        }
        return dataset;
    }

    private void setValueAttributesForAttributes(Object[][] dataset) {
        // Set all valueAttributes in valueAttributeMap
        int index = 0;
        for (int i = 0; i < dataset.length; i++) {
            for (int j = 0; j < dataset[i].length; j++) {
                if (j != TARGET_CLASS) {
                    ValueAttribute valueAttribute = new ValueAttribute();
                    Attribute attribute = attributes.get(index);

                    valueAttribute.setName(dataset[i][j].toString());
                    valueAttribute.getTargetClassesFrequency().put(CLASS_ONE, 0d);
                    valueAttribute.getTargetClassesFrequency().put(CLASS_TWO, 0d);
                    attribute.getValueAttributeMap().put(valueAttribute.getName(), valueAttribute);
                    index++;

                }
            }
            index = 0;
        }
    }

    private void setFrequencyForValueAttributes(List<Map<String, Integer>> calculateFrequency, List<String> headers) {
        //Foreach attribute in attributes
        for (Attribute attribute : attributes) {
            //Get valueAttributeMap
            //Foreach valueAttribute in valueAttributeMap
            for (Map.Entry<String, ValueAttribute> mapEntry : attribute.getValueAttributeMap().entrySet()) {
                //Set Frequency valueAttribute
                int frequency = 0;
                Map<String, Integer> frequencies = calculateFrequency.get(headers.indexOf(attribute.getName()));
                if (frequencies.containsKey(mapEntry.getKey())) {
                    frequency = frequencies.get(mapEntry.getKey());
                }
                mapEntry.getValue().setFrequency(frequency);
            }
        }
    }

    private void setTargetClassFrequenciesForEveryValueAttribute(Object[][] dataset) {
        // Calculate frequency for every valueAttribute for every class
        for (int i = 0; i < dataset.length; i++) {
            for (int j = 0; j < dataset[i].length; j++) {
                for (Attribute attribute : attributes) {
                    ValueAttribute valueAttribute = attribute.getValueAttributeMap().get(dataset[i][j].toString());
                    if (valueAttribute != null) {
                        if (dataset[i][TARGET_CLASS].equals(CLASS_ONE)) {
                            valueAttribute.getTargetClassesFrequency().put(CLASS_ONE, valueAttribute.getTargetClassesFrequency().get(CLASS_ONE) + 1d);
                        } else {
                            valueAttribute.getTargetClassesFrequency().put(CLASS_TWO, valueAttribute.getTargetClassesFrequency().get(CLASS_TWO) + 1d);
                        }
                    }
                }
            }
        }
    }

    private void setEntropyForValueAttributes(Object[][] dataset) {
        for (int i = 0; i < dataset.length; i++) {
            for (int j = 0; j < dataset[i].length; j++) {
                for (Attribute attribute : attributes) {
                    Map<String, ValueAttribute> valueAttributeMap = attribute.getValueAttributeMap();
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
    }

    private void calculateAttributeGain() {
        for (Attribute attribute : attributes) {
            Double attributeEntropy = calculateEntropyForTwoAttributes(attribute);
            Double targetClassEntropy = calculateEntropyForOneAttribute(priorProbabilityFirstClass, priorProbabilitySecondClass);
            attribute.setGain(targetClassEntropy - attributeEntropy);
        }
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

    private double calculateEntropyForOneAttribute(double frequencyOne, double frequencyTwo) {
        double entropy = 0;

        if (frequencyOne > 0 && frequencyTwo > 0) {
            double percentageOne = frequencyOne / (frequencyOne + frequencyTwo);
            double percentageTwo = frequencyTwo / (frequencyOne + frequencyTwo);

            entropy = -(percentageOne * log2(percentageOne)) - (percentageTwo * log2(percentageTwo));
        }
        return entropy;
    }

    private void clearTargetClassesFrequency() {
        for (Attribute atrribute : attributes) {
            for (Map.Entry<String, ValueAttribute> mapEntry : atrribute.getValueAttributeMap().entrySet()) {
                ValueAttribute valuteAttribute = mapEntry.getValue();
                valuteAttribute.getTargetClassesFrequency().put(CLASS_ONE, 0d);
                valuteAttribute.getTargetClassesFrequency().put(CLASS_TWO, 0d);
            }
        }
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

    Object[][] getSubsetForValueAttribute(ValueAttribute valueAttribute, Object[][] dataset) {
        if (valueAttribute.getFrequency() > 0) {
            Object[][] subset = new Object[(int) valueAttribute.getFrequency()][];
            int index = 0;
            for (int i = 0; i < dataset.length; i++) {
                for (int j = 0; j < dataset[i].length; j++) {
                    if (index >= subset.length) {
                        return subset;
                    }
                    if (dataset[i][j].equals(valueAttribute.getName())) {
                        subset[index++] = dataset[i];
                    }
                }
            }
        }
        return null;

    }

    String getTargetClassForLeaf(ValueAttribute valueAttribute, Object[][] dataset) {
        for (int i = 0; i < dataset.length; i++) {
            for (int j = 0; j < dataset[i].length; j++) {
                if (dataset[i][j].equals(valueAttribute.getName())) {
                    if (dataset[i][TARGET_CLASS].equals(CLASS_ONE)) {
                        return CLASS_ONE;
                    } else {
                        return CLASS_TWO;
                    }
                }
            }
        }
        return null;
    }

    List<Attribute> getAttributes() {
        return attributes;
    }

}
