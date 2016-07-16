package topicsimilarity;

import org.apache.commons.math3.linear.RealMatrix;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Main {
    public static void main(String args[]) {
//        MatrixReader matrixReader = new TestMatrixReader();
        Scanner str = new Scanner(System.in);
        System.out.println("Please input the word count file path:");
        String wordCountFilePath = str.next();
        Scanner str2 = new Scanner(System.in);
        System.out.println("Please input the topics file path:");
        String topicsFilePath = str2.next();
        Scanner in = new Scanner(System.in);
        System.out.println("Please input the number of topics:");
        int topicCount = in.nextInt();

        MatrixReader matrixReader = new TopicWordMatrixReader(wordCountFilePath, topicCount);
        TopicSimilarity topicSimilarity = new TopicSimilarity(matrixReader);
        topicSimilarity.generateTopicSimilarity();

        Map<Integer, Integer> topicReducedSequence = new HashMap<>();
        topicReducedSequence = topicSimilarity.getTopicReduceSequence();

        SortTopics sortTopics = new SortTopics(topicReducedSequence);
        String[] sortedTopics = sortTopics.generateSortedTopics(topicsFilePath);

        testSortTopics(sortedTopics);

    }

    public static void testSortTopics(String[] sortedTopics) {
        for(String str : sortedTopics) {
            System.out.println(str);
        }
    }

    public static void testTopicSimilarity(TopicSimilarity topicSimilarity) {
        RealMatrix topicWordMatrix = topicSimilarity.getTopicWordMatrix();

        //print topic word matrix
        for(int i = 0; i < topicWordMatrix.getRowDimension(); ++i) {
            for(int j = 0; j < topicWordMatrix.getColumnDimension(); ++j) {
                System.out.print(topicWordMatrix.getEntry(i, j) + " ");
            }
            System.out.println();
        }

        System.out.println("sorted similarities");
        Map<String, Double> similarities = topicSimilarity.getSimilarities();
        printSortedSimilarities(similarities);
        System.out.println("similaritiesAtReducedSeq");
        Map<Integer, Double> similaritiesAtReducedSeq = topicSimilarity.getSimilaritiesAtReducedSeq();
        printSimilaritiesAtReducedSeq(similaritiesAtReducedSeq);
        System.out.println("topicReduceSequence");
        Map<Integer, Integer> topicReduceSequence = topicSimilarity.getTopicReduceSequence();
        printTopicReduceSequence(topicReduceSequence);
    }

    public static void printSortedSimilarities(Map<String, Double> map) {
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            System.out.println("[row, col] : " + entry.getKey()
                    + " [Topic Similarity] : " + entry.getValue());
        }
    }

    public static void printSimilaritiesAtReducedSeq(Map<Integer, Double> map) {
        for (Map.Entry<Integer, Double> entry : map.entrySet()) {
            System.out.println("[Topic] : " + entry.getKey()
                    + " [Topic Similarity] : " + entry.getValue());
        }
    }

    public static void printTopicReduceSequence(Map<Integer, Integer> map) {
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            System.out.println("[Topic] : " + entry.getKey()
                    + " [Index] : " + entry.getValue());
        }
    }
}
