import java.io.*;
import java.util.*;

public class quick_sort_step {
    private final List<String> steps = new ArrayList<>();

    public void recordInitialStep(List<Pair> currentState) {
        steps.add(currentState.toString());
    }

    public void recordStep(int pi, List<Pair> currentState) {
        steps.add(String.format("pi=%d %s", pi, currentState.toString()));
    }

    public void writeSteps(String filePath) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String step : steps) {
                bw.write(step);
                bw.newLine();
            }
        }
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

    // In-place quick sort with step recording (standalone)
    static void inPlaceQuickSort(List<Pair> arr, int low, int high, quick_sort_step stepRecorder) {
        if (low < high) {
            int pi = partition(arr, low, high, stepRecorder);
            inPlaceQuickSort(arr, low, pi - 1, stepRecorder);
            inPlaceQuickSort(arr, pi + 1, high, stepRecorder);
        }
    }

    static int partition(List<Pair> arr, int low, int high, quick_sort_step stepRecorder) {
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
        stepRecorder.recordStep(i + 1, arr);
        return i + 1;
    }

    public static void main(String[] args) throws IOException {
        // List all dataset_*.csv files in the current directory
        File dir = new File(".");
        File[] csvFiles = dir.listFiles((d, name) -> name.matches("dataset_\\d+\\.csv"));
        if (csvFiles == null || csvFiles.length == 0) {
            System.out.println("No dataset_[size].csv files found in the current directory.");
            return;
        }
        System.out.println("Available dataset CSV files:");
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
        selectedData = new ArrayList<>(data.subList(startRow - 1, endRow));

        // Prepare output filenames
        String baseName = inputFile.replaceFirst("\\.csv$", "");
        String stepsFile = String.format("Java/quick_sort_steps_%s_%d_%d.txt", baseName, startRow, endRow);

        // Sort and record time
        quick_sort_step stepRecorder = new quick_sort_step();
        // Record initial state
        stepRecorder.recordInitialStep(selectedData);
        long startTime = System.nanoTime();
        inPlaceQuickSort(selectedData, 0, selectedData.size() - 1, stepRecorder);
        long endTime = System.nanoTime();
        double sortingTime = (endTime - startTime) / 1e9;

        System.out.println("\n--------------------------------");
        System.out.printf("Sorting time (excluding reading and saving steps): %.6f seconds\n", sortingTime);

        // Save steps
        stepRecorder.writeSteps(stepsFile);
        System.out.printf("Quick sort steps saved to %s%n", stepsFile);
        System.out.println("--------------------------------");
    }
} 