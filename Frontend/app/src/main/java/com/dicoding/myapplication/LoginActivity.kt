package com.dicoding.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginPatientButton: Button
    private lateinit var loginCoassButton: Button
    private lateinit var forgotPasswordTextView: TextView
    private lateinit var registerPatientTextView: TextView
    private lateinit var registerCoassTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEditText = findViewById(R.id.email_input)
        passwordEditText = findViewById(R.id.password_input)
        loginPatientButton = findViewById(R.id.login_patient)
        loginCoassButton = findViewById(R.id.login_coass)
        forgotPasswordTextView = findViewById(R.id.forgot_password)
        registerPatientTextView = findViewById(R.id.register_patient)
        registerCoassTextView = findViewById(R.id.register_coass)

        // Handle Login as Patient
        loginPatientButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password, "patient")
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle Login as Co-Ass
        loginCoassButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password, "coass")
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Forgot Password and Register navigation remains unchanged
        forgotPasswordTextView.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        registerPatientTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra("user_type", "patient")
            startActivity(intent)
        }

        registerCoassTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra("user_type", "coass")
            startActivity(intent)
        }
    }

    private fun loginUser(email: String, password: String, expectedRole: String) {
        val apiService = RetrofitClient.apiService
        val loginRequest = LoginRequest(email, password)

        apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val token = response.body()?.token ?: ""
                    val role = response.body()?.role ?: ""

                    if (token.isNotEmpty() && role.isNotEmpty()) {
                        if (role.equals(expectedRole, ignoreCase = true)) {
                            // Save token and role to SharedPreferences
                            val sharedPreferences =
                                getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
                            with(sharedPreferences.edit()) {
                                putString("token", token)
                                putString("role", role)
                                apply()
                            }

                            // Redirect based on role
                            when (role.lowercase()) {
                                "patient" -> {
                                    val intent =
                                        Intent(this@LoginActivity, CameraActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }

                                "coass" -> {
                                    val intent =
                                        Intent(this@LoginActivity, PendingRequestsActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        } else {
                            // Role mismatch
                            Toast.makeText(
                                this@LoginActivity,
                                "Invalid login for the selected role. Please use the correct button.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Failed to retrieve user details.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Login failed: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(
                    this@LoginActivity,
                    "Error: ${t.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
