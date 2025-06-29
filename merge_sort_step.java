import java.io.*;
import java.util.*;

public class merge_sort_step {
    private final List<List<Pair>> steps = new ArrayList<>();

    public void recordStep(List<Pair> currentState) {
        steps.add(new ArrayList<>(currentState));
    }

    public void writeSteps(String filePath) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (List<Pair> step : steps) {
                bw.write(step.toString());
                bw.newLine();
            }
        }
    }

    public List<List<Pair>> getSteps() {
        return steps;
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

    // In-place merge sort with step recording
    static void inPlaceMergeSort(List<Pair> arr, int left, int right, merge_sort_step stepRecorder) {
        if (left >= right) return;
        int mid = left + (right - left) / 2;
        inPlaceMergeSort(arr, left, mid, stepRecorder);
        inPlaceMergeSort(arr, mid + 1, right, stepRecorder);
        mergeWithSteps(arr, left, mid, right, stepRecorder);
    }

    static void mergeWithSteps(List<Pair> arr, int left, int mid, int right, merge_sort_step stepRecorder) {
        List<Pair> temp = new ArrayList<>();
        int i = left, j = mid + 1;
        while (i <= mid && j <= right) {
            if (arr.get(i).number <= arr.get(j).number) {
                temp.add(arr.get(i++));
            } else {
                temp.add(arr.get(j++));
            }
        }
        while (i <= mid) temp.add(arr.get(i++));
        while (j <= right) temp.add(arr.get(j++));
        for (int k = 0; k < temp.size(); k++) {
            arr.set(left + k, temp.get(k));
        }
        // Record the current state after each merge
        stepRecorder.recordStep(arr);
    }

    public static void main(String[] args) throws IOException {
        // List all .csv files in the current directory
        File dir = new File(".");
        File[] csvFiles = dir.listFiles((d, name) -> name.endsWith(".csv"));
        if (csvFiles == null || csvFiles.length == 0) {
            System.out.println("No .csv files found in the current directory.");
            return;
        }
        System.out.println("Available CSV files:");
        for (int i = 0; i < csvFiles.length; i++) {
            System.out.printf("%d: %s\n", i + 1, csvFiles[i].getName());
        }
        int fileChoice = -1;
        Scanner scanner = new Scanner(System.in);
        while (fileChoice < 1 || fileChoice > csvFiles.length) {
            System.out.print("Select a file by number: ");
            try {
                fileChoice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                fileChoice = -1;
            }
        }
        String inputFile = csvFiles[fileChoice - 1].getName();
        List<Pair> data = readCsv(inputFile);
        int dataSize = data.size();

        // User input for range selection
        System.out.printf("Dataset size: %d rows\n", dataSize);
        List<Pair> selectedData;
        int startRow = 1, endRow = 0;
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

        // Prepare output filenames
        String baseName = inputFile.replaceFirst("\\.csv$", "");
        String outputFile = String.format("Java/merge_sort_%s_%d_%d.csv", baseName, startRow, endRow);
        String stepsFile = String.format("Java/merge_sort_steps_%s_%d_%d.txt", baseName, startRow, endRow);

        // Sort and record time
        merge_sort_step stepRecorder = new merge_sort_step();
        // Record initial state
        stepRecorder.recordStep(selectedData);
        long startTime = System.nanoTime();
        inPlaceMergeSort(selectedData, 0, selectedData.size() - 1, stepRecorder);
        long endTime = System.nanoTime();
        double sortingTime = (endTime - startTime) / 1e9;

        System.out.println("\n--------------------------------");
        System.out.printf("Sorting time (excluding reading and saving steps): %.6f seconds\n", sortingTime);

        // Save sorted data
        writeCsv(outputFile, selectedData);
        System.out.printf("Sorted data saved to %s%n", outputFile);

        // Save steps
        stepRecorder.writeSteps(stepsFile);
        System.out.printf("Merge sort steps saved to %s%n", stepsFile);
        System.out.println("--------------------------------");
    }
} 