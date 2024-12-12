package com.dicoding.myapplication

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://temugigi-backend-302773936528.asia-southeast2.run.app" // Replace with actual base URL

    // Private Retrofit instance
    val retrofitInstance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Publicly accessible ApiService instance
    val apiService: ApiService by lazy {
        retrofitInstance.create(ApiService::class.java)
    }
}
