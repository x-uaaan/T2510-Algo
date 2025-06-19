import java.io.*;
import java.util.Scanner;

public class quick_sort_step {

    // Helper method to print the array to the output file
    private static void printArray(int[] nums, String[] strs, int size, PrintWriter out) {
        out.print("[");
        for (int i = 0; i < size; i++) {
            out.print(nums[i] + "/" + strs[i]);
            if (i != size - 1) out.print(", ");
        }
        out.println("]");
    }

    // Partition function for quicksort
    private static int partition(int[] nums, String[] strs, int low, int high, PrintWriter out) {
        int pivot = nums[high];         // Choose last element as pivot
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (nums[j] < pivot) {
                i++;
                // Swap both number and string together
                int tempNum = nums[i];
                nums[i] = nums[j];
                nums[j] = tempNum;

                String tempStr = strs[i];
                strs[i] = strs[j];
                strs[j] = tempStr;
            }
        }

        // Place pivot in correct position
        int tempNum = nums[i + 1];
        nums[i + 1] = nums[high];
        nums[high] = tempNum;

        String tempStr = strs[i + 1];
        strs[i + 1] = strs[high];
        strs[high] = tempStr;

        int pivotIndex = i + 1;
        // Output the current pivot index and array after partition
        out.print("pi=" + pivotIndex + " ");
        printArray(nums, strs, nums.length, out);
        return pivotIndex;
    }

    // Recursive quicksort algorithm
    private static void quickSort(int[] nums, String[] strs, int low, int high, PrintWriter out) {
        if (low < high) {
            int pi = partition(nums, strs, low, high, out);
            quickSort(nums, strs, low, pi - 1, out);   // Sort left partition
            quickSort(nums, strs, pi + 1, high, out);  // Sort right partition
        }
    }

    public static void main(String[] args) {
        String filename = "dataset_sample_1000.csv"; // Dataset filename is fixed
        Scanner scanner = new Scanner(System.in);

        // Prompt user for start and end rows
        System.out.print("Enter start row: ");
        int startRow = scanner.nextInt();
        System.out.print("Enter end row: ");
        int endRow = scanner.nextInt();

        int size = endRow - startRow + 1;
        if (size <= 0) {
            System.err.println("Invalid row range.");
            return;
        }

        int[] nums = new int[size];          // Array for integers
        String[] strs = new String[size];    // Array for strings

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int row = 1;
            int index = 0;

            // Read only lines within the requested range
            while ((line = reader.readLine()) != null) {
                if (row >= startRow && row <= endRow) {
                    String[] parts = line.split(",");
                    nums[index] = Integer.parseInt(parts[0].trim());
                    strs[index] = parts[1].trim();
                    index++;
                }
                row++;
            }
        } catch (IOException e) {
            System.err.println("Error reading file.");
            return;
        }

        String outFilename = "quick_sort_step_" + startRow + "_" + endRow + ".txt";

        try (PrintWriter out = new PrintWriter(new FileWriter(outFilename))) {
            // Print initial array
            printArray(nums, strs, size, out);

            // Perform quicksort
            quickSort(nums, strs, 0, size - 1, out);

        } catch (IOException e) {
            System.err.println("Error writing output file.");
        }

        System.out.println("Sorting complete. Output written to " + outFilename);
    }
}
