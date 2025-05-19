#include <iostream>
#include <fstream>
#include <unordered_set>
#include <random>
#include <string>
#include <vector>
#include <algorithm>
#include <sys/stat.h> // For mkdir on POSIX/Windows
#include <sys/types.h>
#include <io.h>

int main() {
    const int MAX_VALUE = 1000000000;
    const int DATASET_SIZE = 1000000;
    const int MIN_STRING_LENGTH = 4;
    const int MAX_STRING_LENGTH = 6;
    const std::string CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    const std::string DIR_NAME = "dataset";
    const std::string FILE_NAME = "dataset_cpp.csv";
    
    // Create dataset directory if it doesn't exist
    #if defined(_WIN32)
        _mkdir(DIR_NAME.c_str());
    #else
        mkdir(DIR_NAME.c_str(), 0777);
    #endif
    std::unordered_set<int> unique_numbers;
    std::vector<std::pair<int, std::string>> dataset;
    dataset.reserve(DATASET_SIZE);

    std::random_device rd;
    std::mt19937 gen(rd());
    std::uniform_int_distribution<> num_dist(1, MAX_VALUE);
    std::uniform_int_distribution<> len_dist(MIN_STRING_LENGTH, MAX_STRING_LENGTH);
    std::uniform_int_distribution<> char_dist(0, CHARACTERS.size() - 1);

    while (dataset.size() < DATASET_SIZE) {
        int random_number = num_dist(gen);
        if (unique_numbers.find(random_number) != unique_numbers.end()) continue;
        unique_numbers.insert(random_number);

        int string_length = len_dist(gen);
        std::string random_string;
        random_string.reserve(string_length);
        for (int i = 0; i < string_length; ++i) {
            random_string += CHARACTERS[char_dist(gen)];
        }

        dataset.emplace_back(random_number, random_string);
    }

    // Shuffle dataset
    std::shuffle(dataset.begin(), dataset.end(), gen);

    // Write to CSV
    std::ofstream file(DIR_NAME + "/" + FILE_NAME);
    if (!file) {
        std::cerr << "Failed to open file for writing.\n";
        return 1;
    }
    for (const auto& pair : dataset) {
        file << pair.first << ',' << pair.second << '\n';
    }
    file.close();

    std::cout << "Dataset generated and written to " << DIR_NAME << "/" << FILE_NAME << std::endl;
    return 0;
}