package com.andreeailie.tracker_domain.use_case

import com.andreeailie.tracker_domain.model.UploadResponse
import com.andreeailie.tracker_domain.repository.ModelRepository
import java.io.File

class UploadFile(
    private val repository: ModelRepository
) {
    suspend operator fun invoke(file: File): UploadResponse {
        return try {
            repository.uploadImage(file)
        } catch (e: Exception) {
            throw Exception("Failed to upload image. Please try again later.", e)
        }
    }
}