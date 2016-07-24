package main;

import coverageandvariance.CoverageAndVariance;
import matrixreader.DocumentTopicMatrixReader;
import matrixreader.MatrixReader;
import matrixreader.TopicWordMatrixReader;
import org.apache.commons.math3.linear.RealMatrix;
import topics.similarity.SortTopics;
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
        String wordCountFilePath = rootPath + "/word_top.txt";
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

        SortTopics sortTopics = new SortTopics(topicWordMatrixReader, topicReducedSequence);
        String[] sortedTopics = sortTopics.generateSortedTopics(topicsFilePath);

        MatrixReader docTopicMatrixReader = new DocumentTopicMatrixReader(compositionFilePath, topicCount);
        //generate words count list for all documents
        Map<String, Integer> documentsWordsCountList = new HashMap<>();
        try{
            try(
                    InputStream inputStream = new FileInputStream(new File(documentsWordsCountPath));
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

                String line = "";
                while((line=reader.readLine())!=null) {
                    String[] labels = line.split("\t");//labels[0] is the documents' name, label[1] is word count in the document
                    documentsWordsCountList.put(labels[0], Integer.parseInt(labels[1]));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //calculate weighted topic coverage and variation, and its relevant topic rank value
        for(int i  = 0;i < topicCount; i++) {
            CoverageAndVariance coverageAndVariance = new CoverageAndVariance(docTopicMatrixReader, documentsWordsCountList, i);
            double topicRankValue = coverageAndVariance.calculateTopicRankValue(1, 0);
        }


//        output += printTopicWordMatrix(topicWordMatrixReader);
//        output += "Topic similarity sequence\n";
//        output += generateTopicSimilarity(topicSimilarity);
        output += "Re-ranking in terms of topic similarity and KR1\n";
        output += generateSortTopics(sortedTopics);

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
