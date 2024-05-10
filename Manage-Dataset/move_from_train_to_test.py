import json
import random
import csv
import os
import shutil
from concurrent.futures import ThreadPoolExecutor
from shutil import move


def load_json(file_path):
    with open(file_path, 'r') as file:
        return json.load(file)


def save_json(data, file_path):
    with open(file_path, 'w') as file:
        json.dump(data, file, indent=4)


def load_allocations(csv_path):
    allocations = {}
    with open(csv_path, mode='r', newline='') as file:
        reader = csv.reader(file)
        for row in reader:
            category_name_readable, count = row[0].strip('"'), int(row[1])
            allocations[category_name_readable] = count
    return allocations


def move_files(file_operations):
    """Move files using multiple threads."""
    with ThreadPoolExecutor(max_workers=10) as executor:
        futures = [executor.submit(shutil.move, src, dst) for src, dst in file_operations]
        for future in futures:
            future.result()


def preprocess_annotations(annotations, images):
    print("processing annotations..")
    category_to_images = {}

    for ann in annotations['annotations']:
        if ann['category_id'] not in category_to_images:
            category_to_images[ann['category_id']] = []
        category_to_images[ann['category_id']].append(ann['image_id'])

    image_id_to_file = {img['id']: img for img in images['images']}
    print("Categories to images: ", category_to_images)
    print("Image id to file:", image_id_to_file)
    return category_to_images, image_id_to_file


def select_and_move_images(categories, images, annotations, allocations, train_img_dir, test_img_dir):
    category_to_images, image_id_to_file = preprocess_annotations(annotations, images)
    test_images = []
    test_annotations = []
    train_annotations = []
    test_image_ids = set()

    category_name_to_id = {cat['name_readable']: cat['id'] for cat in categories['categories']}

    for category_name, allocation in allocations.items():
        category_id = category_name_to_id.get(category_name)
        if category_id and category_id in category_to_images:
            image_ids = category_to_images[category_id]
            selected_ids = random.sample(image_ids, min(len(image_ids), allocation))
            for image_id in selected_ids:
                img = image_id_to_file[image_id]
                test_images.append(img)
                test_image_ids.add(img['id'])
                src = os.path.join(train_img_dir, img['file_name'])
                dst = os.path.join(test_img_dir, img['file_name'])
                if os.path.exists(src):
                    shutil.move(src, dst)

    for ann in annotations['annotations']:
        if ann['image_id'] in test_image_ids:
            test_annotations.append(ann)
        else:
            train_annotations.append(ann)

    return (images, train_annotations), (test_images, test_annotations)


def process_datasets(category_file, image_file, annotation_file, allocation_csv, train_img_dir, test_img_dir,
                     output_train_images, output_train_annotations, output_test_images, output_test_annotations):
    categories = load_json(category_file)
    images = load_json(image_file)
    annotations = load_json(annotation_file)
    allocations = load_allocations(allocation_csv)

    train_data, test_data = select_and_move_images(categories, images, annotations, allocations, train_img_dir,
                                                   test_img_dir)

    save_json({'images': train_data[0]}, output_train_images)
    save_json({'annotations': train_data[1]}, output_train_annotations)
    save_json({'images': test_data[0]}, output_test_images)
    save_json({'annotations': test_data[1]}, output_test_annotations)
