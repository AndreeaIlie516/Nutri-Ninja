package com.andreeailie.tracker_data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TrackedFoodEntity(
    val name: String,
    val isBranded: Boolean,
    val brandName: String?,
    val imageUrl: String?,
    val unit: String,
    val calories: Int,
    val carbs: Int,
    val protein: Int,
    val fat: Int,
    val type: String,
    val quantity: Int,
    val dayOfMonth: Int,
    val month: Int,
    val year: Int,
    @PrimaryKey val id: Int? = null
)