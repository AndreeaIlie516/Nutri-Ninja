package com.andreeailie.tracker_data.repository

import com.andreeailie.tracker_data.remote.ModelApi
import com.andreeailie.tracker_domain.model.UploadResponse
import com.andreeailie.tracker_domain.repository.ModelRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException

class ModelRepositoryImpl(
    private val api: ModelApi
) : ModelRepository {
    override suspend fun uploadImage(file: File): UploadResponse {
        return try {
            api.uploadImage(
                image = MultipartBody.Part
                    .createFormData(
                        "file",
                        file.name,
                        file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    )
            )
        } catch (e: IOException) {
            e.printStackTrace()
            UploadResponse(false, "Network Error")
        } catch (e: HttpException) {
            e.printStackTrace()
            UploadResponse(false, "Server Error")
        }
    }
}