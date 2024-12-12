package com.dicoding.myapplication

import com.google.gson.annotations.SerializedName

data class Schedule(
    @SerializedName("Id") val id: String,
    @SerializedName("Date") val date: String,
    @SerializedName("Time") val time: String,
    @SerializedName("Place") val place: String,
    @SerializedName("PatientName") val patientName: String,
    @SerializedName("CoassName") val coassName: String
)
