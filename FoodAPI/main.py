from concurrent.futures import ThreadPoolExecutor, as_completed
from functools import lru_cache

import numpy as np
from flask import Flask, request, jsonify
import requests
import os
from dotenv import load_dotenv

load_dotenv()

app = Flask(__name__)

API_KEY_FATSECRET = os.getenv('API_KEY_FATSECRET')
API_SECRET_FATSECRET = os.getenv('API_SECRET_FATSECRET')
API_KEY_UNSPLASH = os.getenv('API_KEY_UNSPLASH')

food_items = []


@lru_cache()
def get_access_token():
    url = "https://oauth.fatsecret.com/connect/token"
    data = {
        "grant_type": "client_credentials",
        "scope": "premier"
    }
    response = requests.post(url, auth=(API_KEY_FATSECRET, API_SECRET_FATSECRET), data=data)
    if response.status_code == 200:
        return response.json()['access_token']
    return None


def refresh_token():
    global token
    token = get_access_token()


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
        "method": "foods.search.v3",
        "search_expression": query,
        "max_results": 10,
        "format": "json",
        "language": "en",
        "include_food_images": True
    }

    response = requests.post(url, headers=headers, params=params)

    if response.status_code == 200:
        data = response.json()

        food_details = [(item, headers, headers_unsplash) for item in data["foods_search"]["results"]["food"]]

        with ThreadPoolExecutor(max_workers=10) as executor:
            results = executor.map(fetch_details, food_details)

        food_items = list(results)

        return jsonify({"products": food_items}), 200
    else:
        if response.status_code == 401:
            refresh_token()
        return jsonify({"error": "Failed to search food"}), 400


def fetch_details(food_detail):
    item, headers, headers_unsplash = food_detail
    food_name = item['food_name']
    food_id = item['food_id']

    # Check if food_images exist and is not None
    image_url = None
    if item.get("food_images") and item["food_images"].get("food_image"):
        image_url = item["food_images"]["food_image"][0]["image_url"]
    else:
        # Fallback to fetch image from Unsplash if food_images are not available
        image_url = fetch_image(food_name, headers_unsplash)

    nutriments = fetch_nutrients(food_id, headers)

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

    result = [item for item in food_items if item['food_name'] == food_name]
    if result:
        return jsonify(result[0]), 200
    else:
        return jsonify({"error": "Failed to fetch nutrients"}), 400


@app.route('/searchRecipes', methods=['POST'])
def search_recipes():
    query = request.json['query']
    headers = {
        "Authorization": f"Bearer {token}",
        "Content-Type": "application/json"
    }
    url = "https://platform.fatsecret.com/rest/server.api"
    params = {
        "method": "recipes.search.v3",
        "search_expression": query,
        "max_results": 1,
        "format": "json",
        "language": "en"
    }

    response = requests.post(url, headers=headers, params=params)

    if response.status_code == 200:
        data = response.json()
        recipes = data.get("recipes", {}).get("recipe", [])
        if recipes:
            first_recipe_id = recipes[0]["recipe_id"]
            recipe_details = fetch_recipe_details(first_recipe_id)
            return jsonify({"recipe": recipe_details}), 200
        else:
            return jsonify({"error": "No recipes found"}), 400
    else:
        if response.status_code == 401:
            refresh_token()
        return jsonify({"error": "Failed to search recipes"}), 400


def fetch_ingredient(ingredients):
    headers_unsplash = {
        'Authorization': f'Client-ID {API_KEY_UNSPLASH}'
    }

    def fetch_single_ingredient(ingredient):
        ingredient_name = ingredient.get("food_name", "")
        ingredient_description = ingredient.get("ingredient_description", "")
        image_url = fetch_image(ingredient_name, headers_unsplash)
        return (ingredient_name, ingredient_description, image_url)

    with ThreadPoolExecutor(max_workers=10) as executor:
        futures = [executor.submit(fetch_single_ingredient, ingredient) for ingredient in ingredients]
        formatted_ingredients = [future.result() for future in as_completed(futures)]

    return formatted_ingredients


def fetch_recipe_details(recipe_id):
    headers = {
        "Authorization": f"Bearer {token}",
        "Content-Type": "application/json"
    }
    url = "https://platform.fatsecret.com/rest/server.api"
    params = {
        "method": "recipe.get.v2",
        "recipe_id": recipe_id,
        "format": "json"
    }

    response = requests.post(url, headers=headers, params=params)

    if response.status_code == 200:
        recipe = response.json().get("recipe", {})
        directions = recipe.get("directions", {}).get("direction", [])
        instructions = " ".join([direction.get("direction_description", "") for direction in directions])

        ingredients = recipe.get("ingredients", {}).get("ingredient", [])
        formatted_ingredients = fetch_ingredient(ingredients)

        recipe_details = {
            "name": recipe.get("recipe_name", ""),
            "image_url": recipe.get("recipe_images", {}).get("recipe_image", [""])[0],
            "recipe_description": recipe.get("recipe_description", ""),
            "servings": recipe.get("number_of_servings", ""),
            "serving_size": recipe.get("grams_per_portion"),
            "ingredients": formatted_ingredients,
            "instructions": instructions,
        }
        return recipe_details
    return None


if __name__ == '__main__':
    app.run(debug=True)
