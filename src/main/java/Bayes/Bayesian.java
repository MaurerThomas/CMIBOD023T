package Bayes;

import util.DataReader;

import java.util.*;

public class Bayesian {
    private static final int TARGET_CLASS = 0;
    private final DataReader dataReader = new DataReader("mushrooms.csv");

    public static void main(String[] args) {
        Bayesian bayesian = new Bayesian();
        bayesian.init();
    }

    private void init() {
        double correct = 0;
        Object[][] dataset = dataReader.getData();
        Object[][] trainingData = dataReader.createTrainingDataSet();

        Map<Object, TargetClassFeature> targetClassFeatures = setTargetClassFeatures(trainingData);
        List<Feature> features = createFeatures(trainingData, targetClassFeatures);


        for (Object[] row : dataset) {
            TargetClassFeature indexedClass = classify(Arrays.asList(row), features, targetClassFeatures);

            if (indexedClass.getName().equals(row[TARGET_CLASS])) {
                correct++;
            }
        }
        System.out.println("Accuracy: " + (correct / dataset.length));

    }


    private TargetClassFeature classify(List<Object> featuresToClassify, List<Feature> features, Map<Object, TargetClassFeature> targetClassFeatures) {
        double bestP = Double.NEGATIVE_INFINITY;
        double prob;
        TargetClassFeature best = null;

        for (Map.Entry<Object, TargetClassFeature> mapEntry : targetClassFeatures.entrySet()) {
            TargetClassFeature targetClassFeature = mapEntry.getValue();
            prob = (double) targetClassFeature.getFrequency() / (double) targetClassFeature.getTotalTargetClassesFrequency();

            for (int i = 0; i < featuresToClassify.size(); i++) {
                Object featureToClassify = featuresToClassify.get(i);

                for (Feature feature : features) {
                    if (feature.getName().equals(featureToClassify.toString()) && feature.getIndex() == i) {

                        if (feature.getTargetClassFrequencies().containsKey(targetClassFeature)) {
                            prob = prob * feature.getTargetClassFrequencies().get(targetClassFeature) / targetClassFeature.getFrequency();
                        } else {
                            prob = 0;
                        }
                    }
                }
            }

            // Append probability with the best category in results.
            if (prob > bestP) {
                bestP = prob;
                best = targetClassFeature;
            }
        }

        return best;
    }

    private Map<Object, TargetClassFeature> setTargetClassFeatures(Object[][] dataset) {
        Map<Object, TargetClassFeature> targetClassFeaturesMap = new HashMap<>();

        for (Object[] row : dataset) {
            if (targetClassFeaturesMap.containsKey(row[TARGET_CLASS])) {
                TargetClassFeature currentFeature = targetClassFeaturesMap.get(row[TARGET_CLASS]);
                currentFeature.setFrequency(currentFeature.getFrequency() + 1);
                targetClassFeaturesMap.put(row[TARGET_CLASS], currentFeature);
            } else {
                TargetClassFeature targetClassFeature = new TargetClassFeature();
                targetClassFeature.setName(row[TARGET_CLASS].toString());
                targetClassFeature.setFrequency(1);
                targetClassFeature.setTotalTargetClassesFrequency(dataset.length);
                targetClassFeaturesMap.put(row[TARGET_CLASS], targetClassFeature);
            }
        }
        return targetClassFeaturesMap;
    }

    private List<Feature> createFeatures(Object[][] dataset, Map<Object, TargetClassFeature> targetClassFeaturesMap) {
        List<Feature> features = new ArrayList<>();

        for (Object[] row : dataset) {
            for (int j = 0; j < row.length; j++) {
                Feature feature = new Feature();
                feature.setName(row[j].toString());
                feature.setIndex(j);

                if (!featuresContainsFeature(features, feature) && j != TARGET_CLASS) {
                    features.add(feature);
                    feature.getTargetClassFrequencies().put(targetClassFeaturesMap.get(row[TARGET_CLASS]), 1);
                } else {
                    Feature foundFeature = getFeatureFromfeatures(features, feature);
                    if (foundFeature.getTargetClassFrequencies().containsKey(targetClassFeaturesMap.get(row[TARGET_CLASS]))) {
                        foundFeature.getTargetClassFrequencies().put(targetClassFeaturesMap.get(row[TARGET_CLASS]), foundFeature.getTargetClassFrequencies().get(targetClassFeaturesMap.get(row[TARGET_CLASS])) + 1);
                    } else {
                        foundFeature.getTargetClassFrequencies().put(targetClassFeaturesMap.get(row[TARGET_CLASS]), 1);
                    }
                    foundFeature.setTotalFrequency(getTotalTargetClassFrequencies(foundFeature.getTargetClassFrequencies()));
                }

            }
        }

        return features;
    }

    private int getTotalTargetClassFrequencies(Map<TargetClassFeature, Integer> targetClassFrequencies) {
        int totalFrequency = 0;

        for (int value : targetClassFrequencies.values()) {
            totalFrequency += value;
        }

        return totalFrequency;
    }

    private Feature getFeatureFromfeatures(List<Feature> features, Feature targetFeature) {
        for (Feature feature : features) {
            if (feature.getName().equals(targetFeature.getName()) && feature.getIndex() == targetFeature.getIndex()) {
                return feature;
            }
        }
        return new Feature();
    }

    /**
     * Checks if a Feature exists in a list of Feature objects, where the feature index is not the same.
     *
     * @param features      A list of features
     * @param targetFeature The Feature to search for
     * @return true/false
     */
    private boolean featuresContainsFeature(List<Feature> features, Feature targetFeature) {
        for (Feature feature : features) {
            if (feature.getName().equals(targetFeature.getName()) && feature.getIndex() == targetFeature.getIndex()) {
                return true;
            }
        }
        return false;
    }

}