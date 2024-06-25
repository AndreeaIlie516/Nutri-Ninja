package com.andreeailie.tracker_data.remote.dto

import com.squareup.moshi.Json

data class SearchedGrocery(
    val name: String,
    @field:Json(name = "is_branded")
    val isBranded: Boolean,
    @field:Json(name = "brand_name")
    val brandName: String?,
    @field:Json(name = "image_url")
    val imageUrl: String?,
    val unit: String,
    val calories: Int,
    val quantity: Int,
    val type: String,
    @field:Json(name = "is_checked")
    val isChecked: Boolean,
)