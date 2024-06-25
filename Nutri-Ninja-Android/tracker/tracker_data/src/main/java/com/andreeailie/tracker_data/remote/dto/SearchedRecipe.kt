package com.andreeailie.tracker_data.remote.dto

import com.squareup.moshi.Json

data class SearchedRecipe(
    val name: String,
    @field:Json(name = "image_url")
    val imageUrl: String?,
    val ingredients: List<Triple<String, String, String>>,
    @field:Json(name = "serving_size")
    val servingSize: Float,
    val servings: Int,
    val instructions: String,
)
