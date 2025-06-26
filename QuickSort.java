import java.io.*;
import java.util.*;

// Class representing a single row of data from the CSV
class Data {
    int number;
    String text;

    Data(int number, String text) {
        this.number = number;
        this.text = text;
    }
}

public class QuickSort {

    // Parses one line of CSV input into a Data object
    static Data parseLine(String line) {
        String[] parts = line.split(",");
        return new Data(Integer.parseInt(parts[0]), parts[1]);
    }

    // Partition method used in QuickSort
    static int partition(List<Data> arr, int low, int high, BufferedWriter stepFile) throws IOException {
        int pivot = arr.get(high).number;
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr.get(j).number <= pivot) {
                i++;
                Collections.swap(arr, i, j);
            }
        }
        Collections.swap(arr, i + 1, high);
        return i + 1;
    }

    // Recursive QuickSort implementation
    static void quickSort(List<Data> arr, int low, int high, BufferedWriter stepFile) throws IOException {
        if (low < high) {
            int pi = partition(arr, low, high, stepFile);
            quickSort(arr, low, pi - 1, stepFile);
            quickSort(arr, pi + 1, high, stepFile);
        }
    }

    public static void main(String[] args) {
        List<Data> data = new ArrayList<>();

        // Read data from CSV file
        try (BufferedReader reader = new BufferedReader(new FileReader("dataset.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    data.add(parseLine(line));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading dataset file: " + e.getMessage());
            return;
        }

        // Get user input for sorting range
        int startRow, endRow;
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter start row: ");
            startRow = scanner.nextInt();
            System.out.print("Enter end row: ");
            endRow = scanner.nextInt();
        }

        // Validate range
        if (startRow < 1 || endRow > data.size() || startRow > endRow) {
            System.err.println("Invalid row range");
            return;
        }

        // Convert to 0-based indexing
        startRow--;
        endRow--;
        int n = endRow - startRow + 1;

        // Perform the sort and measure runtime (no step logging)
        List<Data> toSort = new ArrayList<>(data.subList(startRow, endRow + 1));
        long startTime = System.nanoTime();
        try {
            quickSort(toSort, 0, toSort.size() - 1, null);
        } catch (IOException e) {
            System.err.println("Unexpected error during sorting: " + e.getMessage());
        }
        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1_000_000_000.0;

        // Write sorted results to output file
        try (BufferedWriter outFile = new BufferedWriter(new FileWriter("quick_sort_" + n + ".csv"))) {
            for (Data d : toSort) {
                outFile.write(d.number + "," + d.text + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing sorted CSV: " + e.getMessage());
        }

        // Print final timing result
        System.out.printf("%d rows sorted. Running time = %.6f seconds%n", n, duration);
    }
}
