
/**
 * Created by tianxia on 16/7/11.
 */

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.util.FastMath;

import java.util.*;

public class TopicSimilarity {

    private Map<Integer, Double> topicSequence;
    private Map<String, Double> similarities;
    private RealMatrix topicWordMatrix;
    private int topicsCount;
    private int wordsCount;

    public TopicSimilarity(String wordCountFilePath) {
        this.topicSequence = new HashMap<Integer, Double>();
        this.similarities = new HashMap<String, Double>();
        int rows = 3;
        int columns = 3;
        RealMatrix topicWordMatrix = MatrixUtils.createRealMatrix(rows, columns);
        topicWordMatrix.addToEntry(0, 0, 0.1);
        topicWordMatrix.addToEntry(0, 1, 0.2);
        topicWordMatrix.addToEntry(0, 2, 0.7);
        topicWordMatrix.addToEntry(1, 0, 0.2);
        topicWordMatrix.addToEntry(1, 1, 0.3);
        topicWordMatrix.addToEntry(1, 2, 0.5);
        topicWordMatrix.addToEntry(2, 0, 0.2);
        topicWordMatrix.addToEntry(2, 1, 0.3);
        topicWordMatrix.addToEntry(2, 2, 0.5);
        this.topicWordMatrix = topicWordMatrix;
        topicsCount = rows;
        wordsCount = columns;

    }

    public void test() {
        similarities = calculateSimilarities(topicWordMatrix, topicsCount);
        similarities = sortSimilarities(similarities);
        topicSequence = generateTopicSequence(similarities);
        printMap(topicSequence);
    }

    //calculate similarity
    private Map<String, Double> calculateSimilarities(RealMatrix topicWordMatrix, int topicsCount) {
        Map<String, Double> similarities = new HashMap<String, Double>();
        double[] variances = new double[topicsCount];
        for (int i = 0; i < topicsCount; i++) {
//            double mean = StatUtils.mean(topicWordMatrix.getRow(i));
            double var = StatUtils.variance(topicWordMatrix.getRow(i));
            variances[i] = var;
        }

        for (int i = 0; i < topicsCount; i++) {
            for (int j = i+1; j < topicsCount; j++) {
                double cor = new PearsonsCorrelation().correlation(topicWordMatrix.getRow(i), topicWordMatrix.getRow(j));
                //similarity=0.5*(DX+DY-根号((DX+DY)^2-4*DX*DY*(1-correlation(X, Y)^2))
                double simil = 0.5 * (variances[i] + variances[j]- FastMath.sqrt(FastMath.pow(variances[i] + variances[j], 2) - 4 * variances[i] * variances[j] * FastMath.pow(1-cor, 2)));
                String key = i + "," + j;
                similarities.put(key, simil);
            }
        }
        return similarities;
    }

    private Map<String, Double> sortSimilarities(Map<String, Double> unsortMap) {

        // Convert Map to List
        List<Map.Entry<String, Double>> list =
                new LinkedList<Map.Entry<String, Double>>(unsortMap.entrySet());

        // Sort list with comparator, to compare the Map values
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Map.Entry<String, Double> o1,
                               Map.Entry<String, Double> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // Convert sorted map back to a Map
        Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
        for (Iterator<Map.Entry<String, Double>> it = list.iterator(); it.hasNext();) {
            Map.Entry<String, Double> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    public Map<Integer, Double> generateTopicSequence(Map<String, Double> map) {
        Map<Integer, Double> topicSequence = new HashMap<Integer, Double>();
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            String key = entry.getKey();
            String[] index = key.split(",");
            int row = Integer.parseInt(index[0]);
            int col = Integer.parseInt(index[1]);
            if(!topicSequence.containsKey(col)) {
                topicSequence.put(col, entry.getValue());
            }
        }

        return topicSequence;

    }

    public Map<Integer, Double> getTopicSequence() {
        return topicSequence;
    }

    public void setTopicSequence(Map<Integer, Double> topicSequence) {
        this.topicSequence = topicSequence;
    }

    public RealMatrix getTopicWordMatrix() {
        return topicWordMatrix;
    }

    public void setTopicWordMatrix(RealMatrix topicWordMatrix) {
        this.topicWordMatrix = topicWordMatrix;
    }

    public Map<String, Double> getSimilarities() {
        return similarities;
    }

    public void setSimilarities(Map<String, Double> similarities) {
        this.similarities = similarities;
    }


    public static void printMap(Map<Integer, Double> map) {
        for (Map.Entry<Integer, Double> entry : map.entrySet()) {
            System.out.println("[Key] : " + entry.getKey()
                    + " [Value] : " + entry.getValue());
        }
    }
}
