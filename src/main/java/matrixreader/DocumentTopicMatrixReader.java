package matrixreader;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by wangtianxia1 on 16/7/18.
 */
public class DocumentTopicMatrixReader implements MatrixReader{
    private RealMatrix docTopicMatrixReader;
    private Map<Integer, String> fileList = new HashMap<>();//<row index in document-topic matrix, file name>
    private Map<Integer, String[]> topDocumentList = new HashMap<>();//<topic, top docuemnt list

    public DocumentTopicMatrixReader(String compositionFilePath, int topicCount) {
        this.docTopicMatrixReader = getDocTopicMatrixReader(compositionFilePath, topicCount);
    }

    private RealMatrix getDocTopicMatrixReader(String compositionFilePath, int topicCount) {
        RealMatrix docTopicMatrix = null;
        ArrayList<String> composition = new ArrayList<>();
        int docCount = 0;
        try{
            try(
                    InputStream in = new FileInputStream(compositionFilePath);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                String line;
                while((line = reader.readLine())!=null) {
                    docCount++;
                    composition.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        docTopicMatrix = MatrixUtils.createRealMatrix(docCount, topicCount);
        for(int i = 0; i < topicCount; i ++) {
            Iterator it = composition.iterator();
            int row = 0;
            double maxValue = 0;
            double secondMaxValue = 0;
            double thirdMaxValue = 0;
            String maxDocument = "";
            String secondMaxDocument = "";
            String thirdMaxDocument = "";
            while(it.hasNext()) {
                String[] cols = it.next().toString().split("\t| ");//cols[0] is the row index in document-topic matrix, col[1] is the file name
                String fileName = cols[1];
                fileList.put(Integer.parseInt(cols[0]), fileName);
                double value = Double.parseDouble(cols[i+2]);
                if (value > maxValue) {
                    maxDocument = fileName;
                    maxValue = value;
                } else if (value > secondMaxValue) {
                    secondMaxDocument = fileName;
                    secondMaxValue = value;
                } else if (value > thirdMaxValue) {
                    thirdMaxDocument = fileName;
                    thirdMaxValue = value;
                }
                docTopicMatrix.setEntry(row, i, value);
                row++;
            }
            String[] topDocuments = {maxDocument, secondMaxDocument, thirdMaxDocument};
            topDocumentList.put(i, topDocuments);
        }

        //test for topDocumentList
//        for(Map.Entry<Integer, String[]> entry:topDocumentList.entrySet()) {
//            int index = entry.getKey();
//            System.out.print(index + ":");
//            String[] strs = entry.getValue();
//            for(int i = 0; i < 3; i++) {
//                System.out.println(strs[i]);
//            }
//            System.out.println();
//        }

        return docTopicMatrix;
    }

    public Map<Integer, String[]> getTopDocumentList() {
        return topDocumentList;
    }

    @Override
    public RealMatrix read() {
        return docTopicMatrixReader;
    }

    @Override
    public Map<String, Integer> getColumnHeaderList() {
        return null;
    }

    @Override
    public Map<Integer, String> getRowHeaderList() {
        return fileList;
    }
}
