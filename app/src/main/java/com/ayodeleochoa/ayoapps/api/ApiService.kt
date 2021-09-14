package com.ayodeleochoa.ayoapps.api

import com.ayodeleochoa.ayoapps.models.Photo
import retrofit2.http.GET

interface ApiService {

    @GET("photos.json")
    suspend fun getPhotos(): List<Photo>

}