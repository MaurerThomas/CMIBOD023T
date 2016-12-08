package bayesian;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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


        // Convert the 2D array from above to a list so we can use streams.
        List<String> terms = twoDArrayToList(data);
        // Frequency "table"


        // Likelihood "table"


        // Posterior Probability


    }

    private static Map<String, Integer> calculateFrequency(List<String> terms) {
        return terms.parallelStream().
                flatMap(s -> Arrays.asList(s.split(" ")).stream()).
                collect(Collectors.toConcurrentMap(
                        String::toLowerCase, w -> 1, Integer::sum));
    }

    private static Object[] extractFeatureFromDataset(Object[][] array, int index) {
        Object[] column = new Object[array[0].length];
        for (int i = 0; i < column.length; i++) {
            column[i] = array[i][index];
        }
        return column;
    }

    private static <T> List<T> twoDArrayToList(Object[][] twoDArray) {
        List<T> list = new ArrayList<>();
        for (Object[] array : twoDArray) {
            list.addAll(Arrays.asList((T[]) array));
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
