package com.andreeailie.tracker_presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreeailie.tracker_domain.use_case.TrackerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FileUploadViewModel @Inject constructor(
    private val trackerUseCases: TrackerUseCases
) : ViewModel() {

    fun uploadImage(file: File) {
        viewModelScope.launch {
            trackerUseCases.uploadFile(file)
        }
    }
}