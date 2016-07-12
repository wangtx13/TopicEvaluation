package topicsimilarity;

import topicsimilarity.testing.TestMatrixReader;

import java.util.Map;


public class Main {
    public static void main(String args[]) {
        MatrixReader testMatrixReader = new TestMatrixReader();
        TopicSimilarity testTS = new TopicSimilarity(testMatrixReader);
        testTS.test();
    }


    public static void printMap(Map<String, Double> map) {
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            System.out.println("[Key] : " + entry.getKey()
                    + " [Value] : " + entry.getValue());
        }
    }
}
