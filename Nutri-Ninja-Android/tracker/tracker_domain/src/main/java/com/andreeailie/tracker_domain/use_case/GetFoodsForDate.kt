package com.andreeailie.tracker_domain.use_case

import com.andreeailie.tracker_domain.model.TrackedFood
import com.andreeailie.tracker_domain.repository.TrackerRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class GetFoodsForDate(
    private val repository: TrackerRepository
) {

    fun invoke (date: LocalDate): Flow<List<TrackedFood>> {
        return repository.getFoodsForDate(date)
    }
}