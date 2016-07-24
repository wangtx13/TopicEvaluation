package topics.similarity;
/**
 * Created by tianxia on 16/7/11.
 */

import matrixreader.MatrixReader;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.util.FastMath;

import java.util.*;

import static tools.Tools.sortMapStringKey;

public class TopicSimilarity {

    private Map<String, Double> similarities;
    private RealMatrix topicWordMatrix;
    private Map<Integer, Double> similaritiesAtReducedSeq;
    private Map<Integer, Integer> topicReduceSequence;//<Topic, Sequence>

    public TopicSimilarity(MatrixReader matrixReader) {
        similarities = new HashMap<String, Double>();
        topicWordMatrix = matrixReader.read();
        similaritiesAtReducedSeq = new HashMap<Integer, Double>();
        topicReduceSequence = new HashMap<Integer, Integer>();
    }

    public void generateTopicSimilarity() {
        int topicsCount = topicWordMatrix.getRowDimension();
        similarities = calculateSimilarities(topicWordMatrix, topicsCount);
        similarities = sortMapStringKey(similarities);
        generateTopicSequence(similarities);
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

    private void generateTopicSequence(Map<String, Double> map) {
        Map<Integer, Double> simAtReducedSeq = new HashMap<Integer, Double>();
        Map<Integer, Integer> topicReduceSeq = new HashMap<Integer, Integer>();
        int seq = 0;
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            String key = entry.getKey();
            String[] index = key.split(",");
            int row = Integer.parseInt(index[0]);
            int col = Integer.parseInt(index[1]);

            if(!simAtReducedSeq.containsKey(row) && !simAtReducedSeq.containsKey(col)) {
                simAtReducedSeq.put(col, entry.getValue());
                topicReduceSeq.put(col, seq);
                seq++;
            }
        }

        similaritiesAtReducedSeq = simAtReducedSeq;
        topicReduceSequence = topicReduceSeq;

    }

    public Map<Integer, Double> getSimilaritiesAtReducedSeq() {
        return similaritiesAtReducedSeq;
    }

    public void setSimilaritiesAtReducedSeq(Map<Integer, Double> similaritiesAtReducedSeq) {
        this.similaritiesAtReducedSeq = similaritiesAtReducedSeq;
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

    public Map<Integer, Integer> getTopicReduceSequence() {
        return topicReduceSequence;
    }

    public void setTopicReduceSequence(Map<Integer, Integer> topicReduceSequence) {
        this.topicReduceSequence = topicReduceSequence;
    }

    public static void printMap(Map<String, Double> map) {
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            System.out.println("[Key] : " + entry.getKey()
                    + " [Value] : " + entry.getValue());
        }
    }
}
