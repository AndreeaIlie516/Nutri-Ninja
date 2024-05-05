package com.andreeailie.tracker_data.remote.dto

import com.squareup.moshi.Json

data class Nutriments(
    val calories: Double,
    val carbohydrates: Double,
    val fat: Double,
    val proteins: Double
)