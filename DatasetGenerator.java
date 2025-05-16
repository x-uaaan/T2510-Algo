import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DatasetGenerator {
    public static void main(String[] args) {
        // Define the maximum value for the integers
        final int MAX_VALUE = 1_000_000_000;
        // Define the size of the dataset (adjust this to ensure sorting time differences)
        final int DATASET_SIZE = 10_000_000; // Example size, adjust as needed

        // Generate the dataset
        List<Integer> dataset = generateDataset(DATASET_SIZE, MAX_VALUE);

        // Write the dataset to a file
        String outputFileName = "dataset.csv";
        try {
            writeDatasetToFile(dataset, outputFileName);
            System.out.println("Dataset generated and written to " + outputFileName);
        } catch (IOException e) {
            System.err.println("Error writing dataset to file: " + e.getMessage());
        }
    }

    private static List<Integer> generateDataset(int size, int maxValue) {
        List<Integer> dataset = new ArrayList<>(size);
        Random random = new Random();

        // Generate unique random integers
        while (dataset.size() < size) {
            int randomNumber = random.nextInt(maxValue) + 1; // Ensure positive numbers
            if (!dataset.contains(randomNumber)) {
                dataset.add(randomNumber);
            }
        }

        // Shuffle the dataset to ensure random order
        Collections.shuffle(dataset);
        return dataset;
    }

    private static void writeDatasetToFile(List<Integer> dataset, String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int i = 0; i < dataset.size(); i++) {
                writer.write(String.valueOf(dataset.get(i)));
                if (i < dataset.size() - 1) {
                    writer.write(","); // Add a comma between numbers
                }
            }
        }
    }
}