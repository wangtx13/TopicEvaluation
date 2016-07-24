package coverageandvariance;

import matrixreader.MatrixReader;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import static java.lang.StrictMath.pow;
import static java.lang.StrictMath.sqrt;

/**
 * Created by wangtianxia1 on 16/7/18.
 */
public class CoverageAndVariance {
    private Map<Integer, String> fileList;
    private double coverage;
    private double variance;

    public CoverageAndVariance(MatrixReader documentTopicMatrixReader, Map<String, Integer> documentWordsCountList, int topicIndex) {
        fileList = new HashMap<>();
        coverage = 0;
        variance = 0;
        calculateCoverageAndVariance(documentTopicMatrixReader, documentWordsCountList, topicIndex);
    }

    private void calculateCoverageAndVariance(MatrixReader documentTopicMatrixReader, Map<String, Integer> documentWordsCountList, int topicIndex) {
        fileList = documentTopicMatrixReader.getRowHeaderList();
        RealMatrix documentTopicMatrix = documentTopicMatrixReader.read();
        int documentIndex;
        double allDocLengthPlusDocTopicProportion = 0;
        double allDocumentsLength = 0;
        double numeratorOfVARk = 0;
        //calculate coverage
        for(Entry<Integer, String> entry:fileList.entrySet()) {
            documentIndex = entry.getKey();
            String[] filePathSet = entry.getValue().split("/");
            String fileName = filePathSet[filePathSet.length-1];
            int documentLength = documentWordsCountList.get(fileName);
            allDocLengthPlusDocTopicProportion += documentLength * documentTopicMatrix.getEntry(documentIndex, topicIndex);
            allDocumentsLength += documentLength;
        }
        if(allDocumentsLength!=0)
            coverage = allDocLengthPlusDocTopicProportion/allDocumentsLength;
        else
            System.out.println("All documents are empty, please check them");
        //calculate variance
        for(Entry<Integer, String> entry:fileList.entrySet()) {
            documentIndex = entry.getKey();
            String[] filePathSet = entry.getValue().split("/");
            String fileName = filePathSet[filePathSet.length-1];
            int documentLength = documentWordsCountList.get(fileName);
            numeratorOfVARk += documentLength *pow(documentTopicMatrix.getEntry(documentIndex, topicIndex)-coverage, 2);
        }
        if(allDocumentsLength!=0)
            variance = sqrt(numeratorOfVARk/allDocumentsLength);
        else
            System.out.println("All documents are empty, please check them");
    }

    public double calculateTopicRankValue(double para1, double para2) {
        double topicRankValue = pow(coverage, para1)*pow(variance, para2);
        return topicRankValue;
    }
}
