package com.andreeailie.tracker_domain.use_case

import com.andreeailie.tracker_domain.model.Grocery
import com.andreeailie.tracker_domain.repository.TrackerRepository

class ToggleGroceryStatus(
    private val repository: TrackerRepository
) {
    suspend operator fun invoke(grocery: Grocery) {
        try {
            repository.insertGrocery(grocery.copy(isChecked = !grocery.isChecked))
        } catch (e: Exception) {
            throw Exception("Failed to add the grocery. Please try again later.")

        }
    }
}
