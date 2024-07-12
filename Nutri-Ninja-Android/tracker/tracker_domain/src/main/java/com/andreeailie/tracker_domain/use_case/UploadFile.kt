package com.andreeailie.tracker_domain.use_case

import com.andreeailie.tracker_domain.repository.FileUploadRepository
import java.io.File

class UploadFile(
    private val repository: FileUploadRepository
) {
    suspend operator fun invoke(file: File) {
        try {
            repository.uploadImage(file)
        } catch (e: Exception) {
            throw Exception("Failed to upload image. Please try again later.")
        }
    }
}