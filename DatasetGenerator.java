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
        final int MAX_VALUE = 1_000_000_0;
        // Define the size of the dataset 
        //final int DATASET_SIZE = 1_000; // testing size 
        final int DATASET_SIZE = 1_000_000_000; // production size
        // Define string length range
        final int MIN_STRING_LENGTH = 4;
        final int MAX_STRING_LENGTH = 6;

        // Generate the dataset
        List<DataEntry> dataset = generateDataset(DATASET_SIZE, MAX_VALUE, MIN_STRING_LENGTH, MAX_STRING_LENGTH);

        // Write the dataset to a file
        String outputFileName = "dataset.csv";
        try {
            writeDatasetToFile(dataset, outputFileName);
            System.out.println("Dataset generated and written to " + outputFileName);
        } catch (IOException e) {
            System.err.println("Error writing dataset to file: " + e.getMessage());
        }
    }

    private static class DataEntry {
        int number;
        String text;

        DataEntry(int number, String text) {
            this.number = number;
            this.text = text;
        }
    }

    private static List<DataEntry> generateDataset(int size, int maxValue, int minStringLength, int maxStringLength) {
        List<DataEntry> dataset = new ArrayList<>(size);
        Random random = new Random();
        String characters = "abcdefghijklmnopqrstuvwxyz";

        // Generate unique random integers with corresponding strings
        while (dataset.size() < size) {
            int randomNumber = random.nextInt(maxValue) + 1; // Ensure positive numbers
            
            // Generate random string
            int stringLength = random.nextInt(maxStringLength - minStringLength + 1) + minStringLength;
            StringBuilder randomString = new StringBuilder();
            for (int i = 0; i < stringLength; i++) {
                randomString.append(characters.charAt(random.nextInt(characters.length())));
            }

            // Check if number is unique
            boolean isUnique = true;
            for (DataEntry entry : dataset) {
                if (entry.number == randomNumber) {
                    isUnique = false;
                    break;
                }
            }

            if (isUnique) {
                dataset.add(new DataEntry(randomNumber, randomString.toString()));
            }
        }

        // Shuffle the dataset to ensure random order
        Collections.shuffle(dataset);
        return dataset;
    }

    private static void writeDatasetToFile(List<DataEntry> dataset, String fileName) throws IOException {
        // Create dataset directory if it doesn't exist
        new java.io.File("dataset").mkdirs();
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("dataset/" + fileName))) {
            for (DataEntry entry : dataset) {
                writer.write(entry.number + "," + entry.text);
                writer.newLine();
            }
        }
    }
}