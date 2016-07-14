package topicsimilarity;

import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.math3.linear.RealMatrix;
import topicsimilarity.testing.TestMatrixReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Scanner;


public class Main {
    public static void main(String args[]) {
////        MatrixReader matrixReader = new TestMatrixReader();
        Scanner str = new Scanner(System.in);
        System.out.println("Please input the word count file path:");
        String wordCountFilePath = str.next();
        Scanner in = new Scanner(System.in);
        System.out.println("Please input the number of topics:");
        int topicCount = in.nextInt();

        System.out.println(wordCountFilePath + " " + topicCount);

        MatrixReader matrixReader = new TopicWordMatrixReader(wordCountFilePath, topicCount);
        TopicSimilarity topicSimilarity = new TopicSimilarity(matrixReader);
        topicSimilarity.generateTopicSimilarity();
        RealMatrix topicWordMatrix = topicSimilarity.getTopicWordMatrix();
        //print topic word matrix
//        for(int i = 0; i < topicWordMatrix.getRowDimension(); ++i) {
//            for(int j = 0; j < topicWordMatrix.getColumnDimension(); ++j) {
//                System.out.print(topicWordMatrix.getEntry(i, j) + " ");
//            }
//         System.out.println()
//        }

        Map<String, Double> similarities = topicSimilarity.getSimilarities();
        printMap(similarities);
        System.out.println("topicSequence");
        Map<Integer, Double> topicSequence = topicSimilarity.getTopicSequence();
        printMap2(topicSequence);

    }


    public static void printMap(Map<String, Double> map) {
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            System.out.println("[Key] : " + entry.getKey()
                    + " [Value] : " + entry.getValue());
        }
    }

    public static void printMap2(Map<Integer, Double> map) {
        for (Map.Entry<Integer, Double> entry : map.entrySet()) {
            System.out.println("[Key] : " + entry.getKey()
                    + " [Value] : " + entry.getValue());
        }
    }
}
