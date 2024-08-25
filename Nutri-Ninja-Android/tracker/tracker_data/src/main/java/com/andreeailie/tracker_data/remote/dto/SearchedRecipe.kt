package com.andreeailie.tracker_data.remote.dto

import com.squareup.moshi.Json

data class SearchedRecipe(
    val name: String,
    @field:Json(name = "image_url")
    val imageUrl: String?,
    val ingredients: List<List<String>>,
    @field:Json(name = "serving_size")
    val servingSize: String,
    val servings: String,
    val instructions: String,
    @field:Json(name = "recipe_description")
    val recipeDescription: String
)
