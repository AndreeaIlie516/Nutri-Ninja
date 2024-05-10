import math
import csv


def load_category_counts(file_path):
    category_image_count = {}
    try:
        with open(file_path, mode='r', newline='') as file:
            reader = csv.reader(file)
            for row in reader:
                category, count = row[0].strip('"'), int(row[1])
                category_image_count[category] = count
        print("Category counts loaded successfully.")
    except Exception as e:
        print(f"Failed to load category counts: {e}")
    return category_image_count


def compute_test_allocations(input_csv_file, output_csv_file, desired_test_set_proportion, minimum_images_per_category, max_images_per_category):
    category_image_count = load_category_counts(input_csv_file)
    total_images = sum(category_image_count.values())
    test_allocations = {
        category: min(max(int(count * desired_test_set_proportion), minimum_images_per_category), max_images_per_category)
        for category, count in category_image_count.items()
    }

    total_allocated = sum(test_allocations.values())
    if total_allocated > total_images * desired_test_set_proportion:
        over_allocation = total_allocated - int(total_images * desired_test_set_proportion)
        for category in sorted(test_allocations, key=test_allocations.get, reverse=True):
            while test_allocations[category] > minimum_images_per_category and over_allocation > 0:
                test_allocations[category] -= 1
                over_allocation -= 1
                if over_allocation <= 0:
                    break

    total = 0
    try:
        with open(output_csv_file, mode='w', newline='') as file:
            writer = csv.writer(file, quoting=csv.QUOTE_STRINGS)
            for category, allocation in test_allocations.items():
                total += int(allocation)
                writer.writerow([category, allocation])
        print("Test allocations (", total, ") written to CSV file successfully.")
    except Exception as e:
        print(f"Error writing to the CSV file: {e}")
