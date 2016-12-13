package bayesian;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class Main {
    private static File file = new File("data.csv");
    private static Object[][] data = new Object[15][5];

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

        List<Object> yes = extractFeatureFromDataset(data, 0, "Yes");
        List<Object> no = extractFeatureFromDataset(data, 0, "No");

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

    private static List extractFeatureFromDataset(Object[][] array, int index, String something) {
        List<Object> column = new ArrayList<>();
        for (int i = 1; i < array.length; i++) {
            if (array[i][4].equals(something)) {
                column.add(array[i][index]);
            }

        }
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
        }
        sc.close();
    }
}
