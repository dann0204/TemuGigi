package com.dicoding.myapplication

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ScheduleAcceptMeetingActivity : AppCompatActivity() {

    private lateinit var token: String
    private lateinit var requestId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accept_schedule_meeting)

        // Retrieve token from SharedPreferences
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("token", "") ?: ""

        // Retrieve request details from Intent
        requestId = intent.getIntExtra("REQUEST_ID", -1).toString()
        val patientName = intent.getStringExtra("PATIENT_NAME") ?: ""
        val diseaseName = intent.getStringExtra("DISEASE_NAME") ?: ""

        // Populate TextViews
        findViewById<TextView>(R.id.patientName).text = "Patient Name: $patientName"
        findViewById<TextView>(R.id.diseaseName).text = "Disease Name: $diseaseName"

        // Set up buttons
        val confirmButton = findViewById<Button>(R.id.confirmButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)

        cancelButton.setOnClickListener { finish() }
        confirmButton.setOnClickListener { scheduleMeeting() }
    }

    private fun scheduleMeeting() {
        val appointmentDate = findViewById<EditText>(R.id.appointmentDate).text.toString()
        val appointmentPlace = findViewById<EditText>(R.id.appointmentPlace).text.toString()

        if (appointmentDate.isBlank() || appointmentPlace.isBlank()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }

        val requestBody = mapOf(
            "request_id" to requestId,
            "appointment_date" to appointmentDate, // Properly formatted date
            "appointment_place" to appointmentPlace
        )

        val retrofit = Retrofit.Builder()
            .baseUrl("https://temugigi-backend-302773936528.asia-southeast2.run.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        apiService.scheduleMeeting("Bearer $token", requestBody).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ScheduleAcceptMeetingActivity, "Meeting scheduled successfully.", Toast.LENGTH_SHORT).show()
                    finish() // Close the activity
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("ScheduleMeeting", "Error: $errorBody")
                    Toast.makeText(this@ScheduleAcceptMeetingActivity, "Failed to schedule meeting.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("ScheduleMeeting", "Failure: ${t.localizedMessage}")
                Toast.makeText(this@ScheduleAcceptMeetingActivity, "Network error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
