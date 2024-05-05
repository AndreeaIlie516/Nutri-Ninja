package com.andreeailie.tracker_data.remote

import com.andreeailie.tracker_data.remote.dto.NutrientRequest
import com.andreeailie.tracker_data.remote.dto.SearchDto
import com.andreeailie.tracker_data.remote.dto.SearchFoodRequest
import com.andreeailie.tracker_data.remote.dto.SearchedProduct
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CustomFoodApi {

    @POST("searchFood")
    suspend fun searchFood(@Body request: SearchFoodRequest): SearchDto

    @POST("getNutrients")
    suspend fun getNutrients(@Body request: NutrientRequest): SearchedProduct

    companion object {
        const val BASE_URL = "http://10.0.2.2:5000/"
    }
}
