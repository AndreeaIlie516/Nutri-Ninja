package com.andreeailie.tracker_presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreeailie.tracker_domain.model.UploadResponse
import com.andreeailie.tracker_domain.use_case.TrackerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FileUploadViewModel @Inject constructor(
    private val trackerUseCases: TrackerUseCases
) : ViewModel() {

    private val _uploadResponse = MutableLiveData<UploadResponse>()
    val uploadResponse: LiveData<UploadResponse> = _uploadResponse

    fun uploadImage(file: File) {
        viewModelScope.launch {
            try {
                val response = trackerUseCases.uploadFile(file)
                _uploadResponse.postValue(response)
            } catch (e: Exception) {
                _uploadResponse.postValue(UploadResponse(false, e.message ?: "Unknown error"))
            }
        }
    }
}