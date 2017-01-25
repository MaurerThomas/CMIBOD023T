package util;

import dbscan.Point;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataReader {
    private static File file = new File("stars.csv");
    private Object[][] data;

    public DataReader(int rowSize, int columnSize) {
        data = new Object[rowSize][columnSize];

        try {
            readMyData();

//            for (Object[] row : data) {
//                printRow(row);
//            }
        } catch (FileNotFoundException e) {
            Logger logger = Logger.getLogger("myLogger");
            logger.log(Level.SEVERE, "Could not find the file: ", e);
        }
    }

    public DataReader() {
    }

    private static void printRow(Object[] row) {
        for (Object i : row) {
            System.out.format("%15s", i);
        }
        System.out.println();
    }

    private void readMyData() throws FileNotFoundException {
        String delimiter = ",";
        Scanner sc = new Scanner(file);
        int index = 0;

        //sc.nextLine();
        while (sc.hasNextLine()) {

            String line = sc.nextLine();
            data[index++] = line.split(delimiter);
        }
        sc.close();
    }

    public List<Point> parseStars() throws FileNotFoundException {
        List<Point> stars = new ArrayList<>();

        try (Scanner sc = new Scanner(file)) {
            sc.nextLine();
            while (sc.hasNextLine()) {
                String[] values = sc.nextLine().split(",");
                stars.add(new Point(Arrays.asList(Double.parseDouble(values[0]), Double.parseDouble(values[1]), Double.parseDouble(values[2]))));
            }

        }
        return stars;
    }

    public Object[][] getData() {
        return data;
    }

    public Object[][] createTrainingDataSet() {
        int trainingDataSetSize = data.length / 3;
        Set<Object[]> subset = new HashSet<>(trainingDataSetSize);
        Random random = new Random();

        for (int i = 0; i < trainingDataSetSize; i++) {
            int index = random.nextInt(data.length);
            Object[] mushroom = data[index];
            while (subset.contains(mushroom)) {
                index = (index + 1) % data.length;
                mushroom = data[index];
            }
            subset.add(mushroom);
        }
        return subset.toArray(new Object[subset.size()][]);
    }

}
