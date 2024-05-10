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

    categories = {category['id']: clean_category_name(category['name_readable']) for category in data['categories']}

    category_images = defaultdict(list)
    for annotation in data['annotations']:
        image_id = annotation['image_id']
        category_id = annotation['category_id']
        category_images[category_id].append(image_id)

    category_image_count = {categories[cat_id]: len(set(images)) for cat_id, images in category_images.items()}

    sorted_category_image_count = dict(sorted(category_image_count.items(), key=lambda item: item[1], reverse=True))

    try:
        with open(output_csv_file, mode='w', newline='') as file:
            writer = csv.writer(file, quoting=csv.QUOTE_STRINGS)
            for category, count in sorted_category_image_count.items():
                writer.writerow([category, count])
        print("Image per category data has been written to the CSV file.")
    except IOError:
        print("Error writing to the file. Please check file path and permissions.")

