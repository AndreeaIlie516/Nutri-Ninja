package com.andreeailie.tracker_domain.repository

import java.io.File

interface FileUploadRepository {

    suspend fun uploadImage(file: File): Boolean
}