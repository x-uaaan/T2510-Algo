import java.io.*;
import java.util.*;

public class quick_sort_step {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        final String filename = "dataset_sample_1000.csv";

        // User input
        System.out.print("Enter start row: ");
        int startRow = scanner.nextInt();

        System.out.print("Enter end row: ");
        int endRow = scanner.nextInt();

        List<Integer> sublist = new ArrayList<>();

        // Read CSV and extract only relevant rows (integers)
        // Using list instead of array because it is a flexible container that adjust dynamically 
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int row = 1;
            while ((line = reader.readLine()) != null) {
                if (row >= startRow && row <= endRow) {
                    String[] parts = line.split(",");
                    sublist.add(Integer.parseInt(parts[0].trim()));
                }
                row++;
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + filename);
            return;
        }

        // Prepare output file and running the quicksort algorithm
        String outputFilename = "quick_sort_step_" + startRow + "_" + endRow + ".txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFilename))) {
            writer.println("Initial subarray: " + sublist);
            quickSort(sublist, 0, sublist.size() - 1, writer);
            writer.println("Sorted subarray: " + sublist);
        } catch (IOException e) {
            System.out.println("Error writing to output file.");
        }

        System.out.println("Sorting complete. Output written to " + outputFilename);
    }

    // Quicksort algorithm
    private static void quickSort(List<Integer> arr, int low, int high, PrintWriter writer) {
        if (low < high) {
            // Partition the array and get the pivot's final position
            int pivotIndex = partition(arr, low, high, writer);
            quickSort(arr, low, pivotIndex - 1, writer);
            quickSort(arr, pivotIndex + 1, high, writer);
        }
    }

    // The partition function to make the quicksort work
    private static int partition(List<Integer> arr, int low, int high, PrintWriter writer) {
        int pivot = arr.get(high);
        writer.println("Pivot chosen: " + pivot);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr.get(j) < pivot) {
                i++;
                Collections.swap(arr, i, j);
                writer.println("Swapped " + arr.get(j) + " with " + arr.get(i));
                writer.println("Current array: " + arr);
            }
        }

        Collections.swap(arr, i + 1, high);
        writer.println("Swapped pivot " + pivot + " to index " + (i + 1));
        writer.println("Current array: " + arr);
        return i + 1;
    }
}
