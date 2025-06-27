#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <sstream>
#include <chrono>
#include <algorithm>
#include <limits>
#include <cstdlib>
#include <cstdio>

#ifdef _WIN32
#ifndef NOMINMAX
#define NOMINMAX
#endif
#include <windows.h>
#include <io.h>
#else
#include <dirent.h>
#endif

class BinarySearch {
private:
    static const int max_index = 1000000000;

public:
    static bool binarySearch(const std::vector<int>& data, int dataSize, int target) {
        int left = 0, right = dataSize - 1;
        
        while (left <= right) {
            int mid = (left + right) / 2;
            int keyValue = data[mid];
            
            if (keyValue == target) {
                return true;
            } else if (keyValue < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return false;
    }
};

std::vector<std::string> getCsvFiles() {
    std::vector<std::string> csvFiles;
    
#ifdef _WIN32
    WIN32_FIND_DATA findFileData;
    HANDLE hFind = FindFirstFile("*.csv", &findFileData);
    
    if (hFind != INVALID_HANDLE_VALUE) {
        do {
            csvFiles.push_back(findFileData.cFileName);
        } while (FindNextFile(hFind, &findFileData) != 0);
        FindClose(hFind);
    }
#else
    DIR* dir = opendir(".");
    if (dir != NULL) {
        struct dirent* entry;
        while ((entry = readdir(dir)) != NULL) {
            std::string filename = entry->d_name;
            if (filename.size() > 4 && filename.substr(filename.size() - 4) == ".csv") {
                csvFiles.push_back(filename);
            }
        }
        closedir(dir);
    }
#endif
    
    return csvFiles;
}

int main() {
    // List all .csv files in /dataset
    std::vector<std::string> csvFiles = getCsvFiles();
    
    if (csvFiles.empty()) {
        std::cout << "No .csv files found." << std::endl;
        return 1;
    }
    
    std::cout << "Available CSV files in current directory:" << std::endl;
    for (size_t i = 0; i < csvFiles.size(); i++) {
        std::cout << (i + 1) << ": " << csvFiles[i] << std::endl;
    }
    
    int fileChoice = -1;
    while (fileChoice < 1 || fileChoice > static_cast<int>(csvFiles.size())) {
        std::cout << "Select a file by number: ";
        std::cin >> fileChoice;
        if (std::cin.fail()) {
            std::cin.clear();
            std::cin.ignore(10000, '\n');
            fileChoice = -1;
        }
    }
    
    std::string filename = csvFiles[fileChoice - 1];
    
    std::cout << "Enter target number to search: ";
    int target;
    std::cin >> target;
    
    std::vector<int> numData;
    std::vector<std::string> textData;
    
    // Read file to put data
    std::ifstream readFile(filename);
    std::string line;
    while (std::getline(readFile, line)) {
        std::stringstream ss(line);
        std::string numStr, textStr;
        
        if (std::getline(ss, numStr, ',')) {
            int num = std::stoi(numStr);
            numData.push_back(num);
            if (std::getline(ss, textStr)) {
                textData.push_back(textStr);
            } else {
                textData.push_back("");
            }
        }
    }
    readFile.close();
    
    int count = numData.size();
    
    // Run best, average and worst case
    double bestTime = (std::numeric_limits<double>::max)();
    double averageTime = 0.0;
    double worstTime = 0.0;
    double totalTime = 0.0;
    
    for (int i = 0; i < count; i++) {
        auto start = std::chrono::high_resolution_clock::now();
        BinarySearch::binarySearch(numData, count, numData[i]);
        auto end = std::chrono::high_resolution_clock::now();
        
        double time = std::chrono::duration<double>(end - start).count();
        totalTime += time;
        
        if (i == 0) {
            bestTime = time;
        }
        
        bestTime = (std::min)(bestTime, time);
        worstTime = (std::max)(worstTime, time);
    }
    averageTime = totalTime / count;
    
    // Prepare output file for steps
    std::string stepsOutput = "binary_search_step_" + std::to_string(count) + "_" + std::to_string(target) + ".txt";
    std::vector<std::string> stepLogs;
    
    int low = 0, high = count - 1, foundIndex = -1;
    auto startTime = std::chrono::high_resolution_clock::now();
    while (low <= high) {
        int mid = low + (high - low) / 2;
        // Print the step as index: number/text
        stepLogs.push_back(std::to_string(mid) + ": " + std::to_string(numData[mid]) + "/" + textData[mid]);
        if (numData[mid] == target) {
            foundIndex = mid;
            break;
        } else if (numData[mid] < target) {
            low = mid + 1;
        } else {
            high = mid - 1;
        }
    }
    if (foundIndex == -1) {
        stepLogs.push_back("-1");
    }
    auto endTime = std::chrono::high_resolution_clock::now();
    double searchTime = std::chrono::duration<double>(endTime - startTime).count();
    
    // Save steps to file
    std::ofstream writer(stepsOutput);
    for (const std::string& log : stepLogs) {
        writer << log << "\n";
    }
    writer.close();
    
    std::cout << "\n--------------------------------" << std::endl;
    std::cout << "Binary search steps saved to " << stepsOutput << std::endl;
    
    // Print result
    if (foundIndex != -1) {
        std::cout << "Target found at index: " << foundIndex << std::endl;
    } else {
        std::cout << "Target not found" << std::endl;
    }
    std::cout << "Search time: " << std::fixed << searchTime << " seconds" << std::endl;
    
    // Display time complexity results
    std::cout << "\nTime Complexity Analysis:" << std::endl;
    std::cout << "Best case time: " << std::fixed << bestTime << " seconds" << std::endl;
    std::cout << "Average case time: " << std::fixed << averageTime << " seconds" << std::endl;
    std::cout << "Worst case time: " << std::fixed << worstTime << " seconds" << std::endl;
    std::cout << "--------------------------------" << std::endl;
    
    return 0;
} 