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

        System.out.print("Enter data filename: ");
        String filename = scanner.nextLine();


        int[] numData = new int[max_index];
        int count = 0;

        //Read file to put data
        try (BufferedReader readFile = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = readFile.readLine()) != null && count < max_index) {
                String[] parts = line.split(",", 2);
                if (parts.length >= 1) {
                    int num = Integer.parseInt(parts[0]);
                    numData[count] = num;

                    count++;
                }
            }
        }catch(IOException e){
            System.out.println("Error! File not found");
            return;
        }

        //run best, average and worst case
        double bestTime = Double.MAX_VALUE;
        double averageTime = 0.0;
        double worstTime = 0.0;

        double time = 0.0;
        double totalTime = 0.0;     
        for(int i=0; i<count; i++){
            double start = System.nanoTime();
            binarySearch(numData, count, numData[i]);

            time = (System.nanoTime() - start) / 1e9;
            totalTime += time;

            if(bestTime <= 0 && i == 0){
                bestTime = time;
            }

            bestTime = Math.min(bestTime, time);
            worstTime = Math.max(worstTime, time);
        }
        averageTime = totalTime / count;

        //Write result to binary_search_n.txt file
        String outFilename = "binary_search_" + count + ".txt";

        try(PrintWriter writeFile = new PrintWriter(new FileWriter(outFilename))){
            System.out.println("Test complete. Please check the result on " + outFilename);

            writeFile.printf("Best case time: %.12f seconds%n", bestTime);
            writeFile.printf("Average case time: %.12f seconds%n", averageTime);
            writeFile.printf("Worst case time: %.12f seconds%n", worstTime);
        }catch(IOException e){
            System.out.println("Error! Fail to write file");
        }
    }
}