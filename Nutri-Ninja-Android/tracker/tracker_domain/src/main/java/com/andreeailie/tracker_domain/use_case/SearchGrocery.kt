package com.andreeailie.tracker_domain.use_case

import com.andreeailie.tracker_domain.model.Grocery
import com.andreeailie.tracker_domain.repository.TrackerRepository

class SearchGrocery(
    private val repository: TrackerRepository
) {

    suspend operator fun invoke(
        query: String
    ): Result<List<Grocery>> {
        if (query.isBlank()) {
            return Result.success(emptyList())
        }
        return repository.searchGrocery(query.trim())
    }
}