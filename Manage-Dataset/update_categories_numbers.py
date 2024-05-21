import json


def create_category_id_mapping(categories):
    """
    Create a mapping from original category IDs to a new range starting from 0.

    Args:
    - categories (list): List of category dictionaries from the annotations.
    - annotations (list): List of annotation dictionaries from the annotations.

    Returns:
    - id_mapping (dict): Dictionary mapping original IDs to new IDs starting from 0.
    """

    def clean_id(id_value):
        if isinstance(id_value, str):
            id_value = id_value.strip('"')
        return int(id_value)

    original_ids = {clean_id(category['id']) for category in categories}

    id_mapping = {orig_id: new_id for new_id, orig_id in enumerate(sorted(original_ids))}
    return id_mapping


def create_annotation_id_mapping(annotations):
    """
    Create a mapping from original annotation IDs to a new range starting from 0.

    Args:
    - annotations (list): List of annotation dictionaries from the annotations.

    Returns:
    - id_mapping (dict): Dictionary mapping original IDs to new IDs starting from 0.
    """
    original_ids = [annotation['id'] for annotation in annotations]
    id_mapping = {orig_id: new_id for new_id, orig_id in enumerate(sorted(original_ids))}
    return id_mapping


def update_category_and_annotation_ids(annotations_path, output_path, category_id_mapping, annotation_id_mapping):
    """
    Update category and annotation IDs in the annotations file and save to a new file.

    Args:
    - annotations_path (str): Path to the original annotations file.
    - output_path (str): Path to save the updated annotations file.
    - category_id_mapping (dict): Dictionary mapping original category IDs to new IDs.
    - annotation_id_mapping (dict): Dictionary mapping original annotation IDs to new IDs.
    """
    with open(annotations_path, 'r') as f:
        data = json.load(f)

    categories = data['categories']
    annotations = data['annotations']

    def clean_id(id_value):
        if isinstance(id_value, str):
            id_value = id_value.strip('"')
        return int(id_value)

    # Update categories with new IDs
    for category in categories:
        original_id = str(clean_id(category['id']))
        if original_id in category_id_mapping:
            category['id'] = category_id_mapping[original_id]
        else:
            print(f"Warning: Category ID {original_id} not found in category_id_mapping")

    for annotation in annotations:
        original_category_id = str(clean_id(annotation['category_id']))
        if original_category_id in category_id_mapping:
            annotation['category_id'] = category_id_mapping[original_category_id]
        else:
            print(f"Warning: Annotation category_id {original_category_id} not found in category_id_mapping")

        if str(annotation['id']) in annotation_id_mapping:
            annotation['id'] = annotation_id_mapping[str(annotation['id'])]
        else:
            print(f"Warning: Annotation ID {annotation['id']} not found in annotation_id_mapping")
    # Save the updated annotations to a new file
    updated_data = {
        'categories': categories,
        'images': data.get('images', []),
        'annotations': annotations
    }

    with open(output_path, 'w') as f:
        json.dump(updated_data, f, indent=4)

    print(f"Updated annotations saved to {output_path}")


def save_mapping(mapping, mapping_path):
    """
    Save the mapping to a file.

    Args:
    - mapping (dict): Dictionary mapping original IDs to new IDs.
    - mapping_path (str): Path to save the mapping file.
    """
    with open(mapping_path, 'w') as f:
        json.dump(mapping, f, indent=4)
    print(f"Mapping saved to {mapping_path}")


def load_mapping(mapping_path):
    """
    Load the mapping from a file.

    Args:
    - mapping_path (str): Path to the mapping file.

    Returns:
    - mapping (dict): Loaded dictionary mapping original IDs to new IDs.
    """
    with open(mapping_path, 'r') as f:
        mapping = json.load(f)
    print(f"Mapping loaded from {mapping_path}")
    return mapping


def process(annotations_path, output_path, category_mapping_path, annotation_mapping_path):
    with open(annotations_path, 'r') as f:
        data = json.load(f)
        categories = data['categories']
        annotations = data['annotations']

    # category_id_mapping = create_category_id_mapping(categories)
    # annotation_id_mapping = create_annotation_id_mapping(annotations)
    # save_mapping(category_id_mapping, category_mapping_path)
    # save_mapping(annotation_id_mapping, annotation_mapping_path)

    category_id_mapping = load_mapping(category_mapping_path)
    annotation_id_mapping = load_mapping(annotation_mapping_path)

    update_category_and_annotation_ids(annotations_path, output_path, category_id_mapping, annotation_id_mapping)
