package com.andreeailie.tracker_domain.use_case

import com.andreeailie.tracker_domain.model.Grocery
import com.andreeailie.tracker_domain.repository.TrackerRepository

class DeleteGrocery(
    val repository: TrackerRepository
) {

    suspend operator fun invoke(grocery: Grocery) {
        try {
            repository.deleteGrocery(grocery)
        } catch (e: Exception) {
            throw Exception("Failed to delete the grocery. Please try again later.")
        }
    }
}