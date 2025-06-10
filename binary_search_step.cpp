#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <sstream>

int main() {
    std::string filename;
    int target;

    // Get user input
    std::cout << "Enter dataset filename: ";
    std::getline(std::cin, filename);

    std::cout << "Enter target integer: ";
    std::cin >> target;

    std::vector<int> data;

    // Read CSV file
    std::ifstream infile(filename);
    if (!infile) {
        std::cerr << "Error: Cannot open file " << filename << "\n";
        return 1;
    }

    std::string line;
    while (std::getline(infile, line)) {
        if (!line.empty()) {
            std::stringstream ss(line);
            std::string num_str;
            std::getline(ss, num_str, ','); // Get the integer part
            int number = std::stoi(num_str);
            data.push_back(number);
        }
    }

    // Output txt file
    std::string out_filename = "binary_search_step_" + std::to_string(target) + ".txt";
    std::ofstream outfile(out_filename);

    if (!outfile) {
        std::cerr << "Error: Cannot write to file " << out_filename << "\n";
        return 1;
    }

    // Binary Search Algo
    int left = 0, right = data.size() - 1;
    bool found = false;

    while (left <= right) {
        int mid = left + (right - left) / 2;
        outfile << "Compared value: " << data[mid] << " at row: " << (mid + 1) << "\n";

        if (data[mid] == target) {
            outfile << "Target found at row: " << (mid + 1) << "\n";
            found = true;
            break;
        } else if (data[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }

    if (!found) {
        outfile << "Target not found\n";
    }

    std::cout << "Search complete. Output written to: " << out_filename << "\n";
    return 0;
}