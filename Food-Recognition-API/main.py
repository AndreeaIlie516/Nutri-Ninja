import torch
from flask import Flask, request, jsonify
from torchvision import transforms
from PIL import Image
import io
import numpy as np
import cv2
from detectron2.config import get_cfg
from detectron2.engine import DefaultPredictor
from detectron2 import model_zoo

app = Flask(__name__)


MODEL_PATH = 'model/model_final.pth'
MODEL_ARCH = "COCO-InstanceSegmentation/mask_rcnn_R_50_FPN_3x.yaml"

checkpoint = torch.load(MODEL_PATH, map_location=torch.device('cpu'))

print(checkpoint.keys())

def load_model():
    cfg = get_cfg()
    try:
        cfg.merge_from_file(model_zoo.get_config_file(MODEL_ARCH))
    except RuntimeError as e:
        print(f"Error loading model config: {e}")
        raise

    cfg.MODEL.WEIGHTS = MODEL_PATH
    cfg.MODEL.ROI_HEADS.SCORE_THRESH_TEST = 0.2
    cfg.MODEL.ROI_HEADS.NUM_CLASSES = 498
    cfg.MODEL.DEVICE = 'cpu'

    return DefaultPredictor(cfg)


predictor = load_model()

def preprocess_image(image):
    transform = transforms.Compose([
        transforms.Resize((224, 224)),
        transforms.ToTensor(),
        transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225]),
    ])
    return transform(image).unsqueeze(0)


def get_segmentation_coordinates(predictions, threshold=0.5):
    instances = predictions['instances'].to('cpu')
    print("instances: ", instances)
    labels = instances.pred_classes.numpy()
    print("labels: ", labels)
    masks = instances.pred_masks.numpy()
    coordinates = []
    for i, mask in enumerate(masks):
        contours, _ = cv2.findContours(mask.astype(np.uint8), cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
        coords = [contour.flatten().tolist() for contour in contours]
        coordinates.append({'label': int(labels[i]), 'coords': coords})
    return coordinates


@app.route('/predict', methods=['POST'])
def predict():
    if 'file' not in request.files:
        return jsonify({'success': False, 'message': 'Invalid file'}), 400

    image_file = request.files['file']

    try:
        image_bytes = image_file.read()
        pil_image = Image.open(io.BytesIO(image_bytes)).convert('RGB')
        image = np.array(pil_image)
        image = cv2.cvtColor(image, cv2.COLOR_RGB2BGR)

        outputs = predictor(image)
        print("outputs: ", outputs)

        segmentation_coordinates = get_segmentation_coordinates(outputs)
        print(segmentation_coordinates)
        results = []
        for coord in segmentation_coordinates:
            print("coord: ", coord)
            results.append({
                'class': 'Baby spinach',
                'coordinates': coord['coords'][0],
                'precision': 56.6
            })

        print(results)
        return jsonify({'success': True, 'results': results}), 200
    except Exception as e:
        return jsonify({'success': False, 'message': str(e)}), 500


if __name__ == '__main__':
    app.run(host='127.0.0.1', port=6000, debug=True)