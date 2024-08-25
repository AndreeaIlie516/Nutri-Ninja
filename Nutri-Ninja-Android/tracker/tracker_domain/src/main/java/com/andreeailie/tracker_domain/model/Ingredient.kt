package com.andreeailie.tracker_domain.model


data class Ingredient(
    val name: String,
    val description: String,
    val imageUrl: String?,
    val id: Int? = null
)

class InvalidIngredientException(message: String) : Exception(message)