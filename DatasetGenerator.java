import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DatasetGenerator {
    private static final int MAX_VALUE = 1_000_000_000; // integer value
    private static final int DATASET_SIZE = 1_000; // dataset size
    //private static final int DATASET_SIZE = 1_000_0000; // dataset size
    private static final int MIN_STRING_LENGTH = 4;
    private static final int MAX_STRING_LENGTH = 6;

    public static List<Pair<Integer, String>> generateDataset(int size, int maxValue, int minStringLength, int maxStringLength) {
        List<Pair<Integer, String>> dataset = new ArrayList<>();
        String characters = "abcdefghijklmnopqrstuvwxyz";
        Set<Integer> uniqueNumbers = new HashSet<>();
        Random random = new Random();

        while (dataset.size() < size) {
            int randomNumber = random.nextInt(maxValue) + 1; // Ensure positive numbers

            // Generate random string
            int stringLength = random.nextInt(maxStringLength - minStringLength + 1) + minStringLength;
            StringBuilder randomString = new StringBuilder();
            for (int i = 0; i < stringLength; i++) {
                randomString.append(characters.charAt(random.nextInt(characters.length())));
            }

            // Check if number is unique
            if (uniqueNumbers.add(randomNumber)) {
                dataset.add(new Pair<>(randomNumber, randomString.toString()));
            }
        }

        // Shuffle the dataset to ensure random order
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
            // Generate the dataset
            List<Pair<Integer, String>> dataset = generateDataset(DATASET_SIZE, MAX_VALUE, MIN_STRING_LENGTH, MAX_STRING_LENGTH);

            // Write the dataset to a file
            String outputFileName = "dataset_java.csv";
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