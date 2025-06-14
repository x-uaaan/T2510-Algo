import java.io.*;
import java.util.*;

public class quick_sort {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String inputFile = "dataset/dataset.csv";
        int startRow = 1, endRow = 0;
        // Read data
        List<Pair> data = readCsv(inputFile);
        int dataSize = data.size();

        // User input for range selection
        System.out.printf("Dataset size: %d rows\n", dataSize);
        System.out.print("Run the whole dataset? (Y/N): ");
        String choice = scanner.nextLine().trim();

        List<Pair> selectedData;
        if (choice.equalsIgnoreCase("Y")) {
            selectedData = data;
            startRow = 1;
            endRow = dataSize;
        } else {
            while (true) {
                try {
                    System.out.print("Enter start row (1-based): ");
                    startRow = Integer.parseInt(scanner.nextLine().trim());
                    System.out.print("Enter end row (1-based): ");
                    endRow = Integer.parseInt(scanner.nextLine().trim());
                    if (startRow < 1 || endRow > dataSize || startRow > endRow) {
                        System.out.println("Invalid range. Please try again.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter numbers.");
                }
            }
            // Sublist is 0-based and end-exclusive
            selectedData = data.subList(startRow - 1, endRow);
        }

        // Prepare output filenames
        String outputFile = String.format("dataset/quick_sort_java_%d_%d.csv", startRow, endRow);
        String stepsFile = String.format("dataset/quick_sort_java_steps_%d_%d.txt", startRow, endRow);

        // Sort and record time
        List<List<Pair>> steps = new ArrayList<>();
        // Record initial state
        steps.add(new ArrayList<>(selectedData));
        long startTime = System.nanoTime();
        inPlaceQuickSort(selectedData, 0, selectedData.size() - 1, steps);
        long endTime = System.nanoTime();
        double sortingTime = (endTime - startTime) / 1e9;
        System.out.printf("Sorting time (excluding reading and saving steps): %.6f seconds\n", sortingTime);

        // Save sorted data
        writeCsv(outputFile, selectedData);
        System.out.printf("Sorted data saved to %s%n", outputFile);

        // Save steps
        writeSteps(stepsFile, steps);
        System.out.printf("Quick sort steps saved to %s%n", stepsFile);
    }

    static class Pair {
        int number;
        String text;
        Pair(int number, String text) {
            this.number = number;
            this.text = text;
        }
        @Override
        public String toString() {
            return "[" + number + "/" + text + "]";
        }
    }

    static List<Pair> readCsv(String filePath) throws IOException {
        List<Pair> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 2) continue;
                int num = Integer.parseInt(parts[0].trim());
                String text = parts[1].trim();
                data.add(new Pair(num, text));
            }
        }
        return data;
    }

    static void writeCsv(String filePath, List<Pair> data) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Pair p : data) {
                bw.write(p.number + "," + p.text);
                bw.newLine();
            }
        }
    }

    static void writeSteps(String filePath, List<List<Pair>> steps) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (List<Pair> step : steps) {
                bw.write(step.toString());
                bw.newLine();
            }
        }
    }

    // In-place quick sort with step recording
    static void inPlaceQuickSort(List<Pair> arr, int low, int high, List<List<Pair>> steps) {
        if (low < high) {
            int pi = partition(arr, low, high, steps);
            inPlaceQuickSort(arr, low, pi - 1, steps);
            inPlaceQuickSort(arr, pi + 1, high, steps);
        }
    }

    static int partition(List<Pair> arr, int low, int high, List<List<Pair>> steps) {
        Pair pivot = arr.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr.get(j).number <= pivot.number) {
                i++;
                Collections.swap(arr, i, j);
            }
        }
        Collections.swap(arr, i + 1, high);
        // Record the current state after each partition
        steps.add(new ArrayList<>(arr));
        return i + 1;
    }
} 