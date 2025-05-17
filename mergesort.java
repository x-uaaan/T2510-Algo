import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class mergesort {

    public static void main(String[] args) {
        // Import dataset from CSV file
        int[] dataset = importData("dataset/dataset_int.csv");

        if (dataset == null) {
            System.out.println("Failed to load dataset.");
            return;
        }

        long startTime = System.nanoTime();
        
        // Sort digits within each number
        String[] sortedNumbers = new String[dataset.length];
        for (int i = 0; i < dataset.length; i++) {
            sortedNumbers[i] = sortDigits(dataset[i]);
        }
        
        long endTime = System.nanoTime();

        // Write sorted array to CSV file
        writeToFile(sortedNumbers, "mergesort/merge_sort_n.csv");

        // Print running time
        System.out.println("\nRunning Time: " + (endTime - startTime) + " nanoseconds");
    }

    // Import data from CSV file
    public static int[] importData(String fileName) {
        ArrayList<Integer> dataList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    // Trim the value and try to parse it as an integer
                    String trimmedValue = line.trim();
                    if (!trimmedValue.isEmpty()) {
                        dataList.add(Integer.parseInt(trimmedValue));
                    }
                } catch (NumberFormatException e) {
                    // Skip invalid values
                    System.out.println("Warning: Skipping invalid value: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
            return null;
        }

        // Convert ArrayList to array
        int[] dataArray = new int[dataList.size()];
        for (int i = 0; i < dataList.size(); i++) {
            dataArray[i] = dataList.get(i);
        }
        return dataArray;
    }

    // Merge Sort function
    public static void mergeSort(int[] array, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;

            // Split into two halves
            mergeSort(array, left, mid);
            mergeSort(array, mid + 1, right);

            // Merge the sorted halves
            merge(array, left, mid, right);
        }
    }

    // Merge function
    public static void merge(int[] array, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        // Temporary arrays
        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        // Copy data to temporary arrays
        for (int i = 0; i < n1; i++) {
            leftArray[i] = array[left + i];
        }
        for (int i = 0; i < n2; i++) {
            rightArray[i] = array[mid + 1 + i];
        }

        // Merge the temporary arrays
        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
            }
            k++;
        }

        // Copy remaining elements of leftArray
        while (i < n1) {
            array[k] = leftArray[i];
            i++;
            k++;
        }

        // Copy remaining elements of rightArray
        while (j < n2) {
            array[k] = rightArray[j];
            j++;
            k++;
        }
    }

    // Function to sort digits within a number
    public static String sortDigits(int number) {
        // Convert number to array of digits
        String numStr = String.valueOf(number);
        int[] digits = new int[numStr.length()];
        for (int i = 0; i < numStr.length(); i++) {
            digits[i] = Character.getNumericValue(numStr.charAt(i));
        }

        // Sort the digits using merge sort
        mergeSort(digits, 0, digits.length - 1);

        // Convert sorted digits back to string to preserve leading zeros
        StringBuilder result = new StringBuilder();
        for (int digit : digits) {
            result.append(digit);
        }
        return result.toString();
    }

    // Write sorted array to CSV file
    public static void writeToFile(String[] array, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            // Write each number on a new line, left-aligned
            for (String num : array) {
                writer.write(num + "\n");
            }
            System.out.println("\nSorted array written to " + fileName);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }
}
