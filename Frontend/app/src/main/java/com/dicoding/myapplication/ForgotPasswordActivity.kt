package com.dicoding.myapplication

import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.dicoding.myapplication.RetrofitClient

class ForgotPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val emailEditText = findViewById<EditText>(R.id.email_input)
        val submitButton = findViewById<Button>(R.id.submit_button)

        submitButton.setOnClickListener {
            val email = emailEditText.text.toString()
            if (email.isNotEmpty()) {
                sendForgotPasswordRequest(email)
            } else {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendForgotPasswordRequest(email: String) {

        val apiService = RetrofitClient.apiService


        val call = apiService.forgotPassword(ForgotPasswordRequest(email))

        call.enqueue(object : Callback<ForgotPasswordResponse> {
            override fun onResponse(call: Call<ForgotPasswordResponse>, response: Response<ForgotPasswordResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val forgotPasswordResponse = response.body()!!
                    if (forgotPasswordResponse.success) {

                        Toast.makeText(this@ForgotPasswordActivity, "Check your email for reset instructions.", Toast.LENGTH_SHORT).show()
                    } else {

                        Toast.makeText(this@ForgotPasswordActivity, forgotPasswordResponse.message, Toast.LENGTH_SHORT).show()
                    }
                } else {

                    Toast.makeText(this@ForgotPasswordActivity, "User not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                
                Toast.makeText(this@ForgotPasswordActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
