#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <sstream>
#include <chrono>
#include <iomanip>  // for std::setprecision

// Structure to hold each row: number and its corresponding text
struct Data {
    int number;
    std::string text;
};

// Function to parse a line from the CSV file
Data parseLine(const std::string& line) {
    Data data;
    size_t comma = line.find(',');
    data.number = std::stoi(line.substr(0, comma));
    data.text = line.substr(comma + 1);
    return data;
}

// Partition function used by QuickSort
int partition(std::vector<Data>& arr, int low, int high, std::ofstream* stepFile) {
    int pivot = arr[high].number;  // Use last element as pivot
    int i = low - 1;

    for (int j = low; j < high; ++j) {
        if (arr[j].number <= pivot) {
            ++i;
            std::swap(arr[i], arr[j]);
        }
    }

    std::swap(arr[i + 1], arr[high]);  // Place pivot in correct position

    return i + 1;
}

// Recursive QuickSort implementation
void quickSort(std::vector<Data>& arr, int low, int high, std::ofstream* stepFile) {
    if (low < high) {
        int pi = partition(arr, low, high, stepFile);  // Partition and get pivot index
        quickSort(arr, low, pi - 1, stepFile);          // Sort left subarray
        quickSort(arr, pi + 1, high, stepFile);         // Sort right subarray
    }
}

int main() {
    std::vector<Data> data;
    std::string line;

    // Read data from CSV file
    std::ifstream inFile("dataset.csv");
    if (!inFile) {
        std::cerr << "Error: Cannot open dataset.csv\n";
        return 1;
    }

    while (std::getline(inFile, line)) {
        if (!line.empty()) {
            data.push_back(parseLine(line));
        }
    }
    inFile.close();

    // Get row range from user
    int startRow, endRow;
    std::cout << "Enter start row: ";
    std::cin >> startRow;
    std::cout << "Enter end row: ";
    std::cin >> endRow;

    // Validate row input
    if (startRow < 1 || endRow > static_cast<int>(data.size()) || startRow > endRow) {
        std::cerr << "Invalid row range\n";
        return 1;
    }

    // Adjust to 0-based index
    startRow--;
    endRow--;
    int n = endRow - startRow + 1;

    // Copy the selected data range for sorting
    std::vector<Data> toSort(data.begin() + startRow, data.begin() + endRow + 1);

    // Measure sorting time in seconds
    auto startTime = std::chrono::high_resolution_clock::now();
    quickSort(toSort, 0, n - 1, nullptr);  // No logging here
    auto endTime = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double> duration = endTime - startTime;

    // Save sorted results to CSV file
    std::ofstream outFile("quick_sort_" + std::to_string(n) + ".csv");
    if (!outFile) {
        std::cerr << "Error writing to output CSV\n";
        return 1;
    }
    for (const auto& d : toSort) {
        outFile << d.number << "," << d.text << "\n";
    }
    outFile.close();

    // Print final result in seconds (6 decimal places)
    std::cout << std::fixed << std::setprecision(6);
    std::cout << n << " rows sorted. Running time = " << duration.count() << " seconds\n";

    return 0;
}

