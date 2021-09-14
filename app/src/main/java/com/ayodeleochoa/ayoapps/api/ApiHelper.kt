package com.ayodeleochoa.ayoapps.api

class ApiHelper(private val apiService: ApiService) {

    suspend fun getPhotos() = apiService.getPhotos()
}