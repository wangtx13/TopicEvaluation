package topicsimilarity;
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

    private Map<Integer, Double> topicSequence;//
    private Map<String, Double> similarities;
    private RealMatrix topicWordMatrix;
    private int topicsCount;

    public TopicSimilarity(MatrixReader matrixReader) {
        topicSequence = new HashMap<Integer, Double>();
        similarities = new HashMap<String, Double>();
        topicWordMatrix = matrixReader.read();
        topicsCount = topicWordMatrix.getRowDimension();
    }

    public void generateTopicSimilarity() {
        similarities = calculateSimilarities(topicWordMatrix, topicsCount);
        similarities = sortSimilarities(similarities);
        topicSequence = generateTopicSequence(similarities);
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
            for (int j = 0; j < topicsCount; j++) {
                double cor = new PearsonsCorrelation().correlation(topicWordMatrix.getRow(i), topicWordMatrix.getRow(j));
                //similarity=0.5*(DX+DY-根号((DX+DY)^2-4*DX*DY*(1-correlation(X, Y)^2))
                double simil = 0.5 * (variances[i] + variances[j]- FastMath.sqrt(FastMath.pow(variances[i] + variances[j], 2) - 4 * variances[i] * variances[j] * (1 - FastMath.pow(cor, 2))));
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
                return (o2.getValue()).compareTo(o1.getValue());
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

    private Map<Integer, Double> generateTopicSequence(Map<String, Double> map) {
        Map<Integer, Double> topicSequence = new HashMap<Integer, Double>();
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            String key = entry.getKey();
            String[] index = key.split(",");
            int row = Integer.parseInt(index[0]);
            int col = Integer.parseInt(index[1]);
            if(!topicSequence.containsKey(row) && !topicSequence.containsKey(col)) {
                double test = entry.getValue();
                System.out.println(col + " " + test);
                topicSequence.put(col, test);
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


    public static void printMap(Map<String, Double> map) {
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            System.out.println("[Key] : " + entry.getKey()
                    + " [Value] : " + entry.getValue());
        }
    }
}
