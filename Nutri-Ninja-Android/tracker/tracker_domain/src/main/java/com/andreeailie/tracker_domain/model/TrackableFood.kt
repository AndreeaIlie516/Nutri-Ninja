package com.andreeailie.tracker_domain.model

data class TrackableFood(
    val name: String,
    val isBranded: Boolean,
    val brandName: String?,
    val imageUrl: String?,
    val quantity: Int,
    val unit: String,
    val calories: Int,
    val carbs: Int,
    val protein: Int,
    val fat: Int
)