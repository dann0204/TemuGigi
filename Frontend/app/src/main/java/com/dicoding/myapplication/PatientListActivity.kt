package com.dicoding.myapplication

import Patient
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PatientListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_list)

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewPatients)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Retrieve token and request ID from SharedPreferences and Intent
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        token = sharedPreferences.getString("token", "") ?: ""

        val requestId = intent.getStringExtra("REQUEST_ID")
        if (requestId.isNullOrEmpty()) {
            Toast.makeText(this, "Request ID is missing!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }


        // Fetch and display the patients
        fetchPatients(requestId)
    }


    private fun fetchPatients(requestId: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://temugigi-backend-302773936528.asia-southeast2.run.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        apiService.getPatients("Bearer $token", requestId).enqueue(object : Callback<List<Patient>> {
            override fun onResponse(call: Call<List<Patient>>, response: Response<List<Patient>>) {
                if (response.isSuccessful) {
                    val patients = response.body() ?: emptyList()
                    recyclerView.adapter = PatientAdapter(patients) { selectedPatient ->
                        showPatientDetailsDialog(selectedPatient)
                    }
                } else {
                    Toast.makeText(this@PatientListActivity, "Failed to load patients.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Patient>>, t: Throwable) {
                Toast.makeText(this@PatientListActivity, "Network Error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun showPatientDetailsDialog(patient: Patient) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_patient_details, null)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        dialogView.findViewById<TextView>(R.id.patientName).text = "Name: ${patient.name}"
        dialogView.findViewById<TextView>(R.id.diseaseName).text = "Disease Name: ${patient.diseaseName}"
        dialogView.findViewById<TextView>(R.id.description).text = patient.description
        dialogView.findViewById<TextView>(R.id.requestedAt).text = "Requested At: ${patient.requestedAt}"

        val imageView = dialogView.findViewById<ImageView>(R.id.patientImage)
        Glide.with(this)
            .load(patient.imageUrl)
            .placeholder(R.drawable.ic_profile_placeholder)
            .into(imageView)

        dialogView.findViewById<Button>(R.id.acceptButton).setOnClickListener {
            handleAccept(patient.id)
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.rejectButton).setOnClickListener {
            handleReject(patient.id)
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.backButton).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
    private fun handleAccept(patientId: String) {
        val requestBody = mapOf("request_id" to patientId, "status" to "Accepted")

        val retrofit = Retrofit.Builder()
            .baseUrl("https://temugigi-backend-302773936528.asia-southeast2.run.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        apiService.reviewRequest("Bearer $token", requestBody).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@PatientListActivity, "Request Accepted", Toast.LENGTH_SHORT).show()
                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(this@PatientListActivity, "Failed to accept request: $error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@PatientListActivity, "Network Error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun handleReject(patientId: String) {
        val requestBody = mapOf("request_id" to patientId, "status" to "Rejected")

        val retrofit = Retrofit.Builder()
            .baseUrl("https://temugigi-backend-302773936528.asia-southeast2.run.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        apiService.reviewRequest("Bearer $token", requestBody).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@PatientListActivity, "Request Rejected", Toast.LENGTH_SHORT).show()
                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(this@PatientListActivity, "Failed to reject request: $error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@PatientListActivity, "Network Error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }


}