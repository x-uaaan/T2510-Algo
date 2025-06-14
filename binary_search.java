import java.io.*;
import java.util.*;

public class binary_search{
    static final int max_index = 1000000000;

    //binary search algorithm
    public static boolean binarySearch(int[] data, int dataSize, int target){
        int left = 0, right = dataSize - 1;

        while(left<=right){
            int mid = (left + right) / 2;
            int keyValue = data[mid];

            if(keyValue == target){
                return true;
            }else if(keyValue < target){
                left = mid + 1;
            }else{
                right = mid - 1;
            }
        }

        return false;
    }

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);

        // List all .csv files in /dataset
        File datasetDir = new File("dataset");
        File[] csvFiles = datasetDir.listFiles((dir, name) -> name.endsWith(".csv"));
        if (csvFiles == null || csvFiles.length == 0) {
            System.out.println("No .csv files found in /dataset directory.");
            return;
        }
        System.out.println("Available CSV files in /dataset:");
        for (int i = 0; i < csvFiles.length; i++) {
            System.out.printf("%d: %s\n", i + 1, csvFiles[i].getName());
        }
        int fileChoice = -1;
        while (fileChoice < 1 || fileChoice > csvFiles.length) {
            System.out.print("Select a file by number: ");
            try {
                fileChoice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                fileChoice = -1;
            }
        }
        String filename = "dataset/" + csvFiles[fileChoice - 1].getName();

        System.out.print("Enter target number to search: ");
        int target = Integer.parseInt(scanner.nextLine().trim());

        List<Integer> numData = new ArrayList<>();
        List<String> textData = new ArrayList<>();

        // Read file to put data
        try (BufferedReader readFile = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = readFile.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length >= 1) {
                    int num = Integer.parseInt(parts[0]);
                    numData.add(num);
                    if (parts.length == 2) {
                        textData.add(parts[1].trim());
                    } else {
                        textData.add("");
                    }
                }
            }
        } catch(IOException e){
            System.out.println("Error! File not found");
            return;
        }
        int count = numData.size();

        //run best, average and worst case
        double bestTime = Double.MAX_VALUE;
        double averageTime = 0.0;
        double worstTime = 0.0;

        double time = 0.0;
        double totalTime = 0.0;     
        for(int i=0; i<count; i++){
            double start = System.nanoTime();
            binarySearch(numData.stream().mapToInt(Integer::intValue).toArray(), count, numData.get(i));

            time = (System.nanoTime() - start) / 1e9;
            totalTime += time;

            if(bestTime <= 0 && i == 0){
                bestTime = time;
            }

            bestTime = Math.min(bestTime, time);
            worstTime = Math.max(worstTime, time);
        }
        averageTime = totalTime / count;

        // Prepare output file for steps
        String stepsOutput = String.format("dataset/binary_search_step_%d_%d.txt", count, target);
        List<String> stepLogs = new ArrayList<>();

        int low = 0, high = count - 1, foundIndex = -1;
        long startTime = System.nanoTime();
        while (low <= high) {
            int mid = low + (high - low) / 2;
            // Print the step as index: number/text
            stepLogs.add(mid + ": " + numData.get(mid) + "/" + textData.get(mid));
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
            stepLogs.add("-1");
        }
        long endTime = System.nanoTime();
        double searchTime = (endTime - startTime) / 1e9;

        // Save steps and timing to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(stepsOutput))) {
            for (String log : stepLogs) {
                writer.write(log);
                writer.newLine();
            }
            writer.newLine();
            writer.write(String.format("Best case time: %.9f seconds\n", bestTime));
            writer.write(String.format("Average case time: %.9f seconds\n", averageTime));
            writer.write(String.format("Worst case time: %.9f seconds\n", worstTime));
        } catch (IOException e) {
            System.out.println("Error writing steps file.");
        }
        System.out.printf("Binary search steps and timing saved to %s\n", stepsOutput);

        // Print result
        if (foundIndex != -1) {
            System.out.println("Target found at index: " + foundIndex);
        } else {
            System.out.println("Target not found");
        }
        System.out.printf("Search time: %.8f seconds\n", searchTime);
    }
}