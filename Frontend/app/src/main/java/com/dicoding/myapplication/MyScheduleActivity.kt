package com.dicoding.myapplication

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyScheduleActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var noSchedulesText: View
    private lateinit var token: String
    private lateinit var adapter: MyScheduleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_schedule)

        recyclerView = findViewById(R.id.recyclerViewMySchedule)
        noSchedulesText = findViewById(R.id.noSchedulesText)

        recyclerView.layoutManager = LinearLayoutManager(this)

        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("token", "") ?: ""

        fetchMySchedule()
    }

    private fun fetchMySchedule() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://temugigi-backend-302773936528.asia-southeast2.run.app") // Adjust base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        apiService.getMySchedule("Bearer $token").enqueue(object : Callback<List<MyPatientSchedule>> {
            override fun onResponse(call: Call<List<MyPatientSchedule>>, response: Response<List<MyPatientSchedule>>) {
                if (response.isSuccessful) {
                    val scheduleList = response.body() ?: emptyList()
                    if (scheduleList.isNotEmpty()) {
                        noSchedulesText.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        adapter = MyScheduleAdapter(scheduleList)
                        recyclerView.adapter = adapter
                    } else {
                        noSchedulesText.visibility = View.VISIBLE
                    }
                } else {
                    Toast.makeText(this@MyScheduleActivity, "Failed to load schedules", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<MyPatientSchedule>>, t: Throwable) {
                Toast.makeText(this@MyScheduleActivity, "Error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
