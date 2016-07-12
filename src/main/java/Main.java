import java.util.Map;

/**
 * Created by u on 16/7/12.
 */
public class Main {
    public static void main(String args[]) {


        TopicSimilarity testTS = new TopicSimilarity("");
        testTS.test();




    }


    public static void printMap(Map<String, Double> map) {
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            System.out.println("[Key] : " + entry.getKey()
                    + " [Value] : " + entry.getValue());
        }
    }


}
