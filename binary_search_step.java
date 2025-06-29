import java.io.*;
import java.util.*;

public class binary_search_step {
    private final List<String> steps = new ArrayList<>();

    public void recordStep(String step) {
        steps.add(step);
    }

    public void writeSteps(String filePath) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String step : steps) {
                bw.write(step);
                bw.newLine();
            }
        }
    }

    public List<String> getSteps() {
        return steps;
    }

    public static void main(String[] args) throws IOException {
        // List all .csv files in /Java
        File datasetDir = new File("Java");
        File[] csvFiles = datasetDir.listFiles((dir, name) -> name.endsWith(".csv"));
        if (csvFiles == null || csvFiles.length == 0) {
            System.out.println("No .csv files found.");
            return;
        }
        System.out.println("Available CSV files in /Java:");
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
        String filename = csvFiles[fileChoice - 1].getPath();

        System.out.print("Enter target number to search: ");
        int target = Integer.parseInt(scanner.nextLine().trim());

        List<Integer> numData = new ArrayList<>();
        // Read file to put data
        try (BufferedReader readFile = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = readFile.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length >= 1) {
                    int num = Integer.parseInt(parts[0]);
                    numData.add(num);
                }
            }
        } catch(IOException e){
            System.out.println("Error! File not found");
            return;
        }
        int count = numData.size();

        binary_search_step stepRecorder = new binary_search_step();
        int low = 0, high = count - 1, foundIndex = -1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            // Record the step as row: value (1-based row number)
            stepRecorder.recordStep((mid + 1) + ": " + numData.get(mid));
            if (numData.get(mid) == target) {
                foundIndex = mid;
                break;
            } else if (numData.get(mid) < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        if (foundIndex == -1) {
            stepRecorder.recordStep("-1");
        }
        String stepsOutput = String.format("Java/binary_search_step_%d.txt", target);
        stepRecorder.writeSteps(stepsOutput);
        System.out.println("\n--------------------------------");
        System.out.printf("Binary search steps saved to %s\n", stepsOutput);
        if (foundIndex != -1) {
            System.out.println("Target found at row: " + (foundIndex + 1));
        } else {
            System.out.println("Target not found");
        }
        System.out.println("--------------------------------");
    }
} 