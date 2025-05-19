# Sorting Algorithms Implementation

This project implements various sorting algorithms (Quick Sort and Merge Sort) in both Java and Python, along with dataset generators for testing. The implementations are done from scratch without using any external libraries, focusing purely on algorithmic concepts.

## Project Structure

```
├── java/
│   ├── DatasetGenerator.java    # Dataset generator for Java
│   ├── QuickSort.java          # Quick Sort implementation
│   └── MergeSort.java          # Merge Sort implementation
├── python/
│   ├── dataset_generator.py    # Dataset generator for Python
│   ├── quick_sort.py          # Quick Sort implementation
│   └── merge_sort.py          # Merge Sort implementation
└── README.md
```

## Design Overview

### Dataset Generators
- Integer dataset generator
  - Generates random integer arrays
  - Configurable size and range
  - Supports both ascending and descending order generation
- String dataset generator
  - Generates random string arrays
  - Configurable length and character set
  - Supports both alphabetical and random string generation

### Sorting Algorithms

#### Quick Sort
- In-place sorting algorithm
- Uses divide-and-conquer strategy
- Performance characteristics:
  - Average time complexity: O(n log n)
  - Worst case: O(n²)
  - Space complexity: O(log n)

#### Merge Sort
- Stable sorting algorithm
- Uses divide-and-conquer strategy
- Performance characteristics:
  - Time complexity: O(n log n)
  - Space complexity: O(n)
  - Guaranteed performance regardless of input

## Implementation Approach

### Java Implementation
- Pure Java implementation without external libraries
- Object-oriented design
- Generic type support for both integer and string sorting
- Modular and reusable components

### Python Implementation
- Pure Python implementation without external libraries
- Functional programming approach
- Clean and readable code structure
- Efficient memory management

## Requirements

### Java
- Java Development Kit (JDK) 8 or higher

### Python
- Python 3.6 or higher

## License
This project is open source and available under the MIT License.
