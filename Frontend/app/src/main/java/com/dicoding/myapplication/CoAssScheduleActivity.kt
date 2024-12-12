package com.dicoding.myapplication

import CoassSchedule
import CoassScheduleAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CoassScheduleActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var noSchedulesText: TextView
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coass_schedule)

        recyclerView = findViewById(R.id.recyclerViewCoassSchedule)
        recyclerView.layoutManager = LinearLayoutManager(this)
        noSchedulesText = findViewById(R.id.noSchedulesText)

        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("token", "") ?: ""

        fetchCoassSchedule()
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_pending_requests -> {
                    startActivity(Intent(this, PendingRequestsActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.nav_schedule_meeting -> {
                    startActivity(Intent(this, ScheduleMeetingActivity::class.java)) // Corrected Activity
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.nav_coass_schedule -> true // Already on this screen
                else -> false
            }
        }
        bottomNavigationView.selectedItemId = R.id.nav_coass_schedule
    }

    private fun fetchCoassSchedule() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://temugigi-backend-302773936528.asia-southeast2.run.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        apiService.getCoassSchedule("Bearer $token").enqueue(object : Callback<List<CoassSchedule>> {
            override fun onResponse(call: Call<List<CoassSchedule>>, response: Response<List<CoassSchedule>>) {
                if (response.isSuccessful) {
                    val schedules = response.body() ?: emptyList()
                    if (schedules.isNotEmpty()) {
                        noSchedulesText.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        recyclerView.adapter = CoassScheduleAdapter(schedules)
                    } else {
                        showEmptyState()
                    }
                } else {
                    Toast.makeText(
                        this@CoassScheduleActivity,
                        "Failed to load schedule: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<CoassSchedule>>, t: Throwable) {
                Toast.makeText(
                    this@CoassScheduleActivity,
                    "Network error: ${t.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun showEmptyState() {
        noSchedulesText.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }
}
