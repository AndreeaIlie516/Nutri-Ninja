package com.andreeailie.tracker_domain.repository

import com.andreeailie.tracker_domain.model.TrackableFood
import com.andreeailie.tracker_domain.model.TrackedFood
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TrackerRepository {

    suspend fun searchFood(
        query: String,
    ): Result<List<TrackableFood>>

    suspend fun getNutrients(
        foodName: String,
        quantity: Int,
        unit: String
    ): Result<TrackableFood?>

    suspend fun insertTrackedFood(food: TrackedFood)

    suspend fun deleteTrackedFood(food: TrackedFood)

    fun getFoodsForDate(localDate: LocalDate): Flow<List<TrackedFood>>
}