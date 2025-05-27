import java.io.*;
import java.util.*;

public class mergesort {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String inputFile = "dataset/dataset_py.csv";
        String outputFile = "mergesort/mergesort_java.csv";
        String stepsFile = "mergesort/mergesort_java_step.txt";

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
        } else {
            int start = 0, end = 0;
            while (true) {
                try {
                    System.out.print("Enter start row (1-based): ");
                    start = Integer.parseInt(scanner.nextLine().trim());
                    System.out.print("Enter end row (1-based): ");
                    end = Integer.parseInt(scanner.nextLine().trim());
                    if (start < 1 || end > dataSize || start > end) {
                        System.out.println("Invalid range. Please try again.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter numbers.");
                }
            }
            // Sublist is 0-based and end-exclusive
            selectedData = data.subList(start - 1, end);
        }

        // Sort and record time
        List<List<Pair>> steps = new ArrayList<>();
        long startTime = System.nanoTime();
        List<Pair> sorted = mergeSort(selectedData, steps);
        long endTime = System.nanoTime();
        double sortingTime = (endTime - startTime) / 1e9;
        System.out.printf("Sorting time (excluding reading and saving steps): %.6f seconds\n", sortingTime);

        // Save sorted data
        writeCsv(outputFile, sorted);
        System.out.printf("Sorted data saved to %s%n", outputFile);

        // Save steps
        writeSteps(stepsFile, steps);
        System.out.printf("Merge sort steps saved to %s%n", stepsFile);
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

    static List<Pair> mergeSort(List<Pair> arr, List<List<Pair>> steps) {
        if (arr.size() <= 1) return new ArrayList<>(arr);
        int mid = arr.size() / 2;
        List<Pair> left = mergeSort(arr.subList(0, mid), steps);
        List<Pair> right = mergeSort(arr.subList(mid, arr.size()), steps);
        List<Pair> merged = merge(left, right);
        steps.add(new ArrayList<>(merged));
        return merged;
    }

    static List<Pair> merge(List<Pair> left, List<Pair> right) {
        List<Pair> merged = new ArrayList<>();
        int i = 0, j = 0;
        while (i < left.size() && j < right.size()) {
            if (left.get(i).number < right.get(j).number) {
                merged.add(left.get(i++));
            } else {
                merged.add(right.get(j++));
            }
        }
        while (i < left.size()) merged.add(left.get(i++));
        while (j < right.size()) merged.add(right.get(j++));
        return merged;
    }
}