package com.andreeailie.tracker_domain.repository

import com.andreeailie.tracker_domain.model.UploadResponse
import java.io.File

interface ModelRepository {

    suspend fun uploadImage(file: File): UploadResponse
}