package matrixreader;

import matrixreader.MatrixReader;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by wangtianxia1 on 16/7/14.
 */
public class TopicWordMatrixReader implements MatrixReader {

    private RealMatrix topicWordMatrix;

    public TopicWordMatrixReader(String wordCountFilePath, int topicCount) {
        topicWordMatrix = getTopicWordMatrix(wordCountFilePath, topicCount);
    }

    private RealMatrix getTopicWordMatrix(String wordCountFilePath, int topicCount) {
        RealMatrix topicWordMatrix = null;
        File wordCountFile = new File(wordCountFilePath);
        ArrayList<String> wordList = new ArrayList<>();
        int totalTermCount = 0;//The number of terms
        String line = null;
        int[] totalWordCount = new int[topicCount];//The number of words, onw term may appears several times
        //initialization
        for(int eachCount :totalWordCount) {
            eachCount = 0;
        }

        try {
            try (
                    InputStream in = new FileInputStream(wordCountFile.getPath());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                while ((line = reader.readLine()) != null) {
                    totalTermCount++;
                    wordList.add(line);
                }

                double[][] topicWordMatrixData = new double[topicCount][totalTermCount];
                int colNumber = 0;
//                int[] totalWordCount = new int[topicCount];//The number of words, onw term may appears several times

                Iterator it = wordList.iterator();
                while(it.hasNext()){
                    String wordListStr = it.next().toString();
                    String[] topics = wordListStr.split("\t| ");
                    for(int i = 0; i < topics.length; ++i) {
                        if(topics[i].contains(":")) {
                            String[] label = topics[i].split(":");
                            //label[0] is the topic index, label[1] is the quantity of a word that belongs to a topic
                            int rowNumber = Integer.parseInt(label[0]);
                            int count = Integer.parseInt(label[1]);
                            //firstly, calculating the word count for each topic
                            topicWordMatrixData[rowNumber][colNumber] = count;
                            totalWordCount[rowNumber] += count;
                        }
                    }
                    //next word
                    colNumber++;
                }

                //secondly, calculating the word count percentage
                for (int i = 0; i < topicCount; ++i) {
                    for(int j = 0; j < colNumber ;j++) {
                        if (totalTermCount != 0) {
                            topicWordMatrixData[i][j] = topicWordMatrixData[i][j]/totalWordCount[i];
                        }
                    }
                }

                topicWordMatrix = MatrixUtils.createRealMatrix(topicWordMatrixData);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Please check the word count file path");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return topicWordMatrix;

    }

    @Override
    public RealMatrix read() {
        return topicWordMatrix;
    }
}