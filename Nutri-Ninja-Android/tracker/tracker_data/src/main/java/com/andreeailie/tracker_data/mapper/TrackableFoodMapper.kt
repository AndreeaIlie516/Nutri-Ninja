package com.andreeailie.tracker_data.mapper

import com.andreeailie.tracker_data.remote.dto.SearchedProduct
import com.andreeailie.tracker_domain.model.TrackableFood
import kotlin.math.roundToInt

fun SearchedProduct.toTrackableFood(): TrackableFood {
    val carbs = nutriments?.carbohydrates?.roundToInt()
    val protein = nutriments?.proteins?.roundToInt()
    val fat = nutriments?.fat?.roundToInt()
    val calories = nutriments?.calories?.roundToInt()
    return TrackableFood(
        name = foodName,
        isBranded = isBranded ?: false,
        brandName = brandName ?: "Unknown",
        imageUrl = imageUrl ?: "",
        quantity = quantity ?: 1,
        unit = unit ?: "g",
        calories = calories ?: 0,
        carbs = carbs ?: 0,
        protein = protein ?: 0,
        fat = fat ?: 0,
    )
}