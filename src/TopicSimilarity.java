import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by u on 16/7/11.
 */
public class TopicSimilarity {


    private String readFileToString(String filePath) throws IOException {

        StringBuilder fileData = new StringBuilder(1000);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            char[] buf = new char[10];
            int numRead = 0;
            while ((numRead = reader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
                buf = new char[1024];
            }

            reader.close();
            return fileData.toString();
        }

    }
}
