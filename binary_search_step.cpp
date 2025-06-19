#include <iostream>
#include <fstream>
#include <string>
#include <sstream>

using namespace std;

const int MAX_SIZE = 1000; // adjust based on dataset size

int main() {
    string filename;
    int target;

    // Get user input
    cout << "Enter dataset filename: ";
    getline(cin, filename);

    cout << "Enter target integer: ";
    cin >> target;

    // Open CSV file
    ifstream infile(filename);
    if (!infile) {
        cerr << "Error: Cannot open file " << filename << "\n";
        return 1;
    }

    int nums[MAX_SIZE];
    string strs[MAX_SIZE];
    int size = 0;
    string line;

    // Read from CSV into arrays
    while (getline(infile, line) && size < MAX_SIZE) {
        if (!line.empty()) {
            stringstream ss(line);
            string numPart, strPart;
            getline(ss, numPart, ',');
            getline(ss, strPart);
            nums[size] = stoi(numPart);
            strs[size] = strPart;
            ++size;
        }
    }
    infile.close();

    // Output file setup
    string outFile = "binary_search_step_" + to_string(target) + ".txt";
    ofstream outfile(outFile);
    if (!outfile) {
        cerr << "Error: Cannot write to file " << outFile << "\n";
        return 1;
    }

    // Binary search
    int left = 0, right = size - 1;
    bool found = false;

    while (left <= right) {
        int mid = left + (right - left) / 2;
        outfile << (mid + 1) << ": " << nums[mid] << "/" << strs[mid] << "\n";

        if (nums[mid] == target) {
            found = true;
            break;
        } else if (nums[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }

    if (!found) {
        outfile << "-1\n";
    }

    cout << "Search complete. Output written to: " << outFile << "\n";
    return 0;
}
