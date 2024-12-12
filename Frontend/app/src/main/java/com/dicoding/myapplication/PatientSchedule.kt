package com.dicoding.myapplication

import com.google.gson.annotations.SerializedName

data class MyPatientSchedule(
    @SerializedName("Schedule_id") val scheduleId: Int,
    @SerializedName("Appointment_date") val appointmentDate: String?,
    @SerializedName("Appointment_place") val appointmentPlace: String?,
    @SerializedName("Coass_Img") val coassImg: String?,
    @SerializedName("Coass_name") val coassName: String,
    @SerializedName("Coass_phone") val coassPhone: String,
    @SerializedName("Coass_university") val coassUniversity: String,
    @SerializedName("Img_disease") val imgDisease: String?,
    @SerializedName("Disease_name") val diseaseName: String,
    @SerializedName("Description") val description: String
)
