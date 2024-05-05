from flask import Flask, request, jsonify
import requests
import os
from dotenv import load_dotenv

load_dotenv()

app = Flask(__name__)

# Environment variables
API_KEY_NUTRITIONIX = os.getenv('API_KEY_NUTRITIONIX')
APP_ID_NUTRITIONIX = os.getenv('APP_ID_NUTRITIONIX')
API_KEY_UNSPLASH = os.getenv('API_KEY_UNSPLASH')


@app.route('/searchFood', methods=['POST'])
def search_food():
    query = request.json['query']
    headers_nutritionix = {
        'Content-Type': 'application/json',
        'x-app-id': APP_ID_NUTRITIONIX,
        'x-app-key': API_KEY_NUTRITIONIX,
    }
    url_nutritionix = (f'https://trackapi.nutritionix.com/v2/search/instant?query={query}&branded=true&common=true'
                       f'&branded_limit=10&common_limit=10')
    response_nutritionix = requests.post(url_nutritionix, headers=headers_nutritionix, json={"query": query})

    headers_unsplash = {
        'Authorization': f'Client-ID {API_KEY_UNSPLASH}'
    }

    if response_nutritionix.status_code == 200:
        data = response_nutritionix.json()
        food_items = []
        quantity = 100
        unit = 'g'
        url_nutritionix_nutrients = 'https://trackapi.nutritionix.com/v2/natural/nutrients'
        for item in data.get('common', []) + data.get('branded', []):
            food_name = item['food_name']

            response = requests.post(url_nutritionix_nutrients, headers=headers_nutritionix, json={"query": f"{quantity} {unit} {food_name}"})
            data = response.json()

            nutriments = {
                'calories': data['foods'][0]['nf_calories'],
                'carbohydrates': data['foods'][0]['nf_total_carbohydrate'],
                'proteins': data['foods'][0]['nf_protein'],
                'fats': data['foods'][0]['nf_total_fat']

            }
            brand_name = None
            is_branded = False

            if data['foods'][0]['brand_name'] is not None:
                brand_name = data['foods'][0]['brand_name']
                is_branded = True

            image_url = data['foods'][0]['photo']['thumb']
            if not is_branded:

                url_unsplash = f'https://api.unsplash.com/search/photos?page=1&query={food_name}'
                response_unsplash = requests.get(url_unsplash, headers=headers_unsplash)
                if response_unsplash.status_code == 200:
                    unsplash_data = response_unsplash.json()
                    image_url = unsplash_data['results'][0]['urls']['thumb'] if unsplash_data['results'] else None

            food_item = {
                'food_name': data['foods'][0]['food_name'],
                'is_branded': is_branded,
                'brand_name': brand_name,
                'image_url': image_url,
                'quantity': quantity,
                'unit': unit,
                'nutriments': nutriments
            }
            food_items.append(food_item)

        # for item in data.get('common', []):
        #     food_name = item['food_name']
        #     headers_unsplash = {
        #         'Authorization': f'Client-ID {API_KEY_UNSPLASH}'
        #     }
        #     url_unsplash = f'https://api.unsplash.com/search/photos?page=1&query={food_name}'
        #     response_unsplash = requests.get(url_unsplash, headers=headers_unsplash)
        #     if response_unsplash.status_code == 200:
        #         unsplash_data = response_unsplash.json()
        #         image_url = unsplash_data['results'][0]['urls']['thumb'] if unsplash_data['results'] else None
        #     else:
        #         image_url = None
        #
        #     food_items.append({
        #         'food_name': item['food_name'],
        #         'image_url': image_url,
        #     })
        #
        # for item in data.get('branded', []):
        #     if 'photo' in item and 'thumb' in item['photo']:
        #         image_url = item['photo']['thumb']
        #
        #     food_items.append({
        #         'food_name': item['food_name'],
        #         'image_url': image_url,
        #     })

        return jsonify({"products": food_items}), 200
    else:
        return jsonify({"error": "Failed to fetch data from Nutritionix"}), response_nutritionix.status_code


@app.route('/getNutrients', methods=['POST'])
def get_nutrients():
    food_name = request.json['food_name']
    quantity = request.json['quantity']
    unit = request.json['unit']
    headers = {
        'Content-Type': 'application/json',
        'x-app-id': APP_ID_NUTRITIONIX,
        'x-app-key': API_KEY_NUTRITIONIX,
    }
    url = 'https://trackapi.nutritionix.com/v2/natural/nutrients'
    response = requests.post(url, headers=headers, json={"query": f"{quantity} {unit} {food_name}"})
    data = response.json()

    nutriments = {
        'calories': data['foods'][0]['nf_calories'],
        'carbohydrates': data['foods'][0]['nf_total_carbohydrate'],
        'proteins': data['foods'][0]['nf_protein'],
        'fats': data['foods'][0]['nf_total_fat']

    }
    brand_name = None
    is_branded = False

    if data['foods'][0]['brand_name'] is not None:
        brand_name = data['foods'][0]['brand_name']
        is_branded = True

    image_url = data['foods'][0]['photo']['thumb']
    if not is_branded:
        headers_unsplash = {
            'Authorization': f'Client-ID {API_KEY_UNSPLASH}'
        }
        url_unsplash = f'https://api.unsplash.com/search/photos?page=1&query={food_name}'
        response_unsplash = requests.get(url_unsplash, headers=headers_unsplash)
        if response_unsplash.status_code == 200:
            unsplash_data = response_unsplash.json()
            image_url = unsplash_data['results'][0]['urls']['thumb'] if unsplash_data['results'] else None

    food_item = {
        'food_name': data['foods'][0]['food_name'],
        'is_branded': is_branded,
        'brand_name': brand_name,
        'image_url': image_url,
        'quantity': quantity,
        'unit': unit,
        'nutriments': nutriments
    }
    if response.status_code == 200:
        return jsonify(food_item), 200
    else:
        return jsonify({"error": "Failed to fetch nutrients"}), response.status_code


if __name__ == '__main__':
    app.run(debug=True)
