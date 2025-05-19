import os
import random
import csv

def generate_dataset(size, max_value, min_string_length, max_string_length):
    dataset = []
    characters = "abcdefghijklmnopqrstuvwxyz"
    unique_numbers = set()

    while len(dataset) < size:
        random_number = random.randint(1, max_value)  # Ensure positive numbers

        # Generate random string
        string_length = random.randint(min_string_length, max_string_length)
        random_string = ''.join(random.choice(characters) for _ in range(string_length))

        # Check if number is unique
        if random_number not in unique_numbers:
            unique_numbers.add(random_number)
            dataset.append((random_number, random_string))

    # Shuffle the dataset to ensure random order
    random.shuffle(dataset)
    return dataset

def write_dataset_to_file(dataset, file_name):
    # Create dataset directory if it doesn't exist
    os.makedirs("dataset", exist_ok=True)

    with open(f"dataset/{file_name}", mode="w", newline="") as file:
        writer = csv.writer(file)
        writer.writerows(dataset)

def main():
    # Define the maximum value for the integers
    MAX_VALUE = 1_000_000_000
    # Define the size of the dataset
    DATASET_SIZE = 1_000_000  # Testing size
    # Define string length range
    MIN_STRING_LENGTH = 4
    MAX_STRING_LENGTH = 6

    # Generate the dataset
    dataset = generate_dataset(DATASET_SIZE, MAX_VALUE, MIN_STRING_LENGTH, MAX_STRING_LENGTH)

    # Write the dataset to a file
    output_file_name = "dataset_py.csv"
    local_file_path = f"dataset/{output_file_name}"
    try:
        write_dataset_to_file(dataset, output_file_name)
        print(f"Dataset generated and written to {local_file_path}")
    except Exception as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    main()