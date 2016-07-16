package topicsimilarity;

import java.io.*;
import java.util.Map;

/**
 * Created by wangtianxia1 on 16/7/15.
 */
public class SortTopics {
    private Map<Integer, Integer> topicReduceSequence;

    public SortTopics(Map<Integer, Integer> topicReduceSequence) {
        this.topicReduceSequence = topicReduceSequence;
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
                    int topicNumber = Integer.parseInt(labels[0]);
                    int newIndex = topicReduceSequence.get(topicNumber);
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
