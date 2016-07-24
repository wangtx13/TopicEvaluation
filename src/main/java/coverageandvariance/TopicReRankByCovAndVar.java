package coverageandvariance;

import matrixreader.DocumentTopicMatrixReader;
import matrixreader.MatrixReader;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static tools.Tools.sortMapIntegerKey;

import java.util.Map.Entry;

/**
 * Created by wangtianxia1 on 16/7/24.
 */
public class TopicReRankByCovAndVar {

    private Map<String, Integer> documentsWordsCountList;
    private Map<Integer, Double> topicRankValues;
    private Map<Integer, Integer> topicRankSequence;//<Topic, Sequence>

    public TopicReRankByCovAndVar(MatrixReader docTopicMatrixReader, String documentsWordsCountPath, int topicCount) {
        //generate words count list for all documents
        documentsWordsCountList = new HashMap<>();
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
        topicRankValues = new HashMap<>();
        for(int i  = 0;i < topicCount; i++) {
            CoverageAndVariance coverageAndVariance = new CoverageAndVariance(docTopicMatrixReader, documentsWordsCountList, i);
            double topicRankValue = coverageAndVariance.calculateTopicRankValue(1, 1);
            topicRankValues.put(i, topicRankValue);
        }
        topicRankValues = sortMapIntegerKey(topicRankValues);
        topicRankSequence = generateTopicSequence(topicRankValues);
    }

    private Map<Integer, Integer> generateTopicSequence(Map<Integer, Double> topicRankValues) {
        Map<Integer, Integer> topicRankSequence = new HashMap<>();//<Topic, Sequence>
        int sequenceNumber = 0;
        for(Entry<Integer, Double> entry:topicRankValues.entrySet()) {
            int topicIndex = entry.getKey();
            topicRankSequence.put(topicIndex, sequenceNumber);
            sequenceNumber++;
        }

        return topicRankSequence;
    }

    public Map<String, Integer> getDocumentsWordsCountList() {
        return documentsWordsCountList;
    }

    public Map<Integer, Double> getTopicRankValues() {
        return topicRankValues;
    }

    public Map<Integer, Integer> getTopicRankSequence() {
        return topicRankSequence;
    }
}
