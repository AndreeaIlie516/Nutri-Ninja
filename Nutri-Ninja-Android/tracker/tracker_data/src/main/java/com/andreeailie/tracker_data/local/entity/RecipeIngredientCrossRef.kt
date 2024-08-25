package com.andreeailie.tracker_data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "recipe_ingredient_cross_ref",
    primaryKeys = ["recipeId", "ingredientId"],
    foreignKeys = [
        ForeignKey(
            entity = RecipeEntity::class,
            parentColumns = ["recipeId"],
            childColumns = ["recipeId"]
        ),
        ForeignKey(
            entity = IngredientEntity::class,
            parentColumns = ["ingredientId"],
            childColumns = ["ingredientId"]
        )
    ]
)
data class RecipeIngredientCrossRef(
    val recipeId: Int,
    val ingredientId: Int
)
