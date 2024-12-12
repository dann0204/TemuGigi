package com.dicoding.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.util.Locale

class ProfileActivity : AppCompatActivity() {

    private lateinit var profilePic: ImageView
    private lateinit var changeProfilePicButton: Button
    private lateinit var setImageButton: Button
    private lateinit var token: String
    private var currentPhotoPath: String? = null
    private val CAMERA_REQUEST_CODE = 100
    private val GALLERY_REQUEST_CODE = 101
    private val EDIT_PROFILE_REQUEST_CODE = 102
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_widget)

        // Set up the action bar with a back button
        supportActionBar?.apply {
            title = "Profile"
            setDisplayHomeAsUpEnabled(true)
        }
        // Back to Camera Button Click Listener
        val backToCameraButton = findViewById<Button>(R.id.backToCameraButton)
        backToCameraButton.setOnClickListener {
            navigateBackToCamera()
        }


        // Initialize views
        profilePic = findViewById(R.id.profilePic)
        changeProfilePicButton = findViewById(R.id.editProfilePicButton)
        setImageButton = findViewById(R.id.setImageButton)
        val editProfileButton = findViewById<Button>(R.id.editProfileButton)

        // Hide "Set Image" button by default
        setImageButton.visibility = Button.GONE

        // Get token from SharedPreferences
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("token", "") ?: ""

        // Fetch and display user profile data
        fetchUserProfile()
        val changePasswordButton = findViewById<Button>(R.id.changePasswordButton)
        changePasswordButton.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }
// Logout Button Click Listener
        val logoutButton = findViewById<Button>(R.id.logoutButton)
        logoutButton.setOnClickListener {
            performLogout()
        }


        // Navigate to EditProfileActivity
        editProfileButton.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivityForResult(intent, EDIT_PROFILE_REQUEST_CODE)
        }

        // Change Profile Picture Button Click Listener
        changeProfilePicButton.setOnClickListener {
            showImageSourceDialog()
        }

        // Set Image Button Click Listener
        setImageButton.setOnClickListener {
            selectedImageUri?.let { uri ->
                uploadProfilePicture(uri)
            }
        }
    }

    private fun showImageSourceDialog() {
        val options = arrayOf("Open Camera", "Choose from Gallery")
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Select Image Source")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> openCamera()
                1 -> openGallery()
            }
        }
        builder.show()
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            val photoFile: File? = try {
                createImageFile()
            } catch (ex: IOException) {
                null
            }
            photoFile?.also {
                val photoURI = FileProvider.getUriForFile(
                    this,
                    "com.dicoding.myapplication.fileprovider",
                    it
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
            }
        }
    }
    private fun performLogout() {
        // Clear the saved token from SharedPreferences
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()

        // Redirect to LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }


    private fun openGallery() {
        val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickPhotoIntent.type = "image/*"
        startActivityForResult(pickPhotoIntent, GALLERY_REQUEST_CODE)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val storageDir: File = getExternalFilesDir(null)!!
        val image = File(storageDir, "temp_image.jpg")
        currentPhotoPath = image.absolutePath
        return image
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            selectedImageUri = when (requestCode) {
                CAMERA_REQUEST_CODE -> Uri.fromFile(File(currentPhotoPath))
                GALLERY_REQUEST_CODE -> data?.data
                else -> null
            }

            selectedImageUri?.let { uri ->
                // Load the selected image into the profilePic ImageView
                Glide.with(this).load(uri).circleCrop().into(profilePic)
                setImageButton.visibility = Button.VISIBLE
            }
        }
    }

    private fun uploadProfilePicture(imageUri: Uri) {
        val imageFile = File(getRealPathFromURI(imageUri))
        if (!imageFile.exists()) {
            Toast.makeText(this, "Error: File not found at ${imageFile.absolutePath}", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("UploadDebug", "Uploading file: ${imageFile.name}")
        Log.d("UploadDebug", "File size: ${imageFile.length()} bytes")
        Log.d("UploadDebug", "MIME type: image/jpeg")

        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), imageFile)
        // Update the key to "Img_profile" as per the API requirement
        val body = MultipartBody.Part.createFormData("Img_profile", imageFile.name, requestFile)

        RetrofitClient.apiService.updateProfilePicture("Bearer $token", body)
            .enqueue(object : Callback<UpdateProfilePictureResponse> {
                override fun onResponse(call: Call<UpdateProfilePictureResponse>, response: Response<UpdateProfilePictureResponse>) {
                    if (response.isSuccessful) {
                        val updatedImageUrl = response.body()?.imageUrl
                        Log.d("UploadDebug", "Upload successful. New image URL: $updatedImageUrl")
                        Toast.makeText(this@ProfileActivity, "Profile picture updated successfully!", Toast.LENGTH_SHORT).show()

                        // Save to SharedPreferences
                        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
                        sharedPreferences.edit().putString("profile_image", updatedImageUrl).apply()

                        // Reload the profile picture
                        Glide.with(this@ProfileActivity).load(updatedImageUrl).circleCrop().into(profilePic)
                        setImageButton.visibility = Button.GONE
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("UploadDebug", "Server responded with error: $errorBody")
                        Toast.makeText(this@ProfileActivity, "Failed to update picture: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UpdateProfilePictureResponse>, t: Throwable) {
                    Log.e("UploadDebug", "Upload failed: ${t.localizedMessage}")
                    Toast.makeText(this@ProfileActivity, "Error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun getRealPathFromURI(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.moveToFirst()
        val idx = cursor?.getColumnIndex(MediaStore.Images.ImageColumns.DATA) ?: 0
        val realPath = cursor?.getString(idx) ?: ""
        cursor?.close()
        return realPath
    }
    private fun navigateBackToCamera() {
        // Navigate back to ActivityCamera
        val intent = Intent(this, CameraActivity::class.java)
        startActivity(intent)
        finish() // Finish ProfileActivity to prevent returning back here
    }

    private fun fetchUserProfile() {
        val apiService = RetrofitClient.apiService
        apiService.getProfile("Bearer $token").enqueue(object : Callback<UserProfileResponse> {
            override fun onResponse(call: Call<UserProfileResponse>, response: Response<UserProfileResponse>) {
                if (response.isSuccessful) {
                    val profile = response.body()
                    if (profile != null) {
                        findViewById<TextView>(R.id.email)?.text = "Email: ${profile.Email ?: "N/A"}"
                        findViewById<TextView>(R.id.name)?.text = "Name: ${profile.Name ?: "N/A"}"
                        findViewById<TextView>(R.id.gender)?.text = "Gender: ${profile.Gender ?: "Unknown"}"
                        findViewById<TextView>(R.id.birthdate)?.text = "Birth Date: ${formatBirthDate(profile.Birth_date)}"
                        findViewById<TextView>(R.id.phone)?.text = "Phone: ${profile.Phone ?: "N/A"}"

                        profile.Img_profile?.let {
                            Glide.with(this@ProfileActivity).load(it).circleCrop().into(profilePic)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                Toast.makeText(this@ProfileActivity, "Error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun formatBirthDate(isoDate: String?): String {
        return try {
            val isoFormat = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val date = isoFormat.parse(isoDate ?: "")
            java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date!!)
        } catch (e: Exception) {
            "Invalid Date"
        }
    }
}
