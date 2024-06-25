package com.andreeailie.tracker_domain.use_case

import com.andreeailie.tracker_domain.model.Grocery
import com.andreeailie.tracker_domain.repository.TrackerRepository
import kotlinx.coroutines.flow.Flow

class GetGroceries(
    private val repository: TrackerRepository
) {

    operator fun invoke(): Flow<List<Grocery>> {
        return repository.getGroceries()
    }
}