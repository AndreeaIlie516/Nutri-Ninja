package com.andreeailie.tracker_domain.model


data class Recipe(
    val name: String,
    val imageUrl: String?,
    val ingredients: List<Ingredient>,
    val servingSize: Float,
    val servings: Int,
    val instructions: String,
    val description: String,
    val id: Int? = null
)

class InvalidRecipeException(message: String) : Exception(message)