import java.io.*;
import java.util.*;

public class merge_sort {
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
        String outputFile = String.format("Java/merge_sort_%d.csv", dataSize);

        // Sort and record time
        long startTime = System.nanoTime();
        inPlaceMergeSort(selectedData, 0, selectedData.size() - 1);
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

    // In-place merge sort without step recording
    static void inPlaceMergeSort(List<Pair> arr, int left, int right) {
        if (left >= right) return;
        int mid = left + (right - left) / 2;
        inPlaceMergeSort(arr, left, mid);
        inPlaceMergeSort(arr, mid + 1, right);
        merge(arr, left, mid, right);
    }

    static void merge(List<Pair> arr, int left, int mid, int right) {
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
    }
}