package topics.similarity;

import keywords.reranking.ReRankingKeywords;
import matrixreader.MatrixReader;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.*;
import java.util.Map;

/**
 * Created by wangtianxia on 16/7/15.
 */
//The last removed topics should be ranked highest
public class SortTopics {
    private RealMatrix topicWordMatrix;
    private Map<Integer, Integer> topicReduceSequence;
    private Map<String, Integer> columnHeaderList;

    public SortTopics(MatrixReader topicWordMatrixReader, Map<Integer, Integer> topicReduceSequence) {
        topicWordMatrix = topicWordMatrixReader.read();
        this.topicReduceSequence = topicReduceSequence;
        columnHeaderList = topicWordMatrixReader.getColumnHeaderList();
    }

    public String[] generateSortedTopics(String topicsFilePath) {
        int topicCount = topicReduceSequence.keySet().size();
        String[] sortedTopics = new String[topicCount];

        String line = null;
        try {
            try (
                    InputStream in = new FileInputStream(topicsFilePath);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                while ((line = reader.readLine()) != null) {
                    String[] labels = line.split("\t| ");
                    int topicIndex = Integer.parseInt(labels[0]);//label[0] is topic index
                    //re-ranking the keywords in terms of KR2; the number of rows of topic word Matrix is the topic count
                    ReRankingKeywords reRankingKeywords = new ReRankingKeywords(topicWordMatrix, topicWordMatrix.getRowDimension(), columnHeaderList);
//                    line = reRankingKeywords.reRankingKeywordsByKR2(line, topicIndex);
                    line = reRankingKeywords.reRankingKeywordsByKR1(line, topicIndex);
                    int newIndex = topicCount - topicReduceSequence.get(topicIndex) - 1;
                    sortedTopics[newIndex] = line;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  sortedTopics;
    }

    public static void printTopicReduceSequence(Map<Integer, Integer> map) {
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            System.out.println("[Topic] : " + entry.getKey()
                    + " [Index] : " + entry.getValue());
        }
    }
}
