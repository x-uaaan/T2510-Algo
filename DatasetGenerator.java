import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class DatasetGenerator {
    // Ranges and their percentages
    private static final int[][] RANGES = {
        {100, 1000},
        {1000, 10_000},
        {10_000, 100_000},
        {100_000, 1_000_000},
        {1_000_000, 10_000_000},
        {10_000_000, 100_000_000},
        {100_000_000, 500_000_000},
        {500_000_000, 1_000_000_000}
    };
    private static final double[] PERCENTAGES = {
        0.125, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125
    };
    private static final int STRING_MIN_LEN = 4;
    private static final int STRING_MAX_LEN = 6;
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz";

    public static void main(String[] args) throws IOException {
        long total = 1000_000L; // limit dataset to 1000 rows
        String outputPath = "dataset/dataset.csv";
        //String outputPath = "dataset/dataset_" + total + ".csv";
        generateDataset(total, outputPath);
    }

    public static void generateDataset(long total, String outputPath) throws IOException {
        Random rand = new Random();
        ArrayList<String> rows = new ArrayList<>();
        for (int i = 0; i < RANGES.length; i++) {
            long count = (long) (total * PERCENTAGES[i]);
            int min = RANGES[i][0];
            int max = RANGES[i][1];
            for (long j = 0; j < count; j++) {
                int number = min + rand.nextInt(max - min + 1); // inclusive upper bound
                String str = randomString(rand, STRING_MIN_LEN, STRING_MAX_LEN);
                rows.add(number + "," + str);
            }
        }
        Collections.shuffle(rows, rand);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            for (String row : rows) {
                writer.write(row + "\n");
            }
        }
    }

    private static String randomString(Random rand, int minLen, int maxLen) {
        int len = minLen + rand.nextInt(maxLen - minLen + 1);
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(CHARACTERS.charAt(rand.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
} 