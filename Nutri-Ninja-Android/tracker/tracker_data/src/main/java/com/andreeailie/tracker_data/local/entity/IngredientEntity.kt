package com.andreeailie.tracker_data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingredients")
data class IngredientEntity(
    @PrimaryKey(autoGenerate = true) val ingredientId: Int = 0,
    val name: String,
    val description: String,
    val imageUrl: String?
)
