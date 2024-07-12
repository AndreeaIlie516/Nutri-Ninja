package com.andreeailie.tracker_data.repository

import com.andreeailie.tracker_data.remote.FileUploadApi
import com.andreeailie.tracker_domain.repository.FileUploadRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException

class FileUploadRepositoryImpl(
    private val api: FileUploadApi
) : FileUploadRepository {
    override suspend fun uploadImage(file: File): Boolean {
        return try {
            api.uploadImage(
                image = MultipartBody.Part
                    .createFormData(
                        "image",
                        file.name,
                        file.asRequestBody()
                    )
            )
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        } catch (e: HttpException) {
            e.printStackTrace()
            false
        }
    }
}