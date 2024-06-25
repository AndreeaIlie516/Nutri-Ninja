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
        caloriesPer100g = calories ?: 0,
        carbsPer100g = carbs ?: 0,
        proteinPer100g = protein ?: 0,
        fatPer100g = fat ?: 0,
    )
}