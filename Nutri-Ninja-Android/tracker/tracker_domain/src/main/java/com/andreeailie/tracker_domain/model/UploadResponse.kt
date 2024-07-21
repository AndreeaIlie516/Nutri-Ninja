package com.andreeailie.tracker_domain.model

data class UploadResponse(
    val success: Boolean,
    val message: String? = null,
    val results: List<Result>? = null
)

data class Result(
    val `class`: String,
    val coordinates: List<Int>,
    val precision: Double
)