package com.andreeailie.tracker_data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.andreeailie.tracker_data.local.entity.TrackedFoodEntity
import com.andreeailie.tracker_domain.model.MealType
import com.andreeailie.tracker_domain.model.TrackedFood
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
fun TrackedFoodEntity.toTrackedFood(): TrackedFood {
    return TrackedFood(
        name = name,
        isBranded = isBranded,
        brandName = brandName,
        carbs = carbs,
        protein = protein,
        fat = fat,
        imageUrl = imageUrl,
        mealType = MealType.fromString(type),
        quantity = quantity,
        unit = unit,
        date = LocalDate.of(year, month, dayOfMonth),
        calories = calories,
        id = id
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun TrackedFood.toTrackedFoodEntity(): TrackedFoodEntity {
    return TrackedFoodEntity(
        name = name,
        isBranded = isBranded,
        brandName = brandName,
        carbs = carbs,
        protein = protein,
        fat = fat,
        imageUrl = imageUrl,
        type = mealType.name,
        quantity = quantity,
        unit = unit,
        dayOfMonth = date.dayOfMonth,
        month = date.monthValue,
        year = date.year,
        calories = calories,
        id = id
    )
}