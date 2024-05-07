from concurrent.futures import ThreadPoolExecutor
from functools import lru_cache

import numpy as np
from flask import Flask, request, jsonify
import requests
import os
from dotenv import load_dotenv

load_dotenv()

app = Flask(__name__)

# API_KEY_NUTRITIONIX = os.getenv('API_KEY_NUTRITIONIX')
# APP_ID_NUTRITIONIX = os.getenv('APP_ID_NUTRITIONIX')

API_KEY_FATSECRET = os.getenv('API_KEY_FATSECRET')
API_SECRET_FATSECRET = os.getenv('API_SECRET_FATSECRET')
API_KEY_UNSPLASH = os.getenv('API_KEY_UNSPLASH')

#
# food_items = []
#
#
# @app.route('/searchFood', methods=['POST'])
# def search_food():
#     query = request.json['query']
#     headers_nutritionix = {
#         'Content-Type': 'application/json',
#         'x-app-id': APP_ID_NUTRITIONIX,
#         'x-app-key': API_KEY_NUTRITIONIX,
#     }
#     if food_items != []:
#         return jsonify({"products": food_items}), 200
#
#     url_nutritionix = (f'https://trackapi.nutritionix.com/v2/search/instant?query={query}&branded=true&common=true'
#                        f'&branded_limit=10&common_limit=10')
#     response_nutritionix = requests.post(url_nutritionix, headers=headers_nutritionix, json={"query": query})
#
#     headers_unsplash = {
#         'Authorization': f'Client-ID {API_KEY_UNSPLASH}'
#     }
#
#     if response_nutritionix.status_code == 200:
#         data = response_nutritionix.json()
#         quantity = 100
#         unit = 'g'
#         url_nutritionix_nutrients = 'https://trackapi.nutritionix.com/v2/natural/nutrients'
#         for item in data.get('common', []) + data.get('branded', []):
#             food_name = item['food_name']
#
#             response = requests.post(url_nutritionix_nutrients, headers=headers_nutritionix, json={"query": f"{quantity} {unit} {food_name}"})
#             data = response.json()
#
#             nutriments = {
#                 'calories': data['foods'][0]['nf_calories'],
#                 'carbohydrates': data['foods'][0]['nf_total_carbohydrate'],
#                 'proteins': data['foods'][0]['nf_protein'],
#                 'fat': data['foods'][0]['nf_total_fat']
#
#             }
#             brand_name = None
#             is_branded = False
#
#             if data['foods'][0]['brand_name'] is not None:
#                 brand_name = data['foods'][0]['brand_name']
#                 is_branded = True
#
#             image_url = data['foods'][0]['photo']['thumb']
#             if not is_branded:
#
#                 url_unsplash = f'https://api.unsplash.com/search/photos?page=1&query={food_name}'
#                 response_unsplash = requests.get(url_unsplash, headers=headers_unsplash)
#                 if response_unsplash.status_code == 200:
#                     unsplash_data = response_unsplash.json()
#                     image_url = unsplash_data['results'][0]['urls']['thumb'] if unsplash_data['results'] else None
#
#             food_item = {
#                 'food_name': data['foods'][0]['food_name'],
#                 'is_branded': is_branded,
#                 'brand_name': brand_name,
#                 'image_url': image_url,
#                 'quantity': quantity,
#                 'unit': unit,
#                 'nutriments': nutriments
#             }
#             food_items.append(food_item)
#
#         # for item in data.get('common', []):
#         #     food_name = item['food_name']
#         #     headers_unsplash = {
#         #         'Authorization': f'Client-ID {API_KEY_UNSPLASH}'
#         #     }
#         #     url_unsplash = f'https://api.unsplash.com/search/photos?page=1&query={food_name}'
#         #     response_unsplash = requests.get(url_unsplash, headers=headers_unsplash)
#         #     if response_unsplash.status_code == 200:
#         #         unsplash_data = response_unsplash.json()
#         #         image_url = unsplash_data['results'][0]['urls']['thumb'] if unsplash_data['results'] else None
#         #     else:
#         #         image_url = None
#         #
#         #     food_items.append({
#         #         'food_name': item['food_name'],
#         #         'image_url': image_url,
#         #     })
#         #
#         # for item in data.get('branded', []):
#         #     if 'photo' in item and 'thumb' in item['photo']:
#         #         image_url = item['photo']['thumb']
#         #
#         #     food_items.append({
#         #         'food_name': item['food_name'],
#         #         'image_url': image_url,
#         #     })
#
#         return jsonify({"products": food_items}), 200
#     else:
#         return jsonify({"error": "Failed to fetch data from Nutritionix"}), response_nutritionix.status_code
#
#
# @app.route('/getNutrients', methods=['POST'])
# def get_nutrients():
#     food_name = request.json['food_name']
#     quantity = request.json['quantity']
#     unit = request.json['unit']
#     headers = {
#         'Content-Type': 'application/json',
#         'x-app-id': APP_ID_NUTRITIONIX,
#         'x-app-key': API_KEY_NUTRITIONIX,
#     }
#     url = 'https://trackapi.nutritionix.com/v2/natural/nutrients'
#     response = requests.post(url, headers=headers, json={"query": f"{quantity} {unit} {food_name}"})
#     data = response.json()
#
#     nutriments = {
#         'calories': data['foods'][0]['nf_calories'],
#         'carbohydrates': data['foods'][0]['nf_total_carbohydrate'],
#         'proteins': data['foods'][0]['nf_protein'],
#         'fat': data['foods'][0]['nf_total_fat']
#
#     }
#     brand_name = None
#     is_branded = False
#
#     if data['foods'][0]['brand_name'] is not None:
#         brand_name = data['foods'][0]['brand_name']
#         is_branded = True
#
#     image_url = data['foods'][0]['photo']['thumb']
#     if not is_branded:
#         headers_unsplash = {
#             'Authorization': f'Client-ID {API_KEY_UNSPLASH}'
#         }
#         url_unsplash = f'https://api.unsplash.com/search/photos?page=1&query={food_name}'
#         response_unsplash = requests.get(url_unsplash, headers=headers_unsplash)
#         if response_unsplash.status_code == 200:
#             unsplash_data = response_unsplash.json()
#             image_url = unsplash_data['results'][0]['urls']['thumb'] if unsplash_data['results'] else None
#
#     food_item = {
#         'food_name': data['foods'][0]['food_name'],
#         'is_branded': is_branded,
#         'brand_name': brand_name,
#         'image_url': image_url,
#         'quantity': quantity,
#         'unit': unit,
#         'nutriments': nutriments
#     }
#     if response.status_code == 200:
#         return jsonify(food_item), 200
#     else:
#         return jsonify({"error": "Failed to fetch nutrients"}), response.status_code

