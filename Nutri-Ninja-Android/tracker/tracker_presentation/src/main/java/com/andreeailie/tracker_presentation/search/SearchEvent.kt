package com.andreeailie.tracker_presentation.search

import com.andreeailie.tracker_domain.model.MealType
import com.andreeailie.tracker_domain.model.TrackableFood
import java.time.LocalDate

sealed class SearchEvent {
    data class OnQueryChange(val query: String) : SearchEvent()
    object OnSearch : SearchEvent()
    data class OnToggleTrackableFood(val food: TrackableFood) : SearchEvent()
    data class OnAmountForFoodChange(
        val food: TrackableFood,
        val amount: String
    ) : SearchEvent()

    data class OnTrackFoodClick(
        val food: TrackableFood,
        val mealType: MealType,
        val date: LocalDate,
        val unit: String = "g"
    ) : SearchEvent()

    data class OnSearchFocusChange(val isFocused: Boolean) : SearchEvent()
    data class SaveIdentifiedItems(
        val identifiedItems: Map<String, Pair<String, Int>>,
        val mealName: String,
        val date: LocalDate,
        val unit: String = "g"
    ) : SearchEvent()
}