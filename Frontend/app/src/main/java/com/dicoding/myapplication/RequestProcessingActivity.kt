package com.dicoding.myapplication

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RequestProcessingActivity : AppCompatActivity() {

    private lateinit var token: String
    private lateinit var requestId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_processing)

        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("token", "") ?: ""
        requestId = intent.getStringExtra("REQUEST_ID") ?: ""

        findViewById<Button>(R.id.acceptButton).setOnClickListener {
            updateRequestStatus("Accepted")
        }

        findViewById<Button>(R.id.rejectButton).setOnClickListener {
            updateRequestStatus("Rejected")
        }
    }

    private fun updateRequestStatus(status: String) {
        val requestBody = mapOf(
            "request_id" to requestId,
            "status" to status
        )

        val retrofit = Retrofit.Builder()
            .baseUrl("https://temugigi-backend-302773936528.asia-southeast2.run.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        apiService.reviewRequest("Bearer $token", requestBody).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@RequestProcessingActivity, "Request status updated successfully.", Toast.LENGTH_SHORT).show()
                    finish() // Close the activity after success
                } else {
                    Toast.makeText(this@RequestProcessingActivity, "Failed to update request.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@RequestProcessingActivity, "Network error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