food_items = []


@lru_cache()
def get_access_token():
    url = "https://oauth.fatsecret.com/connect/token"
    data = {
        "grant_type": "client_credentials",
        "scope": "basic"
    }
    response = requests.post(url, auth=(API_KEY_FATSECRET, API_SECRET_FATSECRET), data=data)
    if response.status_code == 200:
        return response.json()['access_token']
    return None


token = get_access_token()


@app.route('/searchFood', methods=['POST'])
def search_food():
    global food_items
    query = request.json['query']
    headers = {
        "Authorization": f"Bearer {token}",
        "Content-Type": "application/json"
    }
    headers_unsplash = {
        'Authorization': f'Client-ID {API_KEY_UNSPLASH}'
    }
    url = "https://platform.fatsecret.com/rest/server.api"
    params = {
        "method": "foods.search",
        "search_expression": query,
        "max_results": 7,
        "format": "json"
    }

    # Make the initial search request
    response = requests.post(url, headers=headers, params=params)

    if response.status_code == 200:
        data = response.json()

        # Prepare data for parallel processing
        food_details = [(item, headers, headers_unsplash) for item in data["foods"]["food"]]

        # Process each food item in parallel
        with ThreadPoolExecutor(max_workers=10) as executor:
            results = executor.map(fetch_details, food_details)

        food_items = list(results)

    return jsonify({"products": food_items}), 200


def fetch_details(food_detail):
    item, headers, headers_unsplash = food_detail
    food_name = item['food_name']
    food_id = item['food_id']

    # Fetch nutrient details
    nutriments = fetch_nutrients(food_id, headers)

    # Fetch image URL
    image_url = fetch_image(food_name, headers_unsplash)

    is_branded = (item["food_type"] == "Brand")
    brand_name = item.get("brand_name", "")

    return {
        'food_name': food_name,
        'is_branded': is_branded,
        'brand_name': brand_name,
        'image_url': image_url,
        'quantity': 100,
        'unit': 'g',
        'nutriments': nutriments
    }


def fetch_nutrients(food_id, headers):
    url = "https://platform.fatsecret.com/rest/server.api"
    params_nutrients = {
        "method": "food.get.v4",
        "food_id": food_id,
        "format": "json"
    }
    response = requests.post(url, headers=headers, params=params_nutrients)
    if response.status_code == 200:
        nutriments_data = response.json()
        return extract_nutrients(nutriments_data)
    return None


def fetch_image(food_name, headers_unsplash):
    url_unsplash = f'https://api.unsplash.com/search/photos?page=1&query={food_name}'
    response = requests.get(url_unsplash, headers=headers_unsplash)
    if response.status_code == 200:
        unsplash_data = response.json()
        if unsplash_data['results']:
            return unsplash_data['results'][0]['urls']['thumb']
    return None


def extract_nutrients(nutriments_data):
    nutriments = None
    for serving in nutriments_data["food"]["servings"]["serving"]:
        if serving.get("metric_serving_amount") == "100.000":
            nutriments = {
                "calories": float(serving["calories"]),
                "carbohydrates": float(serving["carbohydrate"]),
                "proteins": float(serving["protein"]),
                "fat": float(serving["fat"])
            }
            break
        if "serving" in serving.get("measurement_description"):
            if serving.get("metric_serving_amount"):
                ratio = 100 / float(serving.get("metric_serving_amount"))
                nutriments = {
                    "calories": np.round(float(serving["calories"]) * ratio, 2),
                    "carbohydrates": np.round(float(serving["carbohydrate"]) * ratio, 2),
                    "proteins": np.round(float(serving["protein"]) * ratio, 2),
                    "fat": np.round(float(serving["fat"]) * ratio, 2)
                }
    return nutriments


@app.route('/getNutrients', methods=['POST'])
def get_nutrients():
    food_name = request.json['food_name']

    print(food_items)

    result = [item for item in food_items if item['food_name'] == food_name]
    print(result)
    if result:
        return jsonify(result[0]), 200
    else:
        return jsonify({"error": "Failed to fetch nutrients"}), 400


if __name__ == '__main__':
    app.run(debug=True)
