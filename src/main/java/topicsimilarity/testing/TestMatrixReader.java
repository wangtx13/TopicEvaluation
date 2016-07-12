package topicsimilarity.testing;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import topicsimilarity.MatrixReader;

public class TestMatrixReader implements MatrixReader {
    private static int ROWS = 3;
    private static int COLUMNS = 3;

    private RealMatrix topicWordMatrix;

    public TestMatrixReader() {
        topicWordMatrix = getTestMatrix();
    }

    private static RealMatrix getTestMatrix() {
        RealMatrix topicWordMatrix = MatrixUtils.createRealMatrix(ROWS, COLUMNS);
        topicWordMatrix.addToEntry(0, 0, 0.1);
        topicWordMatrix.addToEntry(0, 1, 0.2);
        topicWordMatrix.addToEntry(0, 2, 0.7);
        topicWordMatrix.addToEntry(1, 0, 0.2);
        topicWordMatrix.addToEntry(1, 1, 0.3);
        topicWordMatrix.addToEntry(1, 2, 0.5);
        topicWordMatrix.addToEntry(2, 0, 0.2);
        topicWordMatrix.addToEntry(2, 1, 0.3);
        topicWordMatrix.addToEntry(2, 2, 0.5);
        return topicWordMatrix;
    }

    @Override
    public RealMatrix read() {
        return topicWordMatrix;
    }
}
