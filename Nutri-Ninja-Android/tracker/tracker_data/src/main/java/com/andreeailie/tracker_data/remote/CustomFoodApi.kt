package com.andreeailie.tracker_data.remote

import com.andreeailie.tracker_data.remote.dto.NutrientRequest
import com.andreeailie.tracker_data.remote.dto.SearchFoodRequest
import com.andreeailie.tracker_data.remote.dto.SearchGroceryDto
import com.andreeailie.tracker_data.remote.dto.SearchGroceryRequest
import com.andreeailie.tracker_data.remote.dto.SearchProductDto
import com.andreeailie.tracker_data.remote.dto.SearchRecipeDto
import com.andreeailie.tracker_data.remote.dto.SearchRecipeRequest
import com.andreeailie.tracker_data.remote.dto.SearchedProduct
import retrofit2.http.Body
import retrofit2.http.POST

interface CustomFoodApi {

    @POST("searchFood")
    suspend fun searchFood(@Body request: SearchFoodRequest): SearchProductDto

    @POST("getNutrients")
    suspend fun getNutrients(@Body request: NutrientRequest): SearchedProduct

    @POST("searchRecipe")
    suspend fun searchRecipe(@Body request: SearchRecipeRequest): SearchRecipeDto

    @POST("searchGrocery")
    suspend fun searchGrocery(@Body request: SearchGroceryRequest): SearchGroceryDto

    companion object {
        const val BASE_URL = "http://10.0.2.2:5000/"
    }
}
