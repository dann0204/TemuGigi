package com.dicoding.myapplication

data class UserProfileResponse(
    val Id: String?,
    val Email: String?,
    val Password: String?, // Typically, you might not need this field for display
    val Name: String?,
    val Role: String?,
    val Gender: String?,
    val Birth_date: String?,
    val City: String?,
    val Phone: String?,
    val Ktp: String?,
    val Nim: String?,
    val Appointment_Place: String?,
    val University: String?,
    val Img_profile: String?
)
