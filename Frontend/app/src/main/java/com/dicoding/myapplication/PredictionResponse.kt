
package com.dicoding.myapplication

data class PredictionResponse(
    val message: String,
    val imageUrl: String,
    val disease_name: String,
    val confidence: String,
    val description: String
)
