package matrixreader;

import org.apache.commons.math3.linear.RealMatrix;

import java.util.Map;

/**
 * Created by tianxia on 16/7/12.
 */
public interface MatrixReader {
    RealMatrix read();

    Map<String,Integer> getColumnHeaderList();

    Map<Integer, String> getRowHeaderList();
}
