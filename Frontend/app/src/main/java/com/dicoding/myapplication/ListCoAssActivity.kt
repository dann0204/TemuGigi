package com.dicoding.myapplication

import Coass
import android.content.Context
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListCoAssActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var token: String
    private lateinit var adapter: CoAssAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_coass)

        // Retrieve token from SharedPreferences
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("token", "") ?: ""

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.coassRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize adapter
        adapter = CoAssAdapter(emptyList()) { selectedCoass ->
            // Show Coass details in a dialog
            showCoassDetailsDialog(selectedCoass)
        }
        recyclerView.adapter = adapter

        // Fetch Co-Ass list from the API
        getCoAssList()
    }

    private fun getCoAssList() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://temugigi-backend-302773936528.asia-southeast2.run.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        apiService.getCoAssList("Bearer $token").enqueue(object : Callback<List<Coass>> {
            override fun onResponse(call: Call<List<Coass>>, response: Response<List<Coass>>) {
                if (response.isSuccessful) {
                    val coassList = response.body() ?: emptyList()
                    Log.d("ListCoAssActivity", "Fetched Co-Ass List: $coassList")

                    if (coassList.isNotEmpty()) {
                        adapter = CoAssAdapter(coassList) { selectedCoass ->
                            showCoassDetailsDialog(selectedCoass)
                        }
                        recyclerView.adapter = adapter
                    } else {
                        Toast.makeText(this@ListCoAssActivity, "No Co-Ass available.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(this@ListCoAssActivity, "Failed to load Co-Ass: $error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Coass>>, t: Throwable) {
                Toast.makeText(this@ListCoAssActivity, "API Call Failed: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showCoassDetailsDialog(coass: Coass) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_profile, null)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        // Bind views
        val profileImage = dialogView.findViewById<ImageView>(R.id.profile_image_dialog)
        val profileName = dialogView.findViewById<TextView>(R.id.profile_name)
        val profileEmail = dialogView.findViewById<TextView>(R.id.profile_email)
        val profileGender = dialogView.findViewById<TextView>(R.id.profile_gender)
        val profileBirthDate = dialogView.findViewById<TextView>(R.id.profile_birth_date)
        val profilePhone = dialogView.findViewById<TextView>(R.id.profile_phone)
        val submitRequestButton = dialogView.findViewById<Button>(R.id.submitRequestButton)

        // Populate views with Coass data
        profileName.text = "Name: ${coass.name}"
        profileEmail.text = "Email: N/A" // Replace with actual field if available in Coass
        profileGender.text = "Gender: ${coass.gender}"
        profileBirthDate.text = "Birth Date: N/A" // Replace with actual field if available in Coass
        profilePhone.text = "Phone: ${coass.phone}"

        // Handle profile image loading with Glide
        if (!coass.imgProfile.isNullOrEmpty()) {
            Glide.with(this)
                .load(coass.imgProfile)
                .placeholder(R.drawable.ic_profile_placeholder) // Ensure this placeholder exists
                .into(profileImage)
        } else {
            profileImage.setImageResource(R.drawable.ic_profile_placeholder) // Fallback if no image URL
        }

        // Handle submit button click
        submitRequestButton.setOnClickListener {
            sendRequestToCoass(coass)
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun sendRequestToCoass(coass: Coass) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://temugigi-backend-302773936528.asia-southeast2.run.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val requestBody = mapOf("coass_id" to coass.id)

        apiService.requestMeeting("Bearer $token", requestBody).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ListCoAssActivity, "Request sent successfully.", Toast.LENGTH_SHORT).show()
                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(this@ListCoAssActivity, "Failed to send request: $error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@ListCoAssActivity, "Network Error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
