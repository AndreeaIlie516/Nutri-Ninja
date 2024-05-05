package com.andreeailie.tracker_domain.model

import java.time.LocalDate

data class TrackedFood(
    val name: String,
    val isBranded: Boolean,
    val brandName: String?,
    val imageUrl: String?,
    val unit: String,
    val calories: Int,
    val carbs: Int,
    val protein: Int,
    val fat: Int,
    val mealType: MealType,
    val quantity: Int,
    val date: LocalDate,
    val id: Int? = null
)