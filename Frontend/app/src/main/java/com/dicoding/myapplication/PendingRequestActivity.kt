package com.dicoding.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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

class PendingRequestsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pending_requests)

        recyclerView = findViewById(R.id.recyclerViewPendingRequests)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("token", "") ?: ""
        Log.d("TOKEN", "Token: $token")

        fetchPendingRequests()

        // Set up Bottom Navigation
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_pending_requests -> true // Already on this screen
                R.id.nav_schedule_meeting -> {
                    startActivity(Intent(this, ScheduleMeetingActivity::class.java))
                    true
                }
                R.id.nav_coass_schedule -> {
                    startActivity(Intent(this, CoassScheduleActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun fetchPendingRequests() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://temugigi-backend-302773936528.asia-southeast2.run.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        apiService.getPendingRequests("Bearer $token").enqueue(object : Callback<List<PatientRequest>> {
            override fun onResponse(call: Call<List<PatientRequest>>, response: Response<List<PatientRequest>>) {
                if (response.isSuccessful) {
                    val requests = response.body() ?: emptyList()
                    updateUI(requests)
                } else {
                    Toast.makeText(this@PendingRequestsActivity, "Failed to load requests", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<PatientRequest>>, t: Throwable) {
                Toast.makeText(this@PendingRequestsActivity, "Network error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUI(requests: List<PatientRequest>) {
        val noRequestsText = findViewById<TextView>(R.id.noRequestsText)
        if (requests.isEmpty()) {
            noRequestsText.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            noRequestsText.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            recyclerView.adapter = PendingRequestsAdapter(requests, this)
        }
    }
}
