package com.example.clickretinaassignment.viewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clickretinaassignment.data.model.UserProfile
import com.example.clickretinaassignment.data.repository.ProfileRepository
import com.example.clickretinaassignment.utils.NetworkUtils
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: ProfileRepository = ProfileRepository()
) : ViewModel() {

    var profile by mutableStateOf<UserProfile?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun fetchProfile(context: Context) {

        Log.d("ProfileAPI", "fetchProfile() called")


        if (!NetworkUtils.isInternetAvailable(context)) {
            errorMessage = "No Internet Connection"
            isLoading = false
            return
        }

        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null

                Log.d("ProfileAPI", "Calling API...")

                val response = repository.getProfile()
                Log.d("ProfileAPI", "API Response Raw → ${response}")

                profile = response.user

                Log.d("ProfileAPI", "User Profile Data → ${profile}")

            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "Something went wrong"
                Log.e("ProfileAPI", "API Error → ${e.localizedMessage}", e)

            } finally {
                isLoading = false

                Log.d("ProfileAPI", "Loading stopped")

            }
        }
    }
}
