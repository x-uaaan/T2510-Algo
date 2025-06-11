#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <string>

// Using vector instead of array because it is a flexible container that adjust dynamically 
// Helper function to print the vector to the output file in readable format
void printVector(const std::vector<int>& arr, std::ofstream& out) {
    out << "[";
    for (size_t i = 0; i < arr.size(); i++) {
        out << arr[i];
        if (i != arr.size() - 1) out << ", ";
    }
    out << "]\n";
}

// The function to help quicksort happen
int partition(std::vector<int>& arr, int low, int high, std::ofstream& out) {
    int pivot = arr[high];
    out << "Pivot chosen: " << pivot << "\n";
    int i = low - 1;

    for (int j = low; j < high; j++) {
        if (arr[j] < pivot) {
            i++;
            std::swap(arr[i], arr[j]);
            out << "Swapped " << arr[j] << " with " << arr[i] << "\n";
            out << "Current array: ";
            printVector(arr, out);
        }
    }

    std::swap(arr[i + 1], arr[high]);
    out << "Swapped pivot " << pivot << " to index " << (i + 1) << "\n";
    out << "Current array: ";
    printVector(arr, out);
    return i + 1;
}

// The quicksort algorithm
void quickSort(std::vector<int>& arr, int low, int high, std::ofstream& out) {
    if (low < high) {
        int pi = partition(arr, low, high, out);
        quickSort(arr, low, pi - 1, out);
        quickSort(arr, pi + 1, high, out);
    }
}

int main() {
    // Opening the dataset and user input
    std::string filename = "dataset_sample_1000.csv";
    int startRow, endRow;

    std::cout << "Enter start row: ";
    std::cin >> startRow;

    std::cout << "Enter end row: ";
    std::cin >> endRow;

    std::ifstream infile(filename);
    if (!infile.is_open()) {
        std::cerr << "Error opening file.\n";
        return 1;
    }

    std::vector<int> values;
    std::string line;
    int row = 1;

    // Reading the dataset and extracting the row the user specified
    while (std::getline(infile, line)) {
        if (row >= startRow && row <= endRow) {
            std::stringstream ss(line);
            std::string num;
            std::getline(ss, num, ',');
            values.push_back(std::stoi(num));
        }
        row++;
    }

    infile.close();

    // Creating the output file
    std::string outFile = "quick_sort_step_" + std::to_string(startRow) + "_" + std::to_string(endRow) + ".txt";
    std::ofstream out(outFile);
    if (!out.is_open()) {
        std::cerr << "Error writing to file.\n";
        return 1;
    }

    // Output initial unsorted array
    out << "Initial subarray: ";
    printVector(values, out);

    // Running the quicksort and logging it
    quickSort(values, 0, values.size() - 1, out);

    // Output sorted array
    out << "Sorted subarray: ";
    printVector(values, out);

    std::cout << "Sorting complete. Output written to " << outFile << "\n";
    return 0;
}
