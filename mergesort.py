import csv
import time

def merge_sort(arr, steps):
    # Base case: if the array has 1 or no elements, it's already sorted
    if len(arr) <= 1:
        return arr

    # Split: find the middle and divide the array into two halves
    mid = len(arr) // 2
    left = arr[:mid]
    right = arr[mid:]

    # Recursively sort both halves
    left_sorted = merge_sort(left, steps)
    right_sorted = merge_sort(right, steps)

    # Merge the sorted halves
    merged = merge(left_sorted, right_sorted)
    steps.append(merged)  # Log the current step
    return merged

def merge(left, right):
    merged = []
    i = j = 0

    # Compare elements from both lists and add the smaller one to the merged list
    while i < len(left) and j < len(right):
        if left[i][0] < right[j][0]:
            merged.append(left[i])
            i += 1
        else:
            merged.append(right[j])
            j += 1

    # Add any remaining elements from the left list
    while i < len(left):
        merged.append(left[i])
        i += 1

    # Add any remaining elements from the right list
    while j < len(right):
        merged.append(right[j])
        j += 1

    return merged

def read_csv(file_path):
    """Reads a CSV file and returns a list of tuples (integer, string). Skips header and invalid rows."""
    data = []
    with open(file_path, mode='r') as file:
        reader = csv.reader(file)
        for row in reader:
            try:
                # Skip rows that don't have at least 2 columns or can't convert first column to int
                data.append((int(row[0]), row[1]))
            except (ValueError, IndexError):
                continue  # Skip invalid rows (e.g., header)
    return data

def write_csv(file_path, data):
    """Writes a list of tuples (integer, string) to a CSV file, one per row."""
    with open(file_path, mode='w', newline='') as file:
        writer = csv.writer(file)
        for value in data:
            writer.writerow([value[0], value[1]])  # Write each tuple as a row

def write_steps(file_path, steps):
    """Writes the merge sort steps to a text file."""
    with open(file_path, mode='w') as file:
        for step in steps:
            file.write(f"{step}\n")

# Example usage
if __name__ == "__main__":
    # Read data from dataset.csv
    input_file = "dataset/dataset_java.csv"
    output_file = "mergesort/mergesort_py.csv"
    steps_file = "mergesort/mergesort_py_step.txt"
    data = read_csv(input_file)

    # Sort the data using merge sort
    steps = []
    start_time = time.time()  # Start timing the sorting process
    sorted_data = merge_sort(data, steps)
    end_time = time.time()  # End timing the sorting process

    # Calculate and print the sorting time
    sorting_time = end_time - start_time
    print(f"Sorting time (excluding reading and saving steps): {sorting_time:.6f} seconds")
    
    # Save the sorted data to mergesort_py.csv
    write_csv(output_file, sorted_data)
    print(f"Sorted data saved to {output_file}")
    
    # Save the merge sort steps to mergesort_py_step.txt
    write_steps(steps_file, steps)
    print(f"Merge sort steps saved to {steps_file}")