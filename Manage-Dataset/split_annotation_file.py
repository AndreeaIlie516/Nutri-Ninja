import json


def load_annotations(file_path):
    """Load the full annotations from the original JSON file."""
    with open(file_path, 'r') as file:
        data = json.load(file)
    return data


def save_data(data, file_path):
    """Save data to a JSON file."""
    with open(file_path, 'w') as file:
        json.dump(data, file, indent=4)


def split_annotation_file(annotation_file, annotation_folder):
    """Split the full annotation file into separate categories, images, and annotations files."""
    data = load_annotations(annotation_file)

    categories = {
        "categories": data["categories"]
    }
    images = {
        "images": data["images"]
    }
    annotations = {
        "annotations": data["annotations"]
    }

    save_data(categories, annotation_folder + '/' + 'categories.json')
    save_data(images,  annotation_folder + '/' + 'images.json')
    save_data(annotations,  annotation_folder + '/' + 'annotations.json')
    print("Files for", annotation_folder, "have been successfully split and saved.")
