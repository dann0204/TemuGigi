package com.dicoding.myapplication

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var oldPasswordEditText: EditText
    private lateinit var newPasswordEditText: EditText
    private lateinit var confirmNewPasswordEditText: EditText
    private lateinit var changePasswordButton: Button
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)


        oldPasswordEditText = findViewById(R.id.currentPassword)
        newPasswordEditText = findViewById(R.id.newPassword)
        confirmNewPasswordEditText = findViewById(R.id.confirmNewPassword)
        changePasswordButton = findViewById(R.id.changePasswordButton)


        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("token", "") ?: ""

        changePasswordButton.setOnClickListener {
            val oldPassword = oldPasswordEditText.text.toString()
            val newPassword = newPasswordEditText.text.toString()
            val confirmPassword = confirmNewPasswordEditText.text.toString()

            if (newPassword != confirmPassword) {
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = ChangePasswordRequest(oldPassword, newPassword)
            changePassword(request)
        }
    }

    private fun changePassword(request: ChangePasswordRequest) {
        if (token.isEmpty()) {
            Toast.makeText(this, "Error: Missing token. Please login again.", Toast.LENGTH_SHORT).show()
            return
        }

        RetrofitClient.apiService.changePassword("Bearer $token", request)
            .enqueue(object : Callback<ChangePasswordResponse> {
                override fun onResponse(call: Call<ChangePasswordResponse>, response: Response<ChangePasswordResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@ChangePasswordActivity, "Password updated successfully!", Toast.LENGTH_SHORT).show()
                        finish() // Navigate back
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Toast.makeText(this@ChangePasswordActivity, "Failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                        Log.e("ChangePassword", "Error: ${response.code()} - $errorBody")
                    }
                }

                override fun onFailure(call: Call<ChangePasswordResponse>, t: Throwable) {
                    Toast.makeText(this@ChangePasswordActivity, "Error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                    Log.e("ChangePassword", "Failure: ${t.localizedMessage}")
                }
            })
    }
}