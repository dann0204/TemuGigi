package com.dicoding.myapplication

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ScheduleMeetingActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var noRequestsText: TextView
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_meeting)

        recyclerView = findViewById(R.id.recyclerViewAcceptedRequests)
        noRequestsText = findViewById(R.id.noRequestsText)

        recyclerView.layoutManager = LinearLayoutManager(this)

        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("token", "") ?: ""

        setupBottomNavigation()

        fetchAcceptedRequests()
    }

    private fun setupBottomNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_pending_requests -> {
                    startActivity(Intent(this, PendingRequestsActivity::class.java))
                    true
                }
                R.id.nav_schedule_meeting -> true // Already on this screen
                R.id.nav_coass_schedule -> {
                    startActivity(Intent(this, CoassScheduleActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun fetchAcceptedRequests() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://temugigi-backend-302773936528.asia-southeast2.run.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        apiService.getAcceptedRequests("Bearer $token").enqueue(object : Callback<List<PatientRequest>> {
            override fun onResponse(
                call: Call<List<PatientRequest>>,
                response: Response<List<PatientRequest>>
            ) {
                if (response.isSuccessful) {
                    val requests = response.body() ?: emptyList()
                    if (requests.isEmpty()) {
                        noRequestsText.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    } else {
                        noRequestsText.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        recyclerView.adapter = AcceptedRequestsAdapter(requests) { request ->
                            processMeetingRequest(request)
                        }
                    }
                } else {
                    Toast.makeText(
                        this@ScheduleMeetingActivity,
                        "Failed to fetch accepted requests.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<PatientRequest>>, t: Throwable) {
                Toast.makeText(
                    this@ScheduleMeetingActivity,
                    "Network error: ${t.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun processMeetingRequest(request: PatientRequest) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://temugigi-backend-302773936528.asia-southeast2.run.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val body = mapOf(
            "request_id" to request.requestId.toString(),
            "appointment_date" to "2024-12-10T10:00:00", // Example date
            "appointment_place" to "RS Gresik, Dental Clinic Room 101" // Example place
        )

        apiService.scheduleMeeting("Bearer $token", body).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    // Redirect to CoassScheduleActivity after scheduling
                    Toast.makeText(
                        this@ScheduleMeetingActivity,
                        "Meeting scheduled successfully.",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this@ScheduleMeetingActivity, CoassScheduleActivity::class.java))
                    finish()
                } else {
                    showFailureDialog()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(
                    this@ScheduleMeetingActivity,
                    "Network error: ${t.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun showFailureDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Gagal Menjadwalkan")
        builder.setMessage("Permintaan tidak valid atau sudah diproses.")
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, _ ->
            dialog.dismiss()
        })
        val dialog = builder.create()
        dialog.show()
    }
}
