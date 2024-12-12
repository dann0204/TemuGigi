package com.dicoding.myapplication

data class UpdateProfilePictureResponse(
    val imageUrl: String, // Returned URL of the updated profile picture
    val message: String   // Success or failure message
)
