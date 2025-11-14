package com.example.clickretinaassignment.data.repository

import com.example.clickretinaassignment.data.model.UserProfileResponse
import com.example.clickretinaassignment.data.remote.RetrofitInstance

class ProfileRepository {
    suspend fun getProfile(): UserProfileResponse =
        RetrofitInstance.apiService.getProfileData()
}
