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

category_ids = ['2', '92', '101', '24', '50', '28', '0', '10', '64', '19', '43', '41', '1', '45', '54', '4', '36', '93',
                '69', '187', '7', '13', '158', '231', '15', '65', '35', '55', '25', '284', '87', '220', '70', '89',
                '23', '21', '191', '128', '52', '58', '44', '159', '59', '123', '80', '5', '196', '242', '164', '76',
                '39', '279', '34', '88', '291', '189', '37', '118', '161', '51', '6', '163', '14', '331', '74', '113',
                '356', '224', '83', '358', '114', '109', '337', '157', '232', '68', '116', '32', '97', '86', '351', '3',
                '217', '244', '229', '216', '247', '334', '148', '271', '117', '422', '225', '27', '30', '29', '169',
                '313', '171', '71', '303', '149', '258', '100', '388', '20', '110', '91', '60', '135', '188', '257',
                '235', '142', '129', '207', '230', '166', '75', '228', '407', '38', '329', '8', '449', '53', '297',
                '322', '265', '255', '150', '416', '211', '270', '17', '11', '175', '40', '182', '375', '122', '186',
                '127', '214', '402', '162', '176', '298', '264', '151', '99', '143', '497', '179', '57', '178', '403',
                '261', '62', '194', '199', '233', '215', '206', '263', '238', '310', '219', '299', '42', '447', '124',
                '90', '253', '290', '262', '12', '84', '144', '85', '486', '384', '359', '324', '300', '9', '167',
                '366', '152', '126', '418', '202', '295', '31', '420', '209', '341', '316', '293', '398', '344', '428',
                '374', '357', '81', '342', '320', '327', '250', '223', '18', '268', '77', '47', '108', '476', '252',
                '153', '395', '440', '437', '452', '221', '56', '289', '155', '98', '315', '204', '195', '296', '493',
                '130', '311', '287', '174', '448', '94', '383', '401', '423', '266', '301', '431', '441', '280', '321',
                '241', '183', '141', '396', '343', '210', '389', '131', '234', '288', '292', '197', '392', '367', '16',
                '96', '147', '154', '104', '377', '73', '436', '458', '465', '340', '442', '22', '282', '459', '107',
                '414', '350', '67', '137', '335', '368', '406', '373', '381', '239', '138', '82', '485', '203', '277',
                '305', '427', '474', '63', '302', '222', '390', '269', '102', '464', '411', '249', '248', '240', '294',
                '212', '470', '467', '256', '496', '160', '120', '134', '481', '170', '349', '192', '438', '332', '274',
                '136', '185', '365', '479', '468', '218', '339', '243', '338', '227', '139', '145', '245', '434', '461',
                '198', '376', '205', '26', '463', '409', '354', '429', '425', '72', '455', '443', '95', '379', '125',
                '181', '352', '286', '391', '168', '236', '472', '346', '193', '267', '272', '307', '445', '451', '326',
                '387', '317', '424', '361', '323', '325', '421', '49', '184', '364', '132', '477', '281', '165', '439',
                '469', '121', '378', '273', '410', '487', '435', '112', '372', '363', '385', '400', '397', '462', '480',
                '254', '456', '471', '405', '314', '119', '426', '140', '453', '333', '380', '488', '466', '483', '475',
                '61', '491', '413', '79', '106', '408', '213', '146', '33', '371', '362', '382', '251', '246', '328',
                '399', '330', '46', '173', '355', '360', '226', '348', '473', '345', '494', '444', '66', '260', '404',
                '48', '115', '482', '495', '415', '417', '433', '478', '278', '490', '308', '201', '318', '180', '370',
                '111', '237', '276', '259', '304', '78', '484', '312', '386', '412', '306', '208', '275', '353', '457',
                '393', '133', '489', '369', '190', '105', '394', '446', '172', '419', '450', '454', '460', '492', '156',
                '430', '309', '319', '200', '103', '336', '347', '283', '285', '432', '177']
