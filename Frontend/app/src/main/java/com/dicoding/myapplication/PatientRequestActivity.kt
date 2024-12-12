package com.dicoding.myapplication

import Coass
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PatientRequestActivity : AppCompatActivity() {

    private lateinit var coass: Coass
    private lateinit var token: String

    private lateinit var nameTextView: TextView
    private lateinit var universityTextView: TextView
    private lateinit var practicePlaceTextView: TextView
    private lateinit var phoneTextView: TextView
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_request)

        // Retrieve token from SharedPreferences
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("token", "") ?: ""

        // Retrieve the co-assistant data from the Intent
        coass = intent.getParcelableExtra("coass") ?: throw IllegalArgumentException("Coass not found in intent")

        // Initialize views
        nameTextView = findViewById(R.id.nameTextView)
        universityTextView = findViewById(R.id.universityTextView)
        practicePlaceTextView = findViewById(R.id.practicePlaceTextView)
        phoneTextView = findViewById(R.id.phoneTextView)
        submitButton = findViewById(R.id.submitButton)

        // Populate views with co-assistant data
        nameTextView.text = coass.name
        universityTextView.text = coass.university
        practicePlaceTextView.text = coass.appointmentPlace
        phoneTextView.text = coass.phone

        // Handle Submit button click
        submitButton.setOnClickListener {
            // Show confirmation dialog before proceeding with the request
            showConfirmationDialog()
        }
    }

    private fun showConfirmationDialog() {
        // Build and show the confirmation dialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Konfirmasi Pengajuan")
            .setMessage("Ajukan temu dengan ${coass.name}. Maksimal pengajuan adalah 1x, setelah itu Anda harus menunggu untuk diproses. Apakah Anda yakin melanjutkan?")
            .setPositiveButton("Ya") { dialog, _ ->
                // If user clicks "Yes", send the request to the server
                sendMeetingRequest()
                dialog.dismiss() // Close the dialog
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss() // Close the dialog if user cancels
            }
            .create()
            .show()
    }

    private fun sendMeetingRequest() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://temugigi-backend-302773936528.asia-southeast2.run.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        // Prepare request body with Co-Ass ID
        val requestBody = mapOf("coass_id" to coass.id)

        apiService.requestMeeting("Bearer $token", requestBody).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@PatientRequestActivity, "Pengajuan berhasil", Toast.LENGTH_SHORT).show()
                    finish() // Optionally close the activity
                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(this@PatientRequestActivity, "Gagal mengajukan pertemuan: $error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@PatientRequestActivity, "Network Error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
