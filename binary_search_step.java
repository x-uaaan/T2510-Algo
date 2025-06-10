import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class binary_search_step {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Ask user for input
        System.out.print("Enter dataset filename: ");
        String filename = scanner.nextLine();

        System.out.print("Enter target integer: ");
        int target;
        try {
            target = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid integer input.");
            return;
        }

        ArrayList<Integer> data = new ArrayList<>();

        // Read dataset CSV
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    int number = Integer.parseInt(parts[0].trim());
                    data.add(number);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading input file.");
            return;
        }

        // initialise txt file name
        String outFilename = "binary_search_step_" + target + ".txt";

        // Binary Search Algo
        try (PrintWriter writer = new PrintWriter(new FileWriter(outFilename))) {
            int left = 0;
            int right = data.size() - 1;
            boolean found = false;

            while (left <= right) {
                int mid = left + (right - left) / 2;
                writer.println("Compared value: " + data.get(mid) + " at row: " + (mid + 1));

                if (data.get(mid) == target) {
                    writer.println("Target found at row: " + (mid + 1));
                    found = true;
                    break;
                } else if (data.get(mid) < target) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }

            if (!found) {
                writer.println("Target not found");
            }

            System.out.println("Search complete. Output written to: " + outFilename);

        } catch (IOException e) {
            System.out.println("Error writing to output file.");
        }
    }
}
