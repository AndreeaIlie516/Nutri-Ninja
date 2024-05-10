import csv
import json
import os
import random
import shutil
from collections import defaultdict


def load_json(file_path):
    """ Load JSON data from a file. """
    with open(file_path, 'r') as file:
        return json.load(file)


def save_json(data, file_path):
    """ Save JSON data to a file. """
    with open(file_path, 'w') as file:
        json.dump(data, file, indent=4)


def clean_category_name(name):
    return name.strip()


def category_counts(category_file, image_file, annotation_file):
    categories = load_json(category_file)
    images = load_json(image_file)
    annotations = load_json(annotation_file)
    categories = {category['id']: clean_category_name(category['name_readable']) for category in
                  categories['categories']}

    category_images = defaultdict(list)
    for annotation in annotations['annotations']:
        image_id = annotation['image_id']
        category_id = annotation['category_id']
        category_images[category_id].append(image_id)

    category_image_count = {categories[cat_id]: len(set(images)) for cat_id, images in category_images.items()}

    sorted_category_image_count = dict(sorted(category_image_count.items(), key=lambda item: item[1], reverse=True))

    return sorted_category_image_count


def calculate_allocations(counts, subset_percentage):
    """ Calculate the number of images to select based on a subset percentage. """
    allocations = {cat: int(count * subset_percentage) for cat, count in counts.items()}
    return allocations


def preprocess_annotations(annotations, images):
    print("processing annotations..")
    category_to_images = {}

    for ann in annotations['annotations']:
        if ann['category_id'] not in category_to_images:
            category_to_images[ann['category_id']] = []
        category_to_images[ann['category_id']].append(ann['image_id'])

    image_id_to_file = {img['id']: img for img in images['images']['images']}
    print("Categories to images: ", category_to_images)
    print("Image id to file:", image_id_to_file)
    return category_to_images, image_id_to_file


def select_and_copy_images(categories, images, annotations, allocations, source_img_dir, subset_img_dir):
    category_to_images, image_id_to_file = preprocess_annotations(annotations, images)
    subset_images = []
    subset_annotations = []
    subset_image_ids = set()

    category_name_to_id = {cat['name_readable']: cat['id'] for cat in categories['categories']}

    for category_name, allocation in allocations.items():
        category_id = category_name_to_id.get(category_name)
        if category_id and category_id in category_to_images:
            image_ids = category_to_images[category_id]
            selected_ids = random.sample(image_ids, min(len(image_ids), allocation))
            for image_id in selected_ids:
                img = image_id_to_file[image_id]
                subset_images.append(img)
                subset_image_ids.add(img['id'])
                src = os.path.join(source_img_dir, img['file_name'])
                dst = os.path.join(subset_img_dir, img['file_name'])
                if os.path.exists(src):
                    shutil.copy(src, dst)

    for ann in annotations['annotations']:
        if ann['image_id'] in subset_image_ids:
            subset_annotations.append(ann)

    return subset_images, subset_annotations


def process_data(category_file, image_file, annotation_file, subset_percentage, source_img_dir, output_directory):
    categories = load_json(category_file)
    images = load_json(image_file)
    annotations = load_json(annotation_file)

    counts = category_counts(category_file, image_file, annotation_file)

    allocations = calculate_allocations(counts, subset_percentage)

    subset_images, subset_annotations = select_and_copy_images(categories, images, annotations, allocations, source_img_dir, output_directory+'/images')

    combined_data = {
        "categories": categories['categories'],
        "images": subset_images,
        "annotations": subset_annotations
    }
    save_json(combined_data, output_directory + '/annotations.json')
