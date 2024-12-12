package com.dicoding.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var genderGroup: RadioGroup
    private lateinit var ktpInput: EditText
    private lateinit var nimInput: EditText
    private lateinit var universityInput: EditText
    private lateinit var practiceAddressInput: EditText
    private lateinit var phoneInput: EditText
    private lateinit var birthdateInput: EditText
    private lateinit var cityInput: EditText
    private lateinit var registerBtn: Button
    private lateinit var progressBar: ProgressBar

    private var userType: String = "" // "coass" or "patient"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        nameInput = findViewById(R.id.input_name)
        emailInput = findViewById(R.id.input_email)
        passwordInput = findViewById(R.id.input_password)
        genderGroup = findViewById(R.id.gender_group)
        ktpInput = findViewById(R.id.input_ktp)
        nimInput = findViewById(R.id.input_nim)
        universityInput = findViewById(R.id.input_university)
        practiceAddressInput = findViewById(R.id.input_practice_address)
        phoneInput = findViewById(R.id.input_phone)
        birthdateInput = findViewById(R.id.input_birthdate)
        cityInput = findViewById(R.id.input_city)
        registerBtn = findViewById(R.id.btn_register)
        progressBar = findViewById(R.id.progress_bar)

        userType = intent.getStringExtra("user_type") ?: "patient"
        setupForm()

        registerBtn.setOnClickListener {
            if (validateInput()) {
                if (userType == "coass") {
                    registerCoAss()
                } else {
                    registerPatient()
                }
            }
        }
    }

    private fun setupForm() {
        if (userType == "coass") {
            ktpInput.visibility = View.VISIBLE
            nimInput.visibility = View.VISIBLE
            universityInput.visibility = View.VISIBLE
            practiceAddressInput.visibility = View.VISIBLE
            cityInput.visibility = View.GONE
        } else {
            ktpInput.visibility = View.GONE
            nimInput.visibility = View.GONE
            universityInput.visibility = View.GONE
            practiceAddressInput.visibility = View.GONE
            cityInput.visibility = View.VISIBLE
        }
    }

    private fun validateInput(): Boolean {
        if (nameInput.text.isEmpty()) {
            nameInput.error = "Name is required"
            return false
        }
        if (emailInput.text.isEmpty()) {
            emailInput.error = "Email is required"
            return false
        }
        if (passwordInput.text.isEmpty()) {
            passwordInput.error = "Password is required"
            return false
        }
        if (phoneInput.text.isEmpty()) {
            phoneInput.error = "Phone number is required"
            return false
        }

        if (userType == "coass") {
            if (nimInput.text.isEmpty()) {
                nimInput.error = "NIM is required"
                return false
            }
            if (universityInput.text.isEmpty()) {
                universityInput.error = "University is required"
                return false
            }
            if (practiceAddressInput.text.isEmpty()) {
                practiceAddressInput.error = "Practice Address is required"
                return false
            }
        } else {
            if (cityInput.text.isEmpty()) {
                cityInput.error = "City is required"
                return false
            }
        }

        return true
    }

    private fun registerCoAss() {
        val registerRequest = RegisterCoAssRequest(
            email = emailInput.text.toString(),
            password = passwordInput.text.toString(),
            name = nameInput.text.toString(),
            gender = if (genderGroup.checkedRadioButtonId == R.id.gender_male) "Male" else "Female",
            birth_date = birthdateInput.text.toString(),
            ktp = ktpInput.text.toString(),
            nim = nimInput.text.toString(),
            appointment_place = practiceAddressInput.text.toString(),
            university = universityInput.text.toString(),
            phone = phoneInput.text.toString()
        )

        progressBar.visibility = View.VISIBLE
        val apiService = RetrofitClient.retrofitInstance.create(ApiService::class.java)
        apiService.registerCoAss(registerRequest).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, "Co-Ass Registered Successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    handleCoAssErrorResponse(response)
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@RegisterActivity, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun registerPatient() {
        val registerRequest = RegisterPatientRequest(
            email = emailInput.text.toString(),
            password = passwordInput.text.toString(),
            name = nameInput.text.toString(),
            gender = if (genderGroup.checkedRadioButtonId == R.id.gender_male) "Male" else "Female",
            birth_date = birthdateInput.text.toString(),
            city = cityInput.text.toString(),
            phone = phoneInput.text.toString()
        )

        progressBar.visibility = View.VISIBLE
        val apiService = RetrofitClient.retrofitInstance.create(ApiService::class.java)
        apiService.registerPatient(registerRequest).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, "Patient Registered Successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    handlePatientErrorResponse(response)
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@RegisterActivity, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun handleCoAssErrorResponse(response: Response<RegisterResponse>) {
        val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
        Log.e("Registration Error", "Error Body: $errorMessage")

        if (errorMessage.contains("Email, KTP, atau NIM telah terdaftar")) {
            Toast.makeText(this@RegisterActivity, "Email, KTP, or NIM already registered. Please use different details.", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this@RegisterActivity, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
        }
    }


    private fun handlePatientErrorResponse(response: Response<RegisterResponse>) {
        if (response.code() == 400) {
            // Safely extract the error message
            val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
            Log.e("Registration Error", "Error Body: $errorMessage")
            Toast.makeText(this@RegisterActivity, "This email is already registered. Please use another.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this@RegisterActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
        }
    }

}
