package com.dicoding.myapplication

data class ChangePasswordRequest(
    val oldPassword: String,
    val newPassword: String
)

data class ChangePasswordResponse(
    val message: String
)