categories = ['Water', 'Salad, leaf / salad, green', 'Bread, white', 'Tomato, raw ', 'Butter', 'Carrot, raw',
              'Bread, wholemeal', 'Coffee, with caffeine', 'Rice', 'Egg', 'Apple', 'Mixed vegetables', 'Jam',
              'Cucumber', 'Wine, red', 'Banana', 'Cheese', 'Potatoes steamed', 'Bell pepper, red, raw ',
              'Espresso, with caffeine', 'Hard cheese', 'Tea', 'Bread, whole wheat',
              'Mixed salad (chopped without sauce)', 'Avocado', 'White coffee, with caffeine', 'Tomato sauce',
              'Wine, white', 'Broccoli', 'Strawberries', 'Pasta, spaghetti', 'Honey', 'Zucchini', 'Parmesan', 'Chicken',
              'Chips, french fries', 'Braided white loaf', 'Dark chocolate', 'Mayonnaise', 'Pizza, Margherita, baked',
              'Blueberries', 'Onion', 'Salami', 'Leaf spinach', 'Salmon', 'Soft cheese', 'Water, mineral', 'Gruyère',
              'Glucose drink 50g', 'Yaourt, yahourt, yogourt ou yoghourt, natural', 'Almonds', 'Mixed nuts', 'Pasta',
              'Black olives', 'Red radish', 'Juice, orange', 'Pear', 'French beans', 'Hummus', 'Herbal tea', 'Ham, raw',
              'Beer', 'Salmon, smoked', 'Tofu', 'Cucumber, pickled ', 'Mozzarella', 'Sweet potato', 'Dairy ice cream',
              'Orange', 'Croissant', 'Pasta, penne', 'Eggplant', 'Salad, rocket', 'Tea, green', 'Feta', 'Ham, cooked',
              'Mandarine', 'Sauce, cream', 'Bread, grain', 'Cake, chocolate', 'Milk', 'Bread, sourdough',
              'Bread, French (white flour)', 'Grapes', 'Dried meat', 'Biscuits', 'Cappuccino', 'Quinoa',
              'Balsamic salad dressing', 'Fennel', 'Kiwi', 'Raspberries', 'Tea, black',
              'Beetroot, steamed, without addition of salt', 'French salad dressing', 'Chickpeas', 'Cauliflower',
              'Corn', 'Green olives', 'Green asparagus', 'Semi-hard cheese', 'Pasta, twist',
              'Risotto, without cheese, cooked', 'Mushrooms', 'Sugar Melon', 'Bacon, frying', 'Bread',
              "Salad, lambs' ear", 'Mushroom', 'Crêpe, plain', 'Egg, scrambled, prepared', 'Fish', 'Walnut',
              'Hamburger (Bread, meat, ketchup)', 'Sauce (savoury)',
              'Mashed potatoes, prepared, with full fat milk, with butter', 'Chicken, breast', 'Ratatouille',
              'Curry, vegetarian', 'Salad dressing', 'Birchermüesli, prepared, no sugar added', 'Cashew nut',
              'Guacamole', 'Cottage cheese', 'Apricots', 'Soup, vegetable', 'Ketchup', 'Witloof chicory', 'Flakes, oat',
              'Müesli', 'Bolognaise sauce', 'Chocolate', 'Red cabbage', 'Baked potato', 'Ristretto, with caffeine',
              'Fruit salad', 'Pasta, wholemeal', 'Lentils', 'Bread, toast', 'Apple pie',
              'Caprese salad (Tomato Mozzarella)', 'Water with lemon juice', 'Tuna',
              'Chicken curry (cream/coconut milk. curry spices/paste))', 'Beef', 'Pizza, with vegetables, baked',
              'Sauce, pesto', 'Pasta, linguini, parpadelle, Tagliatelle', 'Sweet corn, canned', 'Leek',
              'Cheese for raclette', 'Soup, pumpkin', 'Nectarine', 'Sauce, roast', 'Sausage', 'Couscous', 'Lemon',
              'Crisps', 'Tart', 'Hazelnut-chocolate spread(Nutella, Ovomaltine, Caotina)', 'Bread, nut', 'Praline',
              'Crunch Müesli', 'Rice, Basmati', 'Pomegranate', 'Kolhrabi', 'Milk chocolate', 'Fresh cheese',
              'Chicken, cut into stripes (only meat)', 'Peanut butter', 'Peas',
              'Roll of half-white or white flour, with large void', 'Spaetzle', 'Mango', 'Bread, wholemeal toast',
              'Pork', 'Pancakes', 'Pasta, noodles', 'Sushi', 'Cream', 'Watermelon, fresh', 'Berries', 'Sour cream',
              'Goat cheese (soft)', 'Cookies', 'Bread, half white', 'Peanut', 'Croissant with chocolate filling',
              'Fajita (bread only)', 'Omelette, plain', 'Grits, polenta, maize flour', 'Peach', 'Mustard',
              'Pasta, Hörnli', 'Wine, rosé', 'Pumpkin', 'Plums', 'Pistachio', 'Beans, kidney',
              'Thickened cream (> 35%)', 'Pineapple', 'Gluten-free bread', 'Syrup (diluted, ready to drink)',
              'Chicken, leg', 'Salt cake (vegetables, filled)', 'Beef, minced (only meat)', 'Cream cheese',
              'Blue mould cheese', 'Bread, black', 'Brownie', 'Ham', 'Sesame seeds', 'Soup of lentils, Dahl (Dhal)',
              'Greek Yaourt, yahourt, yogourt ou yoghourt', 'Taboulé, prepared, with couscous',
              'Carrot, steamed, without addition of salt', 'Rosti', 'Potato-gnocchi', 'Celeriac', 'Rice, whole-grain',
              'Corn crisps', 'Lasagne, meat, prepared', 'Vegetable mix, peas and carrots',
              'Green bean, steamed, without addition of salt', 'Vegetables', 'Rice noodles/vermicelli',
              'Applesauce, unsweetened, canned', 'Dates', 'Beef, filet', 'Tomme', 'Country fries', 'White asparagus',
              'Dried raisins', 'Bread, fruit', 'Potatoes au gratin, dauphinois, prepared', 'Bread, pita', 'Tiramisu',
              'White cabbage', 'Falafel (balls)', 'Shrimp / prawn (large)', 'Roll with pieces of chocolate',
              'Greek salad', 'Sun-dried tomatoe', 'Bread, 5-grain', 'Veal sausage', 'Breadcrumbs (unspiced)',
              'Bread, rye', 'Bacon, cooking', 'Shrimp / prawn (small)', 'Chinese cabbage', 'Chocolate mousse',
              'Pizza, with ham, baked', 'Swiss chard', 'Mixed seeds', 'Ice tea', 'Tea, peppermint', 'Capers',
              'Fruit tart', 'Hazelnut', 'Oil & vinegar salad dressing', 'Pumpkin seeds', 'Spring onion / scallion',
              'Beetroot, raw', 'Sauce, soya', 'Beef, cut into stripes (only meat)',
              'Quiche, with cheese, baked, with puff pastry', 'Savoury puff pastry', 'Lye pretzel (soft)',
              'Ham croissant', 'Brioche', 'Butter, spread, puree almond', 'Fruit coulis', 'Cervelat', 'Juice, apple',
              'Sauce, mushroom', 'Vegetable au gratin, baked', 'Beef, sirloin steak', 'Frying sausage', 'Focaccia',
              'Sunflower seeds', 'Chili con carne, prepared', 'Kefir drink', 'Artichoke', 'Chocolate cookies',
              'Wienerli (Swiss sausage)', 'Meat terrine, paté', 'Alfa sprouts', 'Veggie burger', 'Bacon',
              'Sauce, curry', 'Figs', 'Bell pepper, red, stewed, without addition of fat, without addition of salt',
              'Porridge, prepared, with partially skimmed milk', 'Rice, wild', 'Pasta in butterfly form, farfalle',
              'Tea, verveine', 'Bread, Ticino', 'Parsley', 'Hamburger', 'Bouillon',
              'Curds, natural, with at most 10% fidm', 'Basil', 'Pine nuts', 'Crisp bread, Wasa',
              'Zucchini, stewed, without addition of fat, without addition of salt', 'Brussel sprouts', 'Sauerkraut',
              'Spinach, raw', 'Pasta in conch form', 'Rusk, wholemeal', 'Spinach, steamed, without addition of salt',
              'Cherries', 'Garlic', 'Spring roll (fried)', 'Smoothie', 'Coconut',
              'Quiche, with spinach, baked, with cake dough', 'Seeds', 'Emmental cheese',
              'Lentils green (du Puy, du Berry)', 'Cordon bleu, from pork schnitzel, fried', 'Croutons',
              'Chicken nuggets', 'Romanesco', 'Sauce, cocktail', 'Shoots', 'Beef, roast', 'Meatloaf',
              'Pizza, with ham, with mushrooms, baked', 'Bulgur', 'Bread, spelt', 'Fish fingers (breaded)',
              'Cream spinach', 'Chestnuts', 'Pork, escalope', 'Tzatziki', 'Processed meat, Charcuterie', 'Bacon, raw',
              'White bread with butter, eggs and milk', 'Mustard, Dijon', 'Shrimp, boiled', 'Sauce, sweet & sour',
              'Muffin', 'Curd', 'Pearl onions', 'Lemon Cake', 'Tartar sauce',
              'Dough (puff pastry, shortcrust, bread, pizza dough)', 'Sorbet', 'Halloumi', 'Tuna, in oil, drained',
              'Pasta, ravioli, stuffing', 'Crackers', 'Coleslaw (chopped without sauce)',
              'Pie, plum, baked, with cake dough', 'Hamburger bun', 'High protein pasta (made of lentils, peas, ...)',
              'Potato salad, with mayonnaise yogurt dressing', 'Sauce, carbonara', 'Cheddar', 'Croissant, wholegrain',
              'Cantonese fried rice', 'Meat', 'Juice, multifruit', 'Dips', 'Lamb', 'Risotto, with mushrooms, cooked',
              'Beans, white', 'Celery', 'Goat, (average), raw', 'Processed cheese', 'Apple crumble',
              'Grapefruit, pomelo', 'Coca Cola', 'Buckwheat, grain peeled', 'Panna cotta',
              'Pasta, tortelloni, stuffing', 'Kaki', 'Ebly', 'Fondue', 'Chicken, wing', 'Cocktail',
              'Vanille cream, cooked, Custard, Crème dessert', 'Pork, chop', 'Anchovies', 'Bread, olive',
              'Savoy cabbage, steamed, without addition of salt', 'Sauce, sweet-salted (asian)', 'Butter, herb',
              'Mungbean sprouts', 'Brie', 'Cheesecake', 'Latte macchiato, with caffeine', 'Lamb, chop',
              'Cenovis, yeast spread', 'Mousse', 'Bouquet garni', 'Sandwich (ham, cheese and butter)', 'Rice waffels',
              'Nuts', 'Chives', 'Tête de Moine', 'Tartar (meat)', 'Coconut milk', 'Cod', 'Coca Cola Zero',
              'Paprika chips', 'Lemon pie', 'Pork, roast', 'Pie, apricot, baked, with cake dough', 'Waffle',
              'Minced meat', '(bread, meat substitute, lettuce, sauce)', 'bean seeds', 'Chia grains',
              'Balsamic vinegar', 'Bouillon, vegetable', 'Fish crunchies (battered)', 'Naan (indien bread)',
              'Coffee, decaffeinated', 'Meat balls', 'Sweets / candies', 'Fruit compotes', 'Coriander',
              'Perch fillets (lake)', 'Chocolate egg, small', 'Biscuit with Butter', 'Meringue', 'Pecan nut',
              'Cocoa powder', 'Eggplant caviar', 'Savoury puff pastry stick', 'Ham, turkey', 'Carrot cake',
              'Rice, Jasmin', 'Fig, dried', 'Soup, tomato', 'Pie, rhubarb, baked, with cake dough',
              'Aperitif, with alcohol, apérol, Spritz', 'Linseeds', 'Cake (oblong)', 'Croque monsieur',
              'Maple syrup (Concentrate)', 'Lasagne, vegetable, prepared', 'Banana cake',
              'Tomato, stewed, without addition of fat, without addition of salt', 'French pizza from Alsace, baked',
              'Cooked sausage', 'Sugar, glazing', 'Damson plum', 'White chocolate',
              'Smoked cooked sausage of pork and beef meat sausag', 'Popcorn salted', 'Soup, Miso', 'White radish',
              'Prosecco', 'Grissini', 'Turnover with meat (small meat pie, empanadas)', 'Kebab in pita bread',
              'Mushroom, (average), stewed, without addition of fat, without addition of salt',
              'Milk Chocolate with hazelnuts', 'Margarine', 'Soup, cream of vegetables', 'Apricot, dried',
              'Mix of dried fruits and nuts', 'Faux-mage Cashew, vegan chers', 'Chorizo', 'Soya drink (soy milk)',
              'Dumplings', 'Philadelphia', 'Pastry, flaky', 'Sekt', 'Soya Yaourt, yahourt, yogourt ou yoghourt',
              'Macaroon', 'Blackberry', 'Champagne', 'Ice cubes', 'Soup, potato', 'Chocolate milk, chocolate drink',
              'Corn Flakes', 'Brazil nut', 'Gummi bears, fruit jellies, Jelly babies with fruit essence',
              'Cake, marble', 'Cake, salted', 'Mango dried', 'Italian salad dressing', 'Tea, ginger',
              'Buckwheat pancake', 'Chocolate, filled', 'Oat milk', 'Black Forest Tart', 'Bagel (without filling)',
              "M&M's", 'Tea, fruit', 'Tea, spice', 'Tea, rooibos', 'Light beer', 'Oil']


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

category_dict = {}
i = 0
for category_id in category_ids:
    category_dict[category_ids[i]] = categories[i]
    i += 1


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
                'class': category_dict[str(coord['label'])],
                'coordinates': coord['coords'][0],
                'precision': 56.6
            })

        print(results)
        return jsonify({'success': True, 'results': results}), 200
    except Exception as e:
        return jsonify({'success': False, 'message': str(e)}), 500


if __name__ == '__main__':
    app.run(host='127.0.0.1', port=6000, debug=True)
