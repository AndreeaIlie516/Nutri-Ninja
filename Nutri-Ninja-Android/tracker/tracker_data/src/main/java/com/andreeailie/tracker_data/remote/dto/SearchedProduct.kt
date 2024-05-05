package com.andreeailie.tracker_data.remote.dto

import com.squareup.moshi.Json

data class SearchedProduct(
//    @field:Json(name = "image_front_thumb_url")
//    val imageFrontThumbUrl: String?,
//    val nutriments: Nutriments?,
//    @field:Json(name = "product_name")
//    val productName: String?
    @field:Json(name = "food_name")
    val foodName: String,
    @field:Json(name = "is_branded")
    val isBranded: Boolean?,
    @field:Json(name = "brand_name")
    val brandName: String?,
    @field:Json(name = "image_url")
    val imageUrl: String?,
    val quantity: Int?,
    val unit: String?,
    val nutriments: Nutriments?,

)