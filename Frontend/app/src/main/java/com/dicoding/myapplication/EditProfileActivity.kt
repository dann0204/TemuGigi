package com.dicoding.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class EditProfileActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var genderGroup: RadioGroup
    private lateinit var birthdateEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var updateProfileButton: Button
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Initialize views
        nameEditText = findViewById(R.id.edit_name)
        emailEditText = findViewById(R.id.edit_email)
        genderGroup = findViewById(R.id.edit_gender_group)
        birthdateEditText = findViewById(R.id.edit_birthdate)
        phoneEditText = findViewById(R.id.edit_phone)
        updateProfileButton = findViewById(R.id.btn_update_profile)

        // Retrieve the token from SharedPreferences
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("token", "") ?: ""

        // Fetch existing profile data
        fetchUserProfile()

        // Handle the profile update
        updateProfileButton.setOnClickListener {
            updateUserProfile()
        }
    }

    private fun fetchUserProfile() {
        val apiService = RetrofitClient.apiService
        apiService.getProfile("Bearer $token").enqueue(object : Callback<UserProfileResponse> {
            override fun onResponse(call: Call<UserProfileResponse>, response: Response<UserProfileResponse>) {
                if (response.isSuccessful) {
                    val profile = response.body()
                    if (profile != null) {
                        nameEditText.setText(profile.Name)
                        emailEditText.setText(profile.Email)
                        birthdateEditText.setText(formatBirthDate(profile.Birth_date))
                        phoneEditText.setText(profile.Phone)

                        if (profile.Gender.equals("Male", true)) {
                            genderGroup.check(R.id.edit_gender_male)
                        } else {
                            genderGroup.check(R.id.edit_gender_female)
                        }
                    } else {
                        Toast.makeText(this@EditProfileActivity, "Error: Empty profile response.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@EditProfileActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                Toast.makeText(this@EditProfileActivity, "Error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUserProfile() {
        val name = nameEditText.text.toString()
        val email = emailEditText.text.toString()
        val gender = if (genderGroup.checkedRadioButtonId == R.id.edit_gender_male) "Male" else "Female"
        val birthDate = birthdateEditText.text.toString()
        val phone = phoneEditText.text.toString()

        val apiService = RetrofitClient.apiService
        val updateRequest = UserProfileRequest(name, email, gender, birthDate, phone)

        apiService.updateProfile("Bearer $token", updateRequest).enqueue(object : Callback<UserProfileResponse> {
            override fun onResponse(call: Call<UserProfileResponse>, response: Response<UserProfileResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@EditProfileActivity, "Profile updated successfully!", Toast.LENGTH_SHORT).show()

                    // Pass updated data back to ProfileActivity
                    val intent = Intent()
                    intent.putExtra("name", name)
                    intent.putExtra("email", email)
                    intent.putExtra("gender", gender)
                    intent.putExtra("birth_date", birthDate)
                    intent.putExtra("phone", phone)
                    setResult(Activity.RESULT_OK, intent)

                    finish() // Close EditProfileActivity
                } else {
                    Toast.makeText(this@EditProfileActivity, "Update failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                Toast.makeText(this@EditProfileActivity, "Error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun formatBirthDate(isoDate: String?): String {
        return try {
            val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            isoFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = isoFormat.parse(isoDate ?: "")
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            outputFormat.format(date!!)
        } catch (e: Exception) {
            "Invalid Date"
        }
    }
}
