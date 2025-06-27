#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <sstream>
#include <chrono>
#include <algorithm>

struct Pair {
    int number;
    std::string text;
    
    Pair(int num, const std::string& txt) : number(num), text(txt) {}
    
    std::string toString() const {
        return "[" + std::to_string(number) + "/" + text + "]";
    }
};

class MergeSort {
public:
    static std::vector<Pair> readCsv(const std::string& filePath) {
        std::vector<Pair> data;
        std::ifstream file(filePath);
        std::string line;
        
        while (std::getline(file, line)) {
            std::stringstream ss(line);
            std::string numStr, textStr;
            
            if (std::getline(ss, numStr, ',') && std::getline(ss, textStr)) {
                int num = std::stoi(numStr);
                data.emplace_back(num, textStr);
            }
        }
        file.close();
        return data;
    }
    
    static void writeCsv(const std::string& filePath, const std::vector<Pair>& data) {
        std::ofstream file(filePath);
        for (const Pair& p : data) {
            file << p.number << "," << p.text << "\n";
        }
        file.close();
    }
    
    static void writeSteps(const std::string& filePath, const std::vector<std::vector<Pair>>& steps) {
        std::ofstream file(filePath);
        for (const std::vector<Pair>& step : steps) {
            file << "[";
            for (size_t i = 0; i < step.size(); i++) {
                if (i > 0) file << ", ";
                file << step[i].toString();
            }
            file << "]\n";
        }
        file.close();
    }
    
    static void inPlaceMergeSort(std::vector<Pair>& arr, int left, int right, std::vector<std::vector<Pair>>& steps) {
        if (left >= right) return;
        int mid = left + (right - left) / 2;
        inPlaceMergeSort(arr, left, mid, steps);
        inPlaceMergeSort(arr, mid + 1, right, steps);
        mergeWithSteps(arr, left, mid, right, steps);
    }
    
private:
    static void mergeWithSteps(std::vector<Pair>& arr, int left, int mid, int right, std::vector<std::vector<Pair>>& steps) {
        std::vector<Pair> temp;
        int i = left, j = mid + 1;
        
        while (i <= mid && j <= right) {
            if (arr[i].number <= arr[j].number) {
                temp.push_back(arr[i++]);
            } else {
                temp.push_back(arr[j++]);
            }
        }
        
        while (i <= mid) temp.push_back(arr[i++]);
        while (j <= right) temp.push_back(arr[j++]);
        
        for (size_t k = 0; k < temp.size(); k++) {
            arr[left + k] = temp[k];
        }
        
        // Record the current state after each merge
        steps.push_back(arr);
    }
};

int main() {
    std::string inputFile = "../dataset.csv";
    int startRow = 1, endRow = 0;
    
    // Read data
    std::vector<Pair> data = MergeSort::readCsv(inputFile);
    int dataSize = data.size();
    
    // User input for range selection
    std::cout << "Dataset size: " << dataSize << " rows" << std::endl;
    
    std::vector<Pair> selectedData;
    while (true) {
        try {
            std::cout << "Enter start row (1-based): ";
            std::cin >> startRow;
            std::cout << "Enter end row (1-based): ";
            std::cin >> endRow;
            
            if (startRow < 1 || endRow > dataSize || startRow > endRow) {
                std::cout << "Invalid range. Please try again." << std::endl;
            } else {
                break;
            }
        } catch (...) {
            std::cout << "Invalid input. Please enter numbers." << std::endl;
            std::cin.clear();
            std::cin.ignore(10000, '\n');
        }
    }
    
    // Sublist (0-based and end-exclusive equivalent)
    selectedData = std::vector<Pair>(data.begin() + startRow - 1, data.begin() + endRow);
    
    // Prepare output filenames
    std::string outputFile = "merge_sort_" + std::to_string(startRow) + "_" + std::to_string(endRow) + ".csv";
    std::string stepsFile = "merge_sort_steps_" + std::to_string(startRow) + "_" + std::to_string(endRow) + ".txt";
    
    // Sort and record time
    std::vector<std::vector<Pair>> steps;
    // Record initial state
    steps.push_back(selectedData);
    
    auto startTime = std::chrono::high_resolution_clock::now();
    MergeSort::inPlaceMergeSort(selectedData, 0, selectedData.size() - 1, steps);
    auto endTime = std::chrono::high_resolution_clock::now();
    
    double sortingTime = std::chrono::duration<double>(endTime - startTime).count();

    std::cout << "\n--------------------------------" << std::endl;
    std::cout << "Sorting time (excluding reading and saving steps): " << std::fixed << sortingTime << " seconds" << std::endl;
    
    // Save sorted data
    MergeSort::writeCsv(outputFile, selectedData);
    std::cout << "Sorted data saved to " << outputFile << std::endl;
    
    // Save steps
    MergeSort::writeSteps(stepsFile, steps);
    std::cout << "Merge sort steps saved to " << stepsFile << std::endl;
    std::cout << "--------------------------------" << std::endl;
    
    return 0;
} 