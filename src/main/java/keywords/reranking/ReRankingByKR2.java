package keywords.reranking;

import org.apache.commons.math3.linear.RealMatrix;

import java.util.HashMap;
import java.util.Map;

import static tools.Tools.sortMap;


/**
 * Created by wangtianxia1 on 16/7/21.
 */
public class ReRankingByKR2 {
    private RealMatrix topicWordMatrix;
    private int topicCount;
    private  Map<String, Integer> columnHeaderList;
    private Map<String, Double> keywordsKR2;
    private Map<String, Double>  sortedKeywordsKR2;

    public ReRankingByKR2(RealMatrix topicWordMatrix, int topicCount,  Map<String, Integer> columnHeaderList) {
        this.topicWordMatrix = topicWordMatrix;
        this.topicCount = topicCount;
        this.columnHeaderList = columnHeaderList;
        keywordsKR2 = new HashMap<>();
    }

    public String reRankingKeywords(String topicLine, int topicIndex) {
        String sortedTopicLine = "";
        String[] keywordsList = topicLine.split("\t| ");
        for(int i = 2; i < keywordsList.length; i++) {
            double KR2 = calculateKR2(keywordsList[i], topicIndex);
            keywordsKR2.put(keywordsList[i], KR2);
        }

        sortedKeywordsKR2 = sortMap(keywordsKR2);
        for(Map.Entry<String, Double> entry:sortedKeywordsKR2.entrySet()) {
            sortedTopicLine += entry.getKey() + " ";
        }

        sortedTopicLine = topicIndex + " " + sortedTopicLine;

        return sortedTopicLine;
    }

    private double calculateKR2(String keywords, int topicIndex) {
        int columnIndex = columnHeaderList.get(keywords);
        double probabilityForATerm = topicWordMatrix.getEntry(topicIndex, columnIndex);

        double productOfOneTermForAllTopics = 1;
        for(int i = 0; i < topicCount; i++) {
            productOfOneTermForAllTopics *= topicWordMatrix.getEntry(i, columnIndex);
        }

        double logDenominator = Math.pow(productOfOneTermForAllTopics, 1/topicCount);
        double KR2 = probabilityForATerm * Math.log(probabilityForATerm/logDenominator);

        return KR2;
    }

}
