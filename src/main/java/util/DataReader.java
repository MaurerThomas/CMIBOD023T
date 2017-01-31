package util;

import dbscan.Point;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataReader {
    private static final long SEED = 1;
    private static File file = new File("mushrooms.csv");
    private Object[][] data;
    private List<String> header;

    public DataReader() {
        try {
            readData();
        } catch (IOException e) {
            Logger logger = Logger.getLogger("myLogger");
            logger.log(Level.SEVERE, "Could not find the file: ", e);
        }
    }

    private void readData() throws IOException {
        String delimiter = ",";
        String line;
        BufferedReader r = new BufferedReader(new FileReader(file));
        List<String[]> list = new ArrayList<>();
        header = Arrays.asList(r.readLine().split(delimiter));

        while ((line = r.readLine()) != null)
            list.add(line.split(delimiter));

        data = new Object[list.size()][];
        list.toArray(data);

        r.close();
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

    public List<String> getHeader() {
        return header;
    }

}
