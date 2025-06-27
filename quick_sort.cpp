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

class QuickSort {
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
    
    static void inPlaceQuickSort(std::vector<Pair>& arr, int low, int high, std::vector<std::vector<Pair>>& steps) {
        if (low < high) {
            int pi = partition(arr, low, high, steps);
            inPlaceQuickSort(arr, low, pi - 1, steps);
            inPlaceQuickSort(arr, pi + 1, high, steps);
        }
    }
    
private:
    static int partition(std::vector<Pair>& arr, int low, int high, std::vector<std::vector<Pair>>& steps) {
        Pair pivot = arr[high];
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            if (arr[j].number <= pivot.number) {
                i++;
                std::swap(arr[i], arr[j]);
            }
        }
        std::swap(arr[i + 1], arr[high]);
        
        // Record the current state after each partition
        steps.push_back(arr);
        return i + 1;
    }
};

int main() {
    std::string inputFile = "../dataset.csv";
    int startRow = 1, endRow = 0;
    
    // Read data
    std::vector<Pair> data = QuickSort::readCsv(inputFile);
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
    std::string outputFile = "quick_sort_" + std::to_string(startRow) + "_" + std::to_string(endRow) + ".csv";
    std::string stepsFile = "quick_sort_steps_" + std::to_string(startRow) + "_" + std::to_string(endRow) + ".txt";
    
    // Sort and record time
    std::vector<std::vector<Pair>> steps;
    // Record initial state
    steps.push_back(selectedData);
    
    auto startTime = std::chrono::high_resolution_clock::now();
    QuickSort::inPlaceQuickSort(selectedData, 0, selectedData.size() - 1, steps);
    auto endTime = std::chrono::high_resolution_clock::now();
    
    double sortingTime = std::chrono::duration<double>(endTime - startTime).count();

    std::cout << "\n--------------------------------" << std::endl;
    std::cout << "Sorting time (excluding reading and saving steps): " << std::fixed << sortingTime << " seconds" << std::endl;
    
    // Save sorted data
    QuickSort::writeCsv(outputFile, selectedData);
    std::cout << "Sorted data saved to " << outputFile << std::endl;
    
    // Save steps
    QuickSort::writeSteps(stepsFile, steps);
    std::cout << "Quick sort steps saved to " << stepsFile << std::endl;
    std::cout << "--------------------------------" << std::endl;
    return 0;
} 