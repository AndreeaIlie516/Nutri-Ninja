package com.andreeailie.tracker_data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true) val recipeId: Int = 0,
    val name: String,
    val imageUrl: String?,
    val servingSize: Float,
    val servings: Int,
    val instructions: String,
    val description: String
)
