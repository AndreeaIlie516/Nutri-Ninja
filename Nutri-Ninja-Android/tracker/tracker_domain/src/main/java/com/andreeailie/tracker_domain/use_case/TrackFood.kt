package com.andreeailie.tracker_domain.use_case

import com.andreeailie.tracker_domain.model.MealType
import com.andreeailie.tracker_domain.model.TrackableFood
import com.andreeailie.tracker_domain.model.TrackedFood
import com.andreeailie.tracker_domain.repository.TrackerRepository
import java.time.LocalDate
import kotlin.math.roundToInt

class TrackFood(
    private val repository: TrackerRepository
) {

    suspend operator fun invoke(
        foodName: String,
        quantity: Int = 100,
        unit: String = "g",
        mealType: MealType,
        date: LocalDate
    ) {
        var trackedFood: TrackableFood? = null

        val result: Result<TrackableFood?> = repository.getNutrients(
            foodName = foodName,
            quantity = quantity,
            unit = unit
        )

        result.onSuccess { trackableFood ->
            trackedFood = trackableFood

        }.onFailure {

        }
        trackedFood?.let {
            TrackedFood(
                name = foodName,
                isBranded = it.isBranded,
                brandName = it.brandName,
                imageUrl = it.imageUrl,
                unit = it.unit,
                calories = it.calories,
                carbs = it.carbs,
                protein = it.protein,
                fat = it.fat,
                mealType = mealType,
                quantity = quantity,
                date = date,
            )
        }?.let {
            repository.insertTrackedFood(
                it
            )
        }
    }
}