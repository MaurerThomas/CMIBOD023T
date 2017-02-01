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
    private double priorProbabilityAll = 0;

    static int getTargetClass() {
        return TARGET_CLASS;
    }

    private static double log2(double value) {
        return Math.log(value) / LN2;
    }

    void init(Object[][] dataset, List<Attribute> attributes) {
        priorProbabilityAll = (double) dataset.length;

        calculatePriorProbability(dataset);

        setValueAttributesForAttributes(dataset, attributes);

        setEntropyForValueAttributes(attributes);

        calculateAttributeGain(attributes);

        clearTargetClassesFrequency(attributes);

        priorProbabilityFirstClass = 0;
        priorProbabilitySecondClass = 0;
    }

    private void calculatePriorProbability(Object[][] dataset) {
        // Calculate prior probability
        for (Object[] row : dataset) {
            if (CLASS_ONE.equals(row[TARGET_CLASS])) {
                priorProbabilityFirstClass += 1;
            } else if (CLASS_TWO.equals(row[TARGET_CLASS])) {
                priorProbabilitySecondClass += 1;
            }
        }
    }

    public List<Attribute> getAllAttributes(List<String> header) {
        List<Attribute> attributes = new ArrayList<>();
        for (int i = 0; i < header.size(); i++) {
            if (i != TARGET_CLASS) {
                Attribute attribute = new Attribute();
                attribute.setIndex(i);
                attributes.add(attribute);
            }
        }
        return attributes;
    }

    private void setValueAttributesForAttributes(Object[][] dataset, List<Attribute> attributes) {
        // Set all valueAttributes in valueAttributeMap
        Map<Attribute, Map<String, ValueAttribute>> kaas = new HashMap<>();
        for (Object[] row : dataset) {
            for (Attribute attribute : attributes) {
                Map<String, ValueAttribute> valueAttributeMap = kaas.get(attribute);
                String valueAttributeName = row[attribute.getIndex()].toString();

                if (valueAttributeMap == null) {
                    valueAttributeMap = new HashMap<>();
                    kaas.put(attribute, valueAttributeMap);
                }
                ValueAttribute valueAttribute = valueAttributeMap.get(valueAttributeName);

                if (valueAttribute == null) {
                    valueAttribute = new ValueAttribute();
                    valueAttribute.setIndex(attribute.getIndex());
                    valueAttribute.setName(valueAttributeName);
                    valueAttribute.getTargetClassesFrequency().put(CLASS_ONE, 0);
                    valueAttribute.getTargetClassesFrequency().put(CLASS_TWO, 0);
                    attribute.getValueAttributeSet().add(valueAttribute);

                    valueAttributeMap.put(valueAttributeName, valueAttribute);
                }
                valueAttribute.getTargetClassesFrequency().put(row[getTargetClass()].toString(), valueAttribute.getTargetClassesFrequency().get(row[getTargetClass()].toString()) + 1);
                valueAttribute.setFrequency(valueAttribute.getFrequency() + 1);
            }
        }
    }

    private void setEntropyForValueAttributes(List<Attribute> attributes) {
        for (Attribute attribute : attributes) {
            for (ValueAttribute valueAttribute : attribute.getValueAttributeSet()) {
                Integer frequencyYes = valueAttribute.getTargetClassesFrequency().get(CLASS_ONE);
                Integer frequencyNo = valueAttribute.getTargetClassesFrequency().get(CLASS_TWO);

                Double entropy = calculateEntropyForOneAttribute(frequencyNo, frequencyYes);
                valueAttribute.setEntropy(entropy);
            }
        }
    }

    private void calculateAttributeGain(List<Attribute> attributes) {
        for (Attribute attribute : attributes) {
            Double attributeEntropy = calculateEntropyForTwoAttributes(attribute);
            Double targetClassEntropy = calculateEntropyForOneAttribute(priorProbabilityFirstClass, priorProbabilitySecondClass);
            attribute.setGain(targetClassEntropy - attributeEntropy);
        }
    }

    private double calculateEntropyForTwoAttributes(Attribute attribute) {
        double entropy = 0;

        for (ValueAttribute valueAttribute : attribute.getValueAttributeSet()) {
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

    private void clearTargetClassesFrequency(List<Attribute> attributes) {
        for (Attribute atrribute : attributes) {
            for (ValueAttribute valueAttribute : atrribute.getValueAttributeSet()) {
                valueAttribute.getTargetClassesFrequency().put(CLASS_ONE, 0);
                valueAttribute.getTargetClassesFrequency().put(CLASS_TWO, 0);
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
        Object[][] subset;
        List<Object[]> list = new ArrayList<>();
        for (Object[] row : dataset) {
            if (row[valueAttribute.getIndex()].equals(valueAttribute.getName())) {
                list.add(row);
            }
        }
        subset = new Object[list.size()][];
        list.toArray(subset);
        return subset;

    }

    String getTargetClassForLeaf(ValueAttribute valueAttribute, Object[][] dataset) {
        for (Object[] row : dataset) {
            if (row[valueAttribute.getIndex()].equals(valueAttribute.getName())) {
                return row[TARGET_CLASS].toString();
            }
        }
        return null;
    }


}
