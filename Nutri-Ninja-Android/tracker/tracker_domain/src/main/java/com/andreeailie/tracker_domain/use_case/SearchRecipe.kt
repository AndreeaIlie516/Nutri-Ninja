package com.andreeailie.tracker_domain.use_case

import android.util.Log
import com.andreeailie.tracker_domain.model.Recipe
import com.andreeailie.tracker_domain.repository.TrackerRepository

class SearchRecipe(
    private val repository: TrackerRepository
) {
    suspend operator fun invoke(
        query: String
    ): Result<List<Recipe>> {
        if (query.isBlank()) {
            Log.d("SearchRecipe", "Query is blank")
            return Result.success(emptyList())
        }
        Log.d("SearchRecipe", "Query is not blank")
        return try {
            val result = repository.searchRecipe(query.trim())
            Log.d("SearchRecipe", "result: $result")
            result
        } catch (e: Exception) {
            Log.e("SearchRecipe", "Error during search: $e")
            Result.failure(e)
        }
    }
}