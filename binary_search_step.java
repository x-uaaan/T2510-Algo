import java.io.*;
import java.util.Scanner;

public class binary_search_step {
    public static void main(String[] args) {
        final int MAX_SIZE = 1000;
        int[] nums = new int[MAX_SIZE];
        String[] strs = new String[MAX_SIZE];
        int size = 0;
        Scanner scanner = new Scanner(System.in);

        // User input
        System.out.print("Enter dataset filename: ");
        String filename = scanner.nextLine();

        System.out.print("Enter target integer: ");
        int target = Integer.parseInt(scanner.nextLine());

        // Read CSV into arrays
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null && size < MAX_SIZE) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",", 2);
                    nums[size] = Integer.parseInt(parts[0].trim());
                    strs[size] = parts.length > 1 ? parts[1].trim() : "";
                    size++;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file.");
            return;
        }

        // Prepare output file
        String outFilename = "binary_search_step_" + target + ".txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(outFilename))) {
            int left = 0;
            int right = size - 1;
            boolean found = false;

            while (left <= right) {
                int mid = left + (right - left) / 2;
                writer.println((mid + 1) + ": " + nums[mid] + "/" + strs[mid]);

                if (nums[mid] == target) {
                    found = true;
                    break;
                } else if (nums[mid] < target) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }

            if (!found) {
                writer.println("-1");
            }

            System.out.println("Search complete. Output written to: " + outFilename);

        } catch (IOException e) {
            System.err.println("Error writing to file.");
        }
    }
}
