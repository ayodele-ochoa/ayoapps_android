package com.ayodeleochoa.ayoapps.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.ayodeleochoa.ayoapps.repos.MainRepository
import kotlinx.coroutines.Dispatchers

class PhotoMainViewModel(private val mainRepository: MainRepository) : ViewModel() {

    fun getPhotos() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getPhotos()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}