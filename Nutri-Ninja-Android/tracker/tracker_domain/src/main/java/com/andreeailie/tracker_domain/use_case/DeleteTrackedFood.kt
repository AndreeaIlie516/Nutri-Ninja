package com.andreeailie.tracker_domain.use_case

import com.andreeailie.tracker_domain.model.TrackedFood
import com.andreeailie.tracker_domain.repository.TrackerRepository

class DeleteTrackedFood(
    private val repository: TrackerRepository
) {

    suspend operator fun invoke(trackedFood: TrackedFood) {
        repository.deleteTrackedFood(trackedFood)
    }
}