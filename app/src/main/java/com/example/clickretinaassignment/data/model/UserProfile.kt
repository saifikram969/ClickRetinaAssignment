package com.example.clickretinaassignment.data.model

data class UserProfileResponse(
    val user: UserProfile
)

data class UserProfile(
    val username: String?,
    val name: String?,
    val avatar: String?,
    val location: Location?,
    val social: SocialLinks?,
    val statistics: Statistics?
)

data class Location(
    val city: String?,
    val country: String?
)

data class SocialLinks(
    val website: String?,
    val profiles: List<SocialProfile>?
)

data class SocialProfile(
    val platform: String?,
    val url: String?
)

data class Statistics(
    val followers: Int?,
    val following: Int?,
    val activity: ActivityStats?
)

data class ActivityStats(
    val shots: Int?,
    val collections: Int?
)
