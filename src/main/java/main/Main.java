package main;

import matrixreader.DocTopicMatrixReader;
import matrixreader.MatrixReader;
import matrixreader.TopicWordMatrixReader;
import org.apache.commons.math3.linear.RealMatrix;
import topicsimilarity.SortTopics;
import topicsimilarity.TopicSimilarity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main {
    public static void main(String args[]) {
//        MatrixReader matrixReader = new TestMatrixReader();
        Scanner str = new Scanner(System.in);
        System.out.println("Please input the root path of files:");
        String rootPath = str.next();
        String wordCountFilePath = rootPath + "/word_top.txt";
        String topicsFilePath = rootPath + "/keys.txt";
        String compositionFilePath = rootPath + "/composition.txt";
        Scanner in = new Scanner(System.in);
        System.out.println("Please input the number of topics:");
        int topicCount = in.nextInt();

        File outputFile = new File("./reRankingTopics.txt");

        MatrixReader matrixReader = new TopicWordMatrixReader(wordCountFilePath, topicCount);
        TopicSimilarity topicSimilarity = new TopicSimilarity(matrixReader);
        topicSimilarity.generateTopicSimilarity();
        Map<Integer, Integer> topicReducedSequence = topicSimilarity.getTopicReduceSequence();

        SortTopics sortTopics = new SortTopics(topicReducedSequence);
        String[] sortedTopics = sortTopics.generateSortedTopics(topicsFilePath);

        DocTopicMatrixReader docTopicMatrixReader = new DocTopicMatrixReader(compositionFilePath, topicCount);
        RealMatrix doctopicMatrix = docTopicMatrixReader.read();
//        for(int i = 0; i < doctopicMatrix.getRowDimension(); i++) {
//            for(int j = 0; j < doctopicMatrix.getColumnDimension(); j++) {
//                System.out.print(doctopicMatrix.getEntry(i, j) + " ");
//            }
//            System.out.println();
//        }

        String output = "";
        output += "Topic similarity sequence\n";
        output += generateTopicSimilarity(topicSimilarity);
        output += "Re-ranking in terms of topic similarity\n";
        output += generateSortTopics(sortedTopics);
//        output += "Re-ranking in terms of topic coverage and variation:\n";
//        output += generateCoverageAndVariation();

        writeToFile(outputFile, output);
    }

    public static String generateTopicSimilarity(TopicSimilarity topicSimilarity) {
        String output = "";

        RealMatrix topicWordMatrix = topicSimilarity.getTopicWordMatrix();

        //print topic word matrix
//        for(int i = 0; i < topicWordMatrix.getRowDimension(); ++i) {
//            for(int j = 0; j < topicWordMatrix.getColumnDimension(); ++j) {
//                System.out.print(topicWordMatrix.getEntry(i, j) + " ");
//            }
//            System.out.println();
//        }

        output = "Sorted similarity sequence:\n";
        Map<String, Double> similarities = topicSimilarity.getSimilarities();
        for (Map.Entry<String, Double> entry : similarities.entrySet()) {
            output = output + "[row, col] : " + entry.getKey()
                    + " [Topic Similarity] : " + entry.getValue() + "\n";
        }
//        printSortedSimilarities(similarities);

        output = output + "Topics' similarities:\n";
        Map<Integer, Double> similaritiesAtReducedSeq = topicSimilarity.getSimilaritiesAtReducedSeq();
        for (Map.Entry<Integer, Double> entry : similaritiesAtReducedSeq.entrySet()) {
            output = output + "[Topic] : " + entry.getKey()
                    + " [Topic Similarity] : " + entry.getValue() + "\n";
        }
//        printSimilaritiesAtReducedSeq(similaritiesAtReducedSeq);

        output = output + "Topic Reduced Sequence\n";
        Map<Integer, Integer> topicReduceSequence = topicSimilarity.getTopicReduceSequence();
        for (Map.Entry<Integer, Integer> entry : topicReduceSequence.entrySet()) {
           output = output + "[Topic] : " + entry.getKey()
                    + " [Index] : " + entry.getValue() + "\n";
        }
//        printTopicReduceSequence(topicReduceSequence);
        return output;
    }

    public static String generateSortTopics(String[] sortedTopics) {
        String output = "";
        for(String str : sortedTopics) {
            output = output + str + "\n";
        }
        return output;
    }

    public static String generateCoverageAndVariation() {
        String output = "";
        return output;
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

    public static void writeToFile(File outputFile, String words) {
        try {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile.getPath()))) {
                writer.write(words);
            }

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
