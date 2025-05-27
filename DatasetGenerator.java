import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DatasetGenerator {
    private static final int MAX_VALUE = 1_000_000_000; // integer value
    private static final int DATASET_SIZE = 1_000; // dataset size
    private static final int MIN_STRING_LENGTH = 4;
    private static final int MAX_STRING_LENGTH = 6;

    public static List<Pair<Integer, String>> generateDataset(int size, int maxValue, int minStringLength, int maxStringLength) {
        List<Pair<Integer, String>> dataset = new ArrayList<>();
        String characters = "abcdefghijklmnopqrstuvwxyz";
        Set<Integer> uniqueNumbers = new HashSet<>();
        Random random = new Random();

        // Shuffle the dataset to ensure random order
        Collections.shuffle(dataset);
        return dataset;
    }

    public static List<Pair<Integer, String>> generateDatasetWithRanges(int totalSize, List<int[]> ranges, int minStringLength, int maxStringLength) {
        List<Pair<Integer, String>> dataset = new ArrayList<>();
        String characters = "abcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        int sizePerRange = totalSize / ranges.size();

        for (int[] range : ranges) {
            int min = range[0];
            int max = range[1];
            Set<Integer> uniqueNumbers = new HashSet<>();
            while (uniqueNumbers.size() < sizePerRange) {
                int randomNumber = random.nextInt(max - min + 1) + min;
                if (uniqueNumbers.add(randomNumber)) {
                    int stringLength = random.nextInt(maxStringLength - minStringLength + 1) + minStringLength;
                    StringBuilder randomString = new StringBuilder();
                    for (int i = 0; i < stringLength; i++) {
                        randomString.append(characters.charAt(random.nextInt(characters.length())));
                    }
                    dataset.add(new Pair<>(randomNumber, randomString.toString()));
                }
            }
        }
        Collections.shuffle(dataset);
        return dataset;
    }

    public static void writeDatasetToFile(List<Pair<Integer, String>> dataset, String fileName) throws IOException {
        // Create dataset directory if it doesn't exist
        File datasetDir = new File("dataset");
        if (!datasetDir.exists()) {
            datasetDir.mkdir();
        }

        try (FileWriter writer = new FileWriter("dataset/" + fileName)) {
            for (Pair<Integer, String> pair : dataset) {
                writer.write(pair.getFirst() + "," + pair.getSecond() + "\n");
            }
        }
    }

    public static void main(String[] args) {
        try {
            // Define the 10 ranges
            List<int[]> ranges = Arrays.asList(
                new int[]{0, 10},
                new int[]{11, 100},
                new int[]{101, 1000},
                new int[]{1001, 10000},
                new int[]{10001, 100000},
                new int[]{100001, 1000000},
                new int[]{1000001, 10000000},
                new int[]{10000001, 100000000},
                new int[]{100000001, 500000000},
                new int[]{500000001, 1000000000}
            );
            // Generate the dataset with ranges
            List<Pair<Integer, String>> dataset = generateDatasetWithRanges(DATASET_SIZE, ranges, MIN_STRING_LENGTH, MAX_STRING_LENGTH);
            // Write the dataset to a file
            String outputFileName = "dataset_java_" + DATASET_SIZE + ".csv";
            String localFilePath = "dataset/" + outputFileName;
            writeDatasetToFile(dataset, outputFileName);
            System.out.println("Dataset generated and written to " + localFilePath);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // Simple Pair class to hold number-string pairs
    static class Pair<F, S> {
        private final F first;
        private final S second;

        public Pair(F first, S second) {
            this.first = first;
            this.second = second;
        }

        public F getFirst() {
            return first;
        }

        public S getSecond() {
            return second;
        }
    }
}