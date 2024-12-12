package com.dicoding.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.myapplication.R

class MainActivity : AppCompatActivity() {
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginPatientBtn: Button
    private lateinit var loginCoAssBtn: Button
    private lateinit var registerPatient: TextView
    private lateinit var registerCoAss: TextView
    private lateinit var forgotPassword: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) // Your login XML file

        emailInput = findViewById(R.id.email_input)
        passwordInput = findViewById(R.id.password_input)
        loginPatientBtn = findViewById(R.id.login_patient)
        loginCoAssBtn = findViewById(R.id.login_coass)
        registerPatient = findViewById(R.id.register_patient)
        registerCoAss = findViewById(R.id.register_coass)
        forgotPassword = findViewById(R.id.forgot_password)

        // Handle Login for Pasien
        loginPatientBtn.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            // Handle login for pasien
        }

        // Handle Login for Co-Ass
        loginCoAssBtn.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            // Handle login for co-ass
        }

        // Navigate to RegisterActivity for Patient Registration
        registerPatient.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra("user_type", "patient") // Pass "patient" user type
            startActivity(intent)
        }

// Navigate to RegisterActivity for Co-Ass Registration
        registerCoAss.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra("user_type", "coass") // Pass "coass" user type
            startActivity(intent)
        }


        // Navigate to Forgot Password Screen
        forgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }
}
