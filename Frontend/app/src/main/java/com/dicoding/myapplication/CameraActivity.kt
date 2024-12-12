package com.dicoding.myapplication

import Coass
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException

class CameraActivity : AppCompatActivity() {

    private val CAMERA_REQUEST_CODE = 100
    private val GALLERY_REQUEST_CODE = 101
    private lateinit var currentPhotoPath: String

    private lateinit var btnCamera: ImageButton
    private lateinit var selectedImageView: ImageView
    private lateinit var resultTextView: TextView
    private lateinit var btnSubmit: Button
    private lateinit var btnRequestMeeting: Button
    private lateinit var profileWidget: FrameLayout

    private lateinit var token: String
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        // Retrieve token from SharedPreferences
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("token", "") ?: ""


        // Initialize UI components
        btnCamera = findViewById(R.id.btn_camera)
        selectedImageView = findViewById(R.id.selected_image)
        resultTextView = findViewById(R.id.result_text)
        btnSubmit = findViewById(R.id.btn_submit)
        btnRequestMeeting = findViewById(R.id.btn_request_meeting) // Add a new button
        profileWidget = findViewById(R.id.floating_profile)

        // Initially hide the "Request Meeting" button
        btnRequestMeeting.visibility = Button.GONE

        // Add click listener for the profile widget
        profileWidget.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        // Add click listener for the camera button
        btnCamera.setOnClickListener {
            showImageSourceDialog()
        }

        // Handle the submit button click
        btnSubmit.setOnClickListener {
            selectedImageUri?.let {
                uploadImageToBackend(it)
            } ?: run {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle the request meeting button click
        btnRequestMeeting.setOnClickListener {
            fetchCoassList(token)
        }
        // Initialize and set click listener for the "My Schedule" button
        val btnMySchedule: Button = findViewById(R.id.btn_my_schedule)
        btnMySchedule.setOnClickListener {
            val intent = Intent(this, MyScheduleActivity::class.java)
            startActivity(intent)
        }

    }

    private fun showImageSourceDialog() {
        val options = arrayOf("Open Camera", "Choose from Gallery")
        val builder = AlertDialog.Builder(this)
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
                val photoURI: Uri = FileProvider.getUriForFile(
                    this,
                    "com.dicoding.myapplication.fileprovider",
                    it
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
            }
        }
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
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val photoUri: Uri = Uri.fromFile(File(currentPhotoPath))
            selectedImageUri = photoUri
            Glide.with(this).load(photoUri).into(selectedImageView)
        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val selectedImage = data?.data
            selectedImageUri = selectedImage
            Glide.with(this).load(selectedImage).into(selectedImageView)
        }
    }

    private fun uploadImageToBackend(imageUri: Uri) {
        val imageFile = File(getRealPathFromURI(imageUri))
        val mimeType = contentResolver.getType(imageUri) ?: "image/jpeg"
        val requestFile: RequestBody = RequestBody.create(mimeType.toMediaTypeOrNull(), imageFile)
        val body = MultipartBody.Part.createFormData("Img_disease", imageFile.name, requestFile)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://temugigi-backend-302773936528.asia-southeast2.run.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        val call: Call<PredictionResponse> = apiService.predictImage("Bearer $token", body)
        call.enqueue(object : Callback<PredictionResponse> {
            override fun onResponse(call: Call<PredictionResponse>, response: Response<PredictionResponse>) {
                if (response.isSuccessful) {
                    val predictionResponse = response.body()
                    predictionResponse?.let {
                        resultTextView.text = """
                            Disease: ${it.disease_name}
                            Confidence: ${it.confidence}
                            Description: ${it.description}
                        """.trimIndent()

                        // Toggle buttons based on response
                        btnSubmit.visibility = Button.GONE
                        btnRequestMeeting.visibility = Button.VISIBLE
                    } ?: run {
                        resultTextView.text = "Error: Unable to parse response."
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("Upload Error", "Error body: $errorBody")
                    resultTextView.text = "No teeth disease detected, please try another image."
                    btnSubmit.visibility = Button.VISIBLE
                    btnRequestMeeting.visibility = Button.GONE
                }
            }

            override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                resultTextView.text = "Error: ${t.localizedMessage}"
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

    private fun fetchCoassList(token: String) {
        // Make the API call to fetch the co-ass list
        val retrofit = Retrofit.Builder()
            .baseUrl("https://temugigi-backend-302773936528.asia-southeast2.run.app") // Replace with your actual API base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        apiService.getCoAssList("Bearer $token").enqueue(object : Callback<List<Coass>> {
            override fun onResponse(call: Call<List<Coass>>, response: Response<List<Coass>>) {
                if (response.isSuccessful) {
                    val coassList = response.body()
                    coassList?.let {
                        // Start ListCoassActivity and pass the co-ass list
                        // In your CameraActivity
                        val intent = Intent(this@CameraActivity, ListCoAssActivity::class.java)
                        intent.putParcelableArrayListExtra("coassList", ArrayList(coassList))
                        startActivity(intent)

                    }
                } else {
                    Toast.makeText(this@CameraActivity, "Failed to load co-ass list", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Coass>>, t: Throwable) {
                Toast.makeText(this@CameraActivity, "Error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
