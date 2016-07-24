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
        Iterator it = composition.iterator();
        int row = 0;
        while(it.hasNext()){
            String[] cols = it.next().toString().split("\t| ");//cols[0] is the row index in document-topic matrix, col[1] is the file name
            fileList.put(Integer.parseInt(cols[0]), cols[1]);
            for(int i = 0; i < topicCount; i++) {
                docTopicMatrix.setEntry(row, i, Double.parseDouble(cols[i+2]));
            }
            row++;
        }

        return docTopicMatrix;
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
