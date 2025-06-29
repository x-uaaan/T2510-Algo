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

            bestTime = Math.min(bestTime, time);
            worstTime = Math.max(worstTime, time);
        }
        averageTime = totalTime / count;

        // Output running times to file
        String timingOutput = String.format("Java/binary_search_%d.txt", count);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(timingOutput))) {
            writer.write(String.format("Best case time: %.9f seconds\n", bestTime));
            writer.write(String.format("Average case time: %.9f seconds\n", averageTime));
            writer.write(String.format("Worst case time: %.9f seconds\n", worstTime));
        } catch (IOException e) {
            System.out.println("Error writing timing file.");
        }
        System.out.println("\n--------------------------------");
        System.out.printf("Binary search running times saved to %s\n", timingOutput);
        System.out.println("Best case time: " + bestTime + " seconds");
        System.out.println("Average case time: " + averageTime + " seconds");
        System.out.println("Worst case time: " + worstTime + " seconds");
        System.out.println("--------------------------------");
    }
}