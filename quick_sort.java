import java.io.*;
import java.util.*;

public class quick_sort {
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
        // Read data
        List<Pair> data = readCsv(inputFile);
        int dataSize = data.size();

        // Always use the full dataset
        List<Pair> selectedData = data;

        // Prepare output filename
        String outputFile = String.format("Java/quick_sort_%d.csv", dataSize);

        // Sort and record time
        long startTime = System.nanoTime();
        inPlaceQuickSort(selectedData, 0, selectedData.size() - 1);
        long endTime = System.nanoTime();
        double sortingTime = (endTime - startTime) / 1e9;

        System.out.println("\n--------------------------------");
        System.out.printf("Sorting time (excluding reading and saving steps): %.6f seconds\n", sortingTime);

        // Save sorted data
        writeCsv(outputFile, selectedData);
        System.out.printf("Sorted data saved to %s%n", outputFile);
        System.out.println("--------------------------------");
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

    // In-place quick sort without step recording
    static void inPlaceQuickSort(List<Pair> arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            inPlaceQuickSort(arr, low, pi - 1);
            inPlaceQuickSort(arr, pi + 1, high);
        }
    }

    static int partition(List<Pair> arr, int low, int high) {
        Pair pivot = arr.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr.get(j).number <= pivot.number) {
                i++;
                Collections.swap(arr, i, j);
            }
        }
        Collections.swap(arr, i + 1, high);
        return i + 1;
    }
} 