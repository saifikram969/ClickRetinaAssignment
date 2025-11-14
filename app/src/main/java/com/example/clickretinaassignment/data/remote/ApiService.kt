package com.example.clickretinaassignment.data.remote

import com.example.clickretinaassignment.data.model.UserProfile
import com.example.clickretinaassignment.data.model.UserProfileResponse
import retrofit2.http.GET

interface ApiService {
    @GET("android-assesment/profile/refs/heads/main/data.json")
    suspend fun getProfileData(): UserProfileResponse
}