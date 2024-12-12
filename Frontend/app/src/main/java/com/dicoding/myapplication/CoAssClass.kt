import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Coass(
    @SerializedName("Id") val id: String,
    @SerializedName("Name") val name: String,
    @SerializedName("Gender") val gender: String,
    @SerializedName("University") val university: String,
    @SerializedName("Appointment_Place") val appointmentPlace: String,
    @SerializedName("Phone") val phone: String,
    @SerializedName("Img_profile") val imgProfile: String?,
    @SerializedName("Email") val email: String?,
    @SerializedName("Birth_Date") val birthDate: String?
) : Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(gender)
        parcel.writeString(university)
        parcel.writeString(appointmentPlace)
        parcel.writeString(phone)
        parcel.writeString(imgProfile)
        parcel.writeString(email)
        parcel.writeString(birthDate)
    }


    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Coass> {

        override fun createFromParcel(parcel: Parcel): Coass {
            return Coass(parcel)
        }

        // Method to create an array of Coass objects
        override fun newArray(size: Int): Array<Coass?> {
            return arrayOfNulls(size)
        }
    }
}
