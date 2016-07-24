package main;

import coverageandvariance.CoverageAndVariance;
import coverageandvariance.TopicReRankByCovAndVar;
import matrixreader.DocumentTopicMatrixReader;
import matrixreader.MatrixReader;
import matrixreader.TopicWordMatrixReader;
import org.apache.commons.math3.linear.RealMatrix;
import tools.SortTopics;
import topics.similarity.TopicSimilarity;

import java.io.*;
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
        String wordCountFilePath = rootPath + "/word-top.txt";
        String topicsFilePath = rootPath + "/keys.txt";
        String compositionFilePath = rootPath + "/composition.txt";
        String documentsWordsCountPath = rootPath + "/documentsWordsCount.txt";
        Scanner in = new Scanner(System.in);
        System.out.println("Please input the number of topics:");
        int topicCount = in.nextInt();
        Scanner strOut = new Scanner(System.in);
        System.out.println("Please input the output file name:");
        String outputFileName = "./" + strOut.next() + ".txt";

        File outputFile = new File(outputFileName);
        String output = "";

        MatrixReader topicWordMatrixReader = new TopicWordMatrixReader(wordCountFilePath, topicCount);

        TopicSimilarity topicSimilarity = new TopicSimilarity(topicWordMatrixReader);
        topicSimilarity.generateTopicSimilarity();
        Map<Integer, Integer> topicReducedSequence = topicSimilarity.getTopicReduceSequence();
        SortTopics sortTopicsBySimilarity = new SortTopics(topicWordMatrixReader, topicReducedSequence);
        String[] sortedTopicsBySimilarity = sortTopicsBySimilarity.generateSortedTopics(topicsFilePath, true);//in ascending order

        MatrixReader docTopicMatrixReader = new DocumentTopicMatrixReader(compositionFilePath, topicCount);

        TopicReRankByCovAndVar topicReRankByCovAndVar = new TopicReRankByCovAndVar(docTopicMatrixReader, documentsWordsCountPath, topicCount);
        Map<Integer, Integer> topicRankSequence = topicReRankByCovAndVar.getTopicRankSequence();
//        printReRankValue(topicReRankByCovAndVar.getTopicRankValues());//test
//        printTopicSequence(topicRankSequence);//test
        SortTopics sortTopicsByCovAndVar = new SortTopics(topicWordMatrixReader, topicRankSequence);
        String[] sortedTopicsByCovAndVar = sortTopicsByCovAndVar.generateSortedTopics(topicsFilePath, false);//in descending order

//        output += "Topic similarity sequence\n";
//        output += generateTopicSimilarity(topicSimilarity);
        output += "Re-ranking in terms of topic similarity by KR1\n";
        output += generateSortTopics(sortedTopicsBySimilarity);
        output += "\n";
        output += "Re-ranking in terms of weighted topic coverage and variation by KR1\n";
        output += generateSortTopics(sortedTopicsByCovAndVar);

        writeToFile(outputFile, output);
    }



    public static String generateTopicSimilarity(TopicSimilarity topicSimilarity) {
        String output = "";
        RealMatrix topicWordMatrix = topicSimilarity.getTopicWordMatrix();

        output = "Sorted topics similarity sequence:\n";
        Map<String, Double> similarities = topicSimilarity.getSimilarities();
        for (Map.Entry<String, Double> entry : similarities.entrySet()) {
            output = output + "[row, col] : " + entry.getKey()
                    + " [Topic Similarity] : " + entry.getValue() + "\n";
        }

        output = output + "Topics' similarities:\n";
        Map<Integer, Double> similaritiesAtReducedSeq = topicSimilarity.getSimilaritiesAtReducedSeq();
        for (Map.Entry<Integer, Double> entry : similaritiesAtReducedSeq.entrySet()) {
            output = output + "[Topic] : " + entry.getKey()
                    + " [Topic Similarity] : " + entry.getValue() + "\n";
        }

        output = output + "Topic Reduced Sequence\n";
        Map<Integer, Integer> topicReduceSequence = topicSimilarity.getTopicReduceSequence();
        for (Map.Entry<Integer, Integer> entry : topicReduceSequence.entrySet()) {
           output = output + "[Topic] : " + entry.getKey()
                    + " [Sequence] : " + entry.getValue() + "\n";
        }
        return output;
    }

    public static String generateSortTopics(String[] sortedTopics) {
        String output = "";
        for(String str : sortedTopics) {
            output = output + str + "\n";
        }
        return output;
    }

    public static String printTopicWordMatrix(MatrixReader topicWordMatrixReader) {
        String output = "";
        RealMatrix topicWordMatrix = topicWordMatrixReader.read();
        for(int i = 0; i < topicWordMatrix.getRowDimension(); i++) {
            for(int j = 0; j < topicWordMatrix.getColumnDimension(); j++) {
                output = output + topicWordMatrix.getEntry(i, j) + "\t";
            }
            output += "\n";
        }
        return output;
    }

    public static String printDocumentTopicMatrix(MatrixReader documentTopicMatrixReader) {
        String output = "";
        RealMatrix documentTopicMatrix = documentTopicMatrixReader.read();
        for(int i = 0; i < documentTopicMatrix.getRowDimension(); i++) {
            for(int j = 0; j < documentTopicMatrix.getColumnDimension(); j++) {
                output = output + documentTopicMatrix.getEntry(i, j) + "\t";
            }
            output += "\n";
        }
        return output;
    }

    public static void printSortedSimilarities(Map<String, Double> map) {
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            System.out.println("[row, col] : " + entry.getKey()
                    + " [Topic Similarity] : " + entry.getValue());
        }
    }

    public static void printReRankValue(Map<Integer, Double> map) {
        for (Map.Entry<Integer, Double> entry : map.entrySet()) {
            System.out.println("[Topic] : " + entry.getKey()
                    + " [Value] : " + entry.getValue());
        }
    }

    public static void printTopicSequence(Map<Integer, Integer> map) {
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            System.out.println("[Topic] : " + entry.getKey()
                    + " [Sequence] : " + entry.getValue());
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
