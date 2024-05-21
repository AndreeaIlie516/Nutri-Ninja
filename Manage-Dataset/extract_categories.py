import csv
import json
from collections import defaultdict


def load_data(filepath):
    try:
        with open(filepath, 'r') as file:
            return json.load(file)
    except FileNotFoundError:
        print("File not found. Please check the file path.")
    except json.JSONDecodeError:
        print("Error decoding JSON. Please check the file content.")
    except Exception as e:
        print(f"An unexpected error occurred: {e}")


def clean_category_name(name):
    return name.strip()


def process_data(annotation_file, output_csv_file):
    data = load_data(annotation_file)
    if not data:
        return

    categories = []

    for category in data['categories']:
        categories.append(category['name'])

    print(categories)

    try:
        with open(output_csv_file, mode='w', newline='') as file:
            writer = csv.writer(file, quoting=csv.QUOTE_STRINGS)
            writer.writerow(categories)
        print("Category data has been written to the CSV file.")
    except IOError:
        print("Error writing to the file. Please check file path and permissions.")

