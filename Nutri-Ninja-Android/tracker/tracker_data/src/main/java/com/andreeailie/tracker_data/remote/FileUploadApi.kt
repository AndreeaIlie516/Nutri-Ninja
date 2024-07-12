package com.andreeailie.tracker_data.remote

import com.andreeailie.tracker_domain.model.UploadResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FileUploadApi {
    @Multipart
    @POST("upload")
    suspend fun uploadImage(@Part image: MultipartBody.Part): UploadResponse

    companion object {
        const val BASE_URL = "http://10.0.2.2:8080/"
    }
}
