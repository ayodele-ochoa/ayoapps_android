package com.ayodeleochoa.ayoapps.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ayodeleochoa.ayoapps.api.ApiHelper
import com.ayodeleochoa.ayoapps.repos.MainRepository

class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PhotoMainViewModel::class.java)) {
            return PhotoMainViewModel(MainRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}