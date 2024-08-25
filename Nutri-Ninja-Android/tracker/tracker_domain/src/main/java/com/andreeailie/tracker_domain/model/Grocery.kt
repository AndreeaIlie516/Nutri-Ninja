package com.andreeailie.tracker_domain.model

data class Grocery(
    val name: String,
    val imageUrl: String?,
    val unit: String,
    val quantity: Float,
    val isChecked: Boolean,
    val id: Int? = null
)

class InvalidGroceryException(message: String) : Exception(message)