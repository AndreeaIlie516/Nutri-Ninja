package com.andreeailie.tracker_domain.model

data class TrackableFood(
    val name: String,
    val isBranded: Boolean,
    val brandName: String?,
    val imageUrl: String?,
    val quantity: Int,
    val unit: String,
    val caloriesPer100g: Int,
    val carbsPer100g: Int,
    val proteinPer100g: Int,
    val fatPer100g: Int
)