package com.andreeailie.tracker_data.remote.dto

import com.squareup.moshi.Json

data class NutrientRequest(
    @field:Json(name = "food_name")
    val foodName: String,
    val quantity: Int,
    val unit: String
)