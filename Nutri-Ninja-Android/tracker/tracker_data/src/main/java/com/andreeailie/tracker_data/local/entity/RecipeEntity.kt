package com.andreeailie.tracker_data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecipeEntity(
    val name: String,
    val imageUrl: String?,
    val ingredients: List<MutableMap<String, Int>>,
    val servingSize: String,
    val servings: Int,
    val type: String,
    val instructions: String,
    @PrimaryKey val id: Int? = null
)