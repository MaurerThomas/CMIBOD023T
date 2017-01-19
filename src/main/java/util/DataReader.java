package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataReader {
    private static File file = new File("data.csv");
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

    public Object[][] getData() {
        return data;
    }

}
