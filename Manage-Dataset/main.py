import compute_test_allocations
import extract_categories
import images_per_category
import move_from_train_to_test
import split_annotation_file
import select_data_subset
import update_categories_numbers

train_annotation_file = ('../../Machine-Learning/Dataset_2022_498_classes/raw_data/public_training_set_release_2.0'
                         '/original_annotations/annotations.json')
validation_annotation_file = ('../../Machine-Learning/Dataset_2022_498_classes/raw_data/public_validation_set_2.0'
                              '/original_annotations/annotations.json')
image_per_category_output_csv_file = './output/images_per_category_output.csv'
allocations_output_csv_file = './output/images_per_category_in_test_set_output.csv'
train_output = './output/train_annotations.json'
test_output = './output/test_annotations.json'
train_img_dir = '../../Machine-Learning/Dataset_2022_498_classes/raw_data/public_training_set_release_2.0/images'
test_img_dir = '../../Machine-Learning/Dataset_2022_498_classes/raw_data/public_test_release_2.0/new_images'
train_directory = '../../Machine-Learning/Dataset_2022_498_classes/raw_data/public_training_set_release_2.0'
test_directory = '../../Machine-Learning/Dataset_2022_498_classes/raw_data/public_test_release_2.0'
desired_test_set_proportion = 0.10
minimum_images_per_category = 1
max_images_per_category = 10
train_annotation_dir = "./train_annotations"
validation_annotation_dir = "./validation_annotations"
image_per_category_subset_csv_file = './output/images_per_category_subset.csv'
validation_directory = '../../Machine-Learning/Dataset_2022_498_classes/raw_data/public_validation_set_2.0'
subset_directory = '../../Machine-Learning/Dataset_2022_498_classes/raw_data/subsets'
categories_output_csv_file = './output/categories_output.csv'

if __name__ == "__main__":
    annotations_path = 'subset_train_annotations.json'
    output_path = './output/new_train_annotations.json'
    category_mapping_path = 'output/category_id_mapping.json'
    annotations_mapping_path = './output/annotations_id_mapping.json'
    update_categories_numbers.process(annotations_path, output_path, category_mapping_path, annotations_mapping_path)

    # extract_categories.process_data(
    #     annotation_file=train_annotation_file, output_csv_file=image_per_category_output_csv_file
    # )
    # images_per_category.process_data(
    #     annotation_file=train_annotation_file, output_csv_file=image_per_category_output_csv_file
    # )
    # compute_test_allocations.compute_test_allocations(
    #     input_csv_file=image_per_category_output_csv_file,
    #     output_csv_file=allocations_output_csv_file,
    #     desired_test_set_proportion=desired_test_set_proportion,
    #     minimum_images_per_category=minimum_images_per_category,
    #     max_images_per_category=max_images_per_category
    #     )
    # split_annotation_file.split_annotation_file(
    #     annotation_file=train_annotation_file,
    #     annotation_folder=train_annotation_dir
    # )
    # split_annotation_file.split_annotation_file(
    #     annotation_file=validation_annotation_file,
    #     annotation_folder=validation_annotation_dir
    # )
    # move_from_train_to_test.process_datasets(
    #     category_file=train_annotation_dir+'/categories.json',
    #     image_file=train_annotation_dir+'/images.json',
    #     annotation_file=train_annotation_dir+'/annotations.json',
    #     allocation_csv=allocations_output_csv_file,
    #     train_img_dir=train_img_dir,
    #     test_img_dir=test_img_dir,
    #     output_train_images=train_img_dir+'/images.json',
    #     output_train_annotations=train_img_dir+'/annotations.json',
    #     output_test_images=test_img_dir+'/images.json',
    #     output_test_annotations=test_img_dir+'/annotations.json'
    # )

    # select_data_subset.process_data(
    #     category_file=train_ann_dir + '/annotations/categories.json',
    #     image_file=train_ann_dir+'/annotations/images.json',
    #     annotation_file=train_ann_dir+'/annotations/annotations.json',
    #     output_csv_file=image_per_category_subset_csv_file
    # )

    # select_data_subset.process_data(
    #     category_file=train_directory+'/annotations/categories.json',
    #     image_file=train_directory+'/annotations/images.json',
    #     annotation_file=train_directory+'/annotations/annotations.json',
    #     subset_percentage=0.1,
    #     source_img_dir=train_directory+'/images/',
    #     output_directory=subset_directory+'/train_subset'
    # )

