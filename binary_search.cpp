#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include <ctime>
#include <chrono>
#include <iomanip>
#include <cmath>
using namespace std;

const int max_index = 1000000000;

//binary search algorithm
bool binarySearch(int data[], int dataSize, int target){
    int left = 0, right = dataSize - 1;
    
    while(left <= right){
        int mid = (left + right)/2;
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

int main(){
    string filename;

    cout << "Enter data filename: ";
    getline(cin, filename);

    ifstream infile(filename);

    if(!infile.is_open()){
        cout << "Error! Could not open file" << endl;
        return 1;
    }

    string line;

    int* numData = new int[max_index];
    int count = 0;

    while (getline(infile, line)) {
        stringstream ss(line);
        string numStr, text;

        if (getline(ss, numStr, ',') && getline(ss, text)) {
            int num = stoi(numStr);

            numData[count] = num;
            count++;
        }
    }

    string outFilename = "binary_search_"+ to_string(count) + ".txt";
    ofstream outfile(outFilename);

    //run best case, average case and worst case
    double bestTime = numeric_limits<long>::max();
    double averageTime = 0.0;
    double worstTime = 0.0;

    double time = 0.0;
    double totalTime = 0.0;
    for(int i=0; i<count; i++){      
        auto start = chrono::high_resolution_clock::now();
        binarySearch(numData, count, numData[i]);
        auto end = chrono::high_resolution_clock::now();

        auto elapsed = chrono::duration_cast<chrono::nanoseconds>(end - start) / 1e9;
        time = elapsed.count();
        totalTime += time;

        bestTime = min(bestTime, time);
        worstTime = max(worstTime, time);
    }
    averageTime = totalTime / count;

    //output
    outfile << fixed << setprecision(12);

    cout << "Test complete. Please check the result on " << outFilename << endl;

    outfile << "Best case time elapsed : " << bestTime << " seconds" << endl; 
    outfile << "Average case time elapsed : " << averageTime << " seconds" << endl; 
    outfile << "Worst case time elapsed : " << worstTime << " seconds" << endl;

    infile.close();
    outfile.close();

    delete[] numData;
    return 0;
}