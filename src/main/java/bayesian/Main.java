package bayesian;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class Main {
    private static File file = new File("data.csv");
    private static Object[][] data = new Object[15][5];
    private static List<String[]> dataList = new ArrayList<>();
    private static final int FEATURE_OUTLOOK = 0;
    private static final int FEATURE_TEMP = 1;
    private static final int FEATURE_HUMIDITTY = 2;
    private static final int FEATURE_WINDY = 3;
    private static final int FEATURE_PLAYINDOOR = 4;
    private static final int FEATURE_DAY = 5;


    private Main() {
    }

    private static void printRow(Object[] row) {
        for (Object i : row) {
            System.out.format("%15s", i);

        }
        System.out.println();
    }

    public static void main(String[] args) throws IOException {
        readMyData();

        for (Object[] row : data) {
            printRow(row);
        }

        // use extractFeatureFromDataset to extract the feature we want from the dataset.

        List<Object> yes = extractFeatureFromDataset(data, FEATURE_OUTLOOK, "Yes");
        List<Object> no =  extractFeatureFromDataset(data, FEATURE_OUTLOOK, "No");

        // Convert the 2D array from above to a list so we can use streams.
        List<String> terms = twoDArrayToList(data);
        // Frequency "table"


        // Likelihood "table"

        double sunnyNo = 0;
        double sunnyYes = 0;
        double output = 0;
        double output1 = 0;
        double total = 0;
        double output2;

        for (int i = 0; i < yes.size(); i++) {
            if (yes.get(i).equals("Sunny")) {
                sunnyYes = sunnyYes + 1;
                total = total + 1;
            }
            // P(x | c) = P(Sunny | Yes)
            output = sunnyYes / yes.size();
        }
        for (int i = 0; i < no.size(); i++) {
            if (no.get(i).equals("Sunny")) {
                sunnyNo = sunnyNo + 1;
                total = total + 1;
            }
            // P(x) = P(Sunny)
            output1 = total / (yes.size() + no.size());
        }

        output2 = (double) yes.size() / (yes.size() + no.size());


        System.out.println(output);
        System.out.println(output1);
        System.out.println(output2);


        // Posterior Probability = P(c | x) = P(Yes | Sunny)
        double posteriorProbability = output * output2 / output1;
        System.out.println(posteriorProbability);

    }

    private static Map<String, Integer> calculateFrequency(List<String> terms) {
        return terms.parallelStream().
                flatMap(s -> asList(s.split(" ")).stream()).
                collect(Collectors.toConcurrentMap(
                        String::toLowerCase, w -> 1, Integer::sum));
    }

    private static List extractFeatureFromDataset(Object[][] array, int columnIndex, String something) {
        List<Object> column = new ArrayList<>();
        Map<Object, List<Object>> properties = new LinkedHashMap<>();
            for(int j = 0 ; j < array[0].length; j++) {
                properties.put(array[0][j], null );
            }
        Object[] features = new Object[properties.size()];
        int p = 0;
        for (Map.Entry<Object, List<Object>> entry : properties.entrySet()) {
            Object key = entry.getKey();
            features[p] = key;
            p++;

            // now work with key and value...
        }

        for (int i = 0; i < properties.size(); i++) {
            Object waarde = array[i][columnIndex];
            if (!properties.containsValue(waarde)) {
                List<Object> currentValues = properties.get(features[i]);
                //properties.put(features[i], currentValues.add(waarde));
                int a = 5;
            }

        }




//            if (array[i][4].equals(something)) {
//                column.add(array[i][columnIndex]);
//            }



        return column;
    }

    private static <T> List<T> twoDArrayToList(Object[][] twoDArray) {
        List<T> list = new ArrayList<>();
        for (Object[] array : twoDArray) {
            list.addAll(asList((T[]) array));
        }
        return list;
    }

    private static void readMyData() throws FileNotFoundException {
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
