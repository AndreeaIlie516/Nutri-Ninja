package com.andreeailie.tracker_data.remote.dto

import com.squareup.moshi.Json

data class SearchedIngredient(
    val name: String,
    val description: String,
    @field:Json(name = "image_url")
    val imageUrl: String?,
)