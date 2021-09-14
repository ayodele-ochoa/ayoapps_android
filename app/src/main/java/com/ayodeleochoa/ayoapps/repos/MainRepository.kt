package com.ayodeleochoa.ayoapps.repos

import com.ayodeleochoa.ayoapps.api.ApiHelper

class MainRepository(private val apiHelper: ApiHelper) {

    suspend fun getPhotos() = apiHelper.getPhotos()
}