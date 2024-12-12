import com.google.gson.annotations.SerializedName

data class CoassSchedule(
    @SerializedName("Schedule_id") val scheduleId: Int,
    @SerializedName("Appointment_date") val appointmentDate: String?,
    @SerializedName("Appointment_place") val appointmentPlace: String?,
    @SerializedName("Patient_name") val patientName: String,
    @SerializedName("Patient_phone") val patientPhone: String?,
    @SerializedName("Patient_gender") val patientGender: String?,
    @SerializedName("Img_disease") val imgDisease: String?,
    @SerializedName("Disease_name") val diseaseName: String?,
    @SerializedName("Description") val description: String?
)
