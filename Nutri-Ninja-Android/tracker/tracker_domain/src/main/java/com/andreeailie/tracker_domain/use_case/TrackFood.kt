package com.andreeailie.tracker_domain.use_case

import android.util.Log
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
        quantity: Int,
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

        Log.d("TrackFood", "result: $result")

        result.onSuccess { trackableFood ->
            Log.d("TrackFood", "trackableFood: $trackableFood")
            trackedFood = trackableFood

        }.onFailure {
            Log.d("TrackFood", "onFailure")
        }
        trackedFood?.let {
            TrackedFood(
                name = foodName,
                isBranded = it.isBranded,
                brandName = it.brandName,
                imageUrl = it.imageUrl,
                unit = it.unit,
                calories = ((it.caloriesPer100g / 100f) * quantity).roundToInt(),
                carbs = ((it.carbsPer100g / 100f) * quantity).roundToInt(),
                protein = ((it.proteinPer100g / 100f) * quantity).roundToInt(),
                fat = ((it.fatPer100g / 100f) * quantity).roundToInt(),
                mealType = mealType,
                quantity = quantity,
                date = date,
            )
        }?.let {
            repository.insertTrackedFood(
                it
            )
            Log.d("TrackFood", "trackedFood: $it")
        }
    }
}