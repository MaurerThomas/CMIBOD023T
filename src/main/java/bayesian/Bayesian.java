package bayesian;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bayesian {
    private static final int FEATURE_PLAYINDOOR = 4;
    private static File file = new File("data.csv");
    private static Object[][] data = new Object[15][5];
    private static List<String[]> dataList = new ArrayList<>();
    private Logger logger = Logger.getLogger("myLogger");
    private double priorProbabilityFirstClass = 0;
    private double priorProbabilitySecondClass = 0;

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
        Map<Object, Map<Integer, Map<Object, Double>>> trainedData = trainDataset(data, FEATURE_PLAYINDOOR);
    }

    private Map<Object, Map<Integer, Map<Object, Double>>> trainDataset(Object[][] array, int columnIndex) {
        Map<Object, Map<Integer, Map<Object, Double>>> classes = new HashMap<>();

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

    private double getPercentageOfTotal(Object anObject) {
        double percentageOfTotal = 0;

        if ("Yes".equals(anObject)) {
            percentageOfTotal = 1 / priorProbabilityFirstClass;
        } else if ("No".equals(anObject)) {
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
            if ("Yes".equals(array[i][columnIndex])) {
                priorProbabilityFirstClass += 1;
            } else if ("No".equals(array[i][columnIndex])) {
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
            dataList.add(line.split(delimiter));
        }
        sc.close();
    }
}
