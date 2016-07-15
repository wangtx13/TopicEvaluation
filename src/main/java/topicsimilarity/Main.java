package topicsimilarity;

import org.apache.commons.math3.linear.RealMatrix;

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
        System.out.println("similaritiesAtReducedSeq");
        Map<Integer, Double> similaritiesAtReducedSeq = topicSimilarity.getSimilaritiesAtReducedSeq();
        printMap2(similaritiesAtReducedSeq);
        System.out.println("topicReduceSequence");
        Map<Integer, Integer> topicReduceSequence = topicSimilarity.getTopicReduceSequence();
        printMap3(topicReduceSequence);

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

    public static void printMap3(Map<Integer, Integer> map) {
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            System.out.println("[Key] : " + entry.getKey()
                    + " [Value] : " + entry.getValue());
        }
    }
}
