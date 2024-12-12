package com.dicoding.myapplication

import Coass
import CoassSchedule
import Patient
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import com.google.gson.annotations.SerializedName
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

// Data classes remain the same

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(
    val token: String?,
    val role: String?  // This will be either "patient" or "coass"
)


data class RegisterCoAssRequest(
    val email: String,
    val password: String,
    val name: String,
    val gender: String,
    val birth_date: String,
    val ktp: String,
    val nim: String,
    val appointment_place: String,
    val university: String,
    val phone: String
)

data class RegisterPatientRequest(
    val email: String,
    val password: String,
    val name: String,
    val gender: String,
    val birth_date: String,
    val city: String,
    val phone: String
)

data class RegisterResponse(
    val success: Boolean,
    val message: String
)

data class ForgotPasswordRequest(
    val email: String
)

data class ForgotPasswordResponse(
    val success: Boolean,
    val message: String
)

interface ApiService {
    @POST("/registercoass")
    fun registerCoAss(@Body registerRequest: RegisterCoAssRequest): Call<RegisterResponse>

    @POST("/registerpatient")
    fun registerPatient(@Body registerRequest: RegisterPatientRequest): Call<RegisterResponse>

    @POST("/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("/forgot-password")
    fun forgotPassword(@Body forgotPasswordRequest: ForgotPasswordRequest): Call<ForgotPasswordResponse>

    @Multipart
    @POST("/predict")
    fun predictImage(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part
    ): Call<PredictionResponse>

    @GET("/profile")
    fun getProfile(@Header("Authorization") token: String): Call<UserProfileResponse>
    @PUT("/profile")
    fun updateProfile(
        @Header("Authorization") token: String,
        @Body userProfileRequest: UserProfileRequest
    ): Call<UserProfileResponse>
    @Multipart
    @PUT("/profile-picture")
    fun updateProfilePicture(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part
    ): Call<UpdateProfilePictureResponse>
    @PUT("/change-password")
    fun changePassword(
        @Header("Authorization") token: String,
        @Body changePasswordRequest: ChangePasswordRequest
    ): Call<ChangePasswordResponse>
    @GET("/list-coass")
    fun getCoAssList(@Header("Authorization") token: String): Call<List<Coass>>
    @POST("/request-meeting")
    fun requestMeeting(
        @Header("Authorization") authorization: String,
        @Body requestBody: Map<String, String> // or a data class if you prefer
    ): Call<Void>

    @GET("/pending-requests")
    fun getPendingRequests(
        @Header("Authorization") authorization: String
    ): Call<List<PatientRequest>>
    @GET("/list-patients")
    fun getPatients(
        @Header("Authorization") token: String,
        @Query("request_id") requestId: String
    ): Call<List<Patient>>

    @PUT("/review-request")
    fun reviewRequest(
        @Header("Authorization") token: String,
        @Body requestBody: Map<String, String>
    ): Call<ResponseBody>

    @GET("/accepted-requests")
    fun getAcceptedRequests(
        @Header("Authorization") token: String
    ): Call<List<PatientRequest>>
    @POST("/schedule-meeting")
    fun scheduleMeeting(
        @Header("Authorization") token: String,
        @Body requestBody: Map<String, String>
    ): Call<ResponseBody>

    @GET("/my-schedule")
    fun getMySchedule(
        @Header("Authorization") token: String
    ): Call<List<MyPatientSchedule>>
    @GET("/coass-schedule")
    fun getCoassSchedule(
        @Header("Authorization") token: String
    ): Call<List<CoassSchedule>>





}
