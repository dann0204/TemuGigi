package com.dicoding.myapplication

import com.google.gson.annotations.SerializedName

data class PatientRequest(
    @SerializedName("Id") val id: String,
    @SerializedName("Name") val name: String,
    @SerializedName("Request_id") val requestId: Int,
    @SerializedName("Img_disease") val imageUrl: String,
    @SerializedName("Disease_name") val diseaseName: String,
    @SerializedName("Description") val description: String,
    @SerializedName("Requested_at") val requestedAt: String
)
