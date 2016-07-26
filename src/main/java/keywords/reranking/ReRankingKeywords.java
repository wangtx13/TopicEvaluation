package keywords.reranking;

import org.apache.commons.math3.linear.RealMatrix;

import java.util.HashMap;
import java.util.Map;

import static tools.Tools.sortMapStringKey;


/**
 * Created by wangtianxia1 on 16/7/21.
 */
public class ReRankingKeywords {
    private RealMatrix topicWordMatrix;
    private int topicCount;
    private  Map<String, Integer> columnHeaderList;
    private Map<String, Double> keywordsKR0;
    private Map<String, Double>  sortedKeywordsKR0;
    private Map<String, Double> keywordsKR1;
    private Map<String, Double>  sortedKeywordsKR1;
    private Map<String, Double> keywordsKR2;
    private Map<String, Double>  sortedKeywordsKR2;


    public ReRankingKeywords(RealMatrix topicWordMatrix, int topicCount, Map<String, Integer> columnHeaderList) {
        this.topicWordMatrix = topicWordMatrix;
        this.topicCount = topicCount;
        this.columnHeaderList = columnHeaderList;
        keywordsKR0 = new HashMap<>();
        keywordsKR1 = new HashMap<>();
        keywordsKR2 = new HashMap<>();
    }

    public String reRankingKeywordsByKR2(String topicLine, int topicIndex) {
        String sortedTopicLine = "";
        String[] keywordsList = topicLine.split("\t| ");
        for(int i = 2; i < keywordsList.length; i++) {
            double KR2 = calculateKR2(keywordsList[i], topicIndex);
            keywordsKR2.put(keywordsList[i], KR2);
            System.out.println(keywordsList[i] + " " + KR2);
        }

        sortedKeywordsKR2 = sortMapStringKey(keywordsKR2);
        for(Map.Entry<String, Double> entry:sortedKeywordsKR2.entrySet()) {
            sortedTopicLine += entry.getKey() + " ";
        }

        sortedTopicLine = topicIndex + " " + sortedTopicLine;

        return sortedTopicLine;
    }

    public String reRankingKeywordsByKR0(String topicLine, int topicIndex) {
        String sortedTopicLine = "";
        String[] keywordsList = topicLine.split("\t| ");
        for(int i = 2; i < keywordsList.length; i++) {
            int columnIndex = columnHeaderList.get(keywordsList[i]);
            double KR0 = topicWordMatrix.getEntry(topicIndex, columnIndex);
            System.out.println(keywordsList[i] + " " + KR0);
            keywordsKR0.put(keywordsList[i], KR0);
        }

        sortedKeywordsKR0 = sortMapStringKey(keywordsKR0);
        for(Map.Entry<String, Double> entry:sortedKeywordsKR0.entrySet()) {
            sortedTopicLine += entry.getKey() + " ";
        }

        sortedTopicLine = topicIndex + " " + sortedTopicLine;

        return sortedTopicLine;
    }


    public String reRankingKeywordsByKR1(String topicLine, int topicIndex) {
        String sortedTopicLine = "";
        String[] keywordsList = topicLine.split("\t| ");
        for(int i = 2; i < keywordsList.length; i++) {
            double KR1 = calculateKR1(keywordsList[i], topicIndex);
            System.out.println(keywordsList[i] + " " + KR1);
            keywordsKR1.put(keywordsList[i], KR1);
        }

        sortedKeywordsKR1 = sortMapStringKey(keywordsKR1);
        for(Map.Entry<String, Double> entry:sortedKeywordsKR1.entrySet()) {
            sortedTopicLine += entry.getKey() + " ";
        }

        sortedTopicLine = topicIndex + " " + sortedTopicLine;

        return sortedTopicLine;
    }

    private double calculateSumOfOneTermForAllTopics(int columnIndex) {
        double sumOfOneTermForAllTopics = 0;
        for(int i = 0; i < topicCount; i++) {
            sumOfOneTermForAllTopics += topicWordMatrix.getEntry(i, columnIndex);;
        }
        return sumOfOneTermForAllTopics;
    }


    private double calculateKR1(String keywords, int topicIndex) {
        int columnIndex = columnHeaderList.get(keywords);
        double probabilityForATerm = topicWordMatrix.getEntry(topicIndex, columnIndex);

        double KR1 = probabilityForATerm/calculateSumOfOneTermForAllTopics(columnIndex);

        return KR1;
    }

    private double calculateProductOfOneTermForAllTopics(int columnIndex) {
        double productOfOneTermForAllTopics = 1;
        for(int i = 0; i < topicCount; i++) {
            double probabilityForATerm = topicWordMatrix.getEntry(i, columnIndex);
            productOfOneTermForAllTopics *= probabilityForATerm;
        }
//        System.out.println(productOfOneTermForAllTopics);
        return productOfOneTermForAllTopics;
    }

    private double calculateKR2(String keywords, int topicIndex) {
        int columnIndex = columnHeaderList.get(keywords);
        double probabilityForATerm = topicWordMatrix.getEntry(topicIndex, columnIndex);

        double logDenominator = Math.pow(calculateProductOfOneTermForAllTopics(columnIndex), 1/topicCount);
        double KR2 = probabilityForATerm * Math.log(probabilityForATerm/logDenominator);

        return KR2;
    }

}
