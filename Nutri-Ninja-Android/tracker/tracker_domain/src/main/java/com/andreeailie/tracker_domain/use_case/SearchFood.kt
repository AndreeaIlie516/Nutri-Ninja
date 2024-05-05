package com.andreeailie.tracker_domain.use_case

import android.util.Log
import com.andreeailie.tracker_domain.model.TrackableFood
import com.andreeailie.tracker_domain.repository.TrackerRepository

class SearchFood(
    private val repository: TrackerRepository
) {

    suspend operator  fun invoke(
        query: String
    ): Result<List<TrackableFood>> {
        if(query.isBlank()) {
            return Result.success(emptyList())
        }
        Log.d("SearchFood", "Query is not blank")
        val result = repository.searchFood(query.trim())
        Log.d("SearchFood", "result: $result")
        return result
    }
}