package bayesian;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bayesian {
    private static final int FEATURE_PLAYINDOOR = 4;
    private static final String CLASS_ONE = "Yes";
    private static final String CLASS_TWO = "No";
    private static File file = new File("data.csv");
    private static Object[][] data = new Object[15][5];
    private Logger logger = Logger.getLogger("myLogger");
    private double priorProbabilityFirstClass = 0;
    private double priorProbabilitySecondClass = 0;
    private double priorProbabilityAll = 0;
    private Map<Object, Map<Integer, Map<Object, Double>>> classes = new HashMap<>();

    private Bayesian() {
    }

    private static void printRow(Object[] row) {
        for (Object i : row) {
            System.out.format("%15s", i);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Bayesian bayesian = new Bayesian();
        bayesian.init();

    }

    private void init() {
        try {
            readMyData();
            for (Object[] row : data) {
                printRow(row);
            }
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Could not find the file: ", e);
        }
        trainDataset(data, FEATURE_PLAYINDOOR);
        ArrayList<Object> features = new ArrayList<>();
        features.add("Sunny");
        features.add("Cool");
        features.add("High");
        features.add("True");
        System.out.println("");
        System.out.println("Highest probability class: " + classify(features));
    }

    private Map<Object, Map<Integer, Map<Object, Double>>> trainDataset(Object[][] array, int columnIndex) {
        initPossibleClasses(array, columnIndex, classes);
        calculatePriorProbability(array, columnIndex);

        // Calculate conditional probabilities for every feature.
        for (int i = 1; i < array.length; i++) {
            Map<Integer, Map<Object, Double>> features = new HashMap<>();
            for (int j = 0; j < array[i].length; j++) {
                Map<Object, Double> featureWithValues = new HashMap<>();

                // Skip column 4(classes)
                if (j == columnIndex) {
                    continue;
                }

                // Calculates conditional probability for this feature where class is "Yes" or "No".
                double percentageOfTotal = getPercentageOfTotal(array[i][columnIndex]);

                // Check if class exists (Yes/No). Yes then check if feature exists. No then add the class and feature.
                if (classes.get(array[i][columnIndex]) != null && classes.containsKey(array[i][columnIndex])) {
                    Map<Integer, Map<Object, Double>> currentFeatures;
                    currentFeatures = classes.get(array[i][columnIndex]);

                    // Check if feature exists, if yes then get the Map of this feature.
                    Map<Object, Double> currentValues = currentFeatures.get(j);
                    //If the feature exists, update the percentage.
                    if (currentFeatures.containsKey(j) && currentValues.containsKey(array[i][j])) {
                        percentageOfTotal += currentValues.get(array[i][j]);

                        if (currentValues.size() > 1) {
                            currentValues.put(array[i][j], percentageOfTotal);
                            features.put(j, currentValues);
                            continue;
                        }
                    } else {
                        currentValues.put(array[i][j], percentageOfTotal);
                        features.put(j, currentValues);
                        continue;
                    }

                }
                featureWithValues.put(array[i][j], percentageOfTotal);
                features.put(j, featureWithValues);
            }
            classes.put(array[i][columnIndex], features);
        }

        return classes;
    }

    private Object classify(ArrayList<Object> features) {
        // Store all the probabilities
        Map<Object, Double> results = new HashMap<>();

        // For every feature in features that exists in classes map for every category take that value and multiply for every feature.
        for (Map.Entry<Object, Map<Integer, Map<Object, Double>>> entry : classes.entrySet()) {
            Object priorKey = entry.getKey();
            double prop;

            if (CLASS_ONE.equals(priorKey)) {
                prop = priorProbabilityFirstClass / priorProbabilityAll;
            } else {
                prop = priorProbabilitySecondClass / priorProbabilityAll;
            }

            int col = 0;
            for (Object feature : features) {

                Map<Integer, Map<Object, Double>> columns = entry.getValue();
                Map<Object, Double> attributesMap = columns.get(col);
                if (attributesMap.containsKey(feature)) {
                    // Save probability in a variable
                    double attribute = attributesMap.get(feature);
                    prop = prop * attribute;
                    col += 1;
                } else {
                    // we did not find any instances of this attribute value
                    // occurring with this category so prob = 0
                    prop = 0;
                }
                // Append probability with the category in results.
                results.put(priorKey, prop);
            }
        }
        printResults(results);
        Map<Object, Double> normalizedResults = normalize(results);
        printResults(normalizedResults);
        // return the category with the highest probability
        return getMaxValueFromMap(normalizedResults).getKey();
    }

    private void printResults(Map<Object, Double> results) {
        System.out.println("Probabilities:");
        for (Map.Entry<Object, Double> entry : results.entrySet()) {
            System.out.println("Class: " + entry.getKey() + " P: " + entry.getValue());
        }

    }

    private Map<Object, Double> normalize(Map<Object, Double> inputResults) {
        double yes = 0;
        double no = 0;

        for (Map.Entry<Object, Double> entry : inputResults.entrySet()) {
            if (entry.getKey().equals(CLASS_ONE)) {
                yes = entry.getValue();
            } else {
                no = entry.getValue();
            }
        }

        double denumerator = yes + no;
        for (Map.Entry<Object, Double> entry : inputResults.entrySet()) {
            if (entry.getKey().equals(CLASS_ONE)) {
                inputResults.put(CLASS_ONE, yes / denumerator);
            } else {
                inputResults.put(CLASS_TWO, no / denumerator);
            }
        }

        return inputResults;
    }

    private Map.Entry<Object, Double> getMaxValueFromMap(Map<Object, Double> results) {
        Map.Entry<Object, Double> maxEntry = null;
        for (Map.Entry<Object, Double> entry : results.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }
        return maxEntry;
    }

    private double getPercentageOfTotal(Object anObject) {
        double percentageOfTotal = 0;

        if (CLASS_ONE.equals(anObject)) {
            percentageOfTotal = 1 / priorProbabilityFirstClass;
        } else if (CLASS_TWO.equals(anObject)) {
            percentageOfTotal = 1 / priorProbabilitySecondClass;
        }
        return percentageOfTotal;
    }

    private void initPossibleClasses(Object[][] array, int columnIndex, Map<Object, Map<Integer, Map<Object, Double>>> classes) {
        // Start at 1 because of heading in csv and the data structure.
        for (int i = 1; i < array.length; i++) {
            if (!classes.containsKey(array[i][columnIndex])) {
                classes.put(array[i][columnIndex], null);
            }
        }
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

    private void readMyData() throws FileNotFoundException {
        String delimiter = ";";
        Scanner sc = new Scanner(file);
        int index = 0;

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            data[index++] = line.split(delimiter);
        }
        priorProbabilityAll = index - 1;
        sc.close();
    }
}
