package com.andreeailie.tracker_domain.use_case

import com.andreeailie.tracker_domain.model.Grocery
import com.andreeailie.tracker_domain.repository.TrackerRepository

class AddGrocery(
    private val repository: TrackerRepository
) {

    suspend operator fun invoke(grocery: Grocery) {
        try {
            repository.insertGrocery(grocery)
        } catch (e: Exception) {
            throw Exception("Failed to add the grocery. Please try again later.")
        }
    }
}