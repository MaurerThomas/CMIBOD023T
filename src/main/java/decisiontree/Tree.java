package decisiontree;

import util.DataReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tree {
    private List<Attribute> attributes = new ArrayList<>();
    private TreeNode root;
    private DataReader dataReader = new DataReader(15, 5);
    Object[][] data = dataReader.getData();



    public void train() {
        //TreeNode root = new TreeNode();

    }

}
