#include <iostream>      
#include <fstream>       
#include <string>        
using namespace std;

// Structure to hold int/string pair
struct Value {
    int number;
    string text;
};

// Function to print the contents of an array to output file
void printArray(Value arr[], int size, ofstream& out) {
    out << "[";
    for (int i = 0; i < size; ++i) {
        out << arr[i].number << "/" << arr[i].text;
        if (i != size - 1) out << ", ";
    }
    out << "]\n";
}

// Partition function for quicksort
int partition(Value arr[], int low, int high, ofstream& out) {
    Value pivot = arr[high]; // Pivot element is the last element
    int i = low - 1;

    for (int j = low; j < high; ++j) {
        if (arr[j].number < pivot.number) {
            ++i;
            swap(arr[i], arr[j]);
        }
    }

    swap(arr[i + 1], arr[high]); // Place pivot in the correct position
    out << "pi=" << (i + 1) << " ";
    printArray(arr, high + 1, out);  

    return i + 1; // Return pivot index
}

// Recursive quicksort algorithm
void quickSort(Value arr[], int low, int high, ofstream& out) {
    if (low < high) {
        int pi = partition(arr, low, high, out); // Partition the array
        quickSort(arr, low, pi - 1, out);        // Recursively sort left partition
        quickSort(arr, pi + 1, high, out);       // Recursively sort right partition
    }
}

int main() {
    string filename = "dataset_sample_1000.csv"; // Dataset filename is fixed
    int startRow, endRow;

    // Ask user for start and end row
    cout << "Enter start row: ";
    cin >> startRow;
    cout << "Enter end row: ";
    cin >> endRow;

    // Calculate number of elements to sort
    int size = endRow - startRow + 1;
    if (size <= 0) {
        cerr << "Invalid row range.\n";
        return 1;
    }

    Value* values = new Value[size];
    ifstream infile(filename);
    if (!infile.is_open()) {
        cerr << "Error opening file.\n";
        delete[] values;
        return 1;
    }

    string line;
    int row = 1, index = 0;

    // Read CSV file line by line
    while (getline(infile, line)) {
        if (row >= startRow && row <= endRow) {
            size_t commaPos = line.find(',');
            if (commaPos != string::npos) {
                string numStr = line.substr(0, commaPos);
                string txtStr = line.substr(commaPos + 1);
                values[index].number = stoi(numStr);
                values[index].text = txtStr;
                ++index;
            }
        }
        ++row;
    }
    infile.close();

    // Open output file
    string outFile = "quick_sort_step_" + to_string(startRow) + "_" + to_string(endRow) + ".txt";
    ofstream out(outFile);
    if (!out.is_open()) {
        cerr << "Error writing to file.\n";
        delete[] values;
        return 1;
    }

    printArray(values, size, out); // Initial array print

    quickSort(values, 0, size - 1, out); // Perform quicksort on the extracted subarray

    delete[] values; 
    cout << "Sorting complete. Output written to " << outFile << "\n";
    return 0;
}
