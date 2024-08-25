package com.andreeailie.tracker_data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GroceryEntity(
    val name: String,
    val imageUrl: String?,
    val unit: String,
    val quantity: Float,
    val isChecked: Boolean,
    @PrimaryKey val id: Int? = null
)