import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.myapplication.R
import com.dicoding.myapplication.Schedule

class CoassScheduleAdapter(
    private val schedules: List<CoassSchedule>
) : RecyclerView.Adapter<CoassScheduleAdapter.ScheduleViewHolder>() {

    class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val patientName: TextView = itemView.findViewById(R.id.patientName)
        val appointmentDate: TextView = itemView.findViewById(R.id.appointmentDate)
        val appointmentPlace: TextView = itemView.findViewById(R.id.appointmentPlace)
        val diseaseName: TextView = itemView.findViewById(R.id.diseaseName)
        val description: TextView = itemView.findViewById(R.id.description)
        val diseaseImage: ImageView = itemView.findViewById(R.id.diseaseImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_coass_schedule, parent, false)
        return ScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val schedule = schedules[position]

        holder.patientName.text = schedule.patientName
        holder.appointmentDate.text = "Date: ${schedule.appointmentDate}"
        holder.appointmentPlace.text = "Place: ${schedule.appointmentPlace}"
        holder.diseaseName.text = schedule.diseaseName
        holder.description.text = schedule.description

        Glide.with(holder.itemView.context)
            .load(schedule.imgDisease)
            .placeholder(R.drawable.ic_profile_placeholder) // Ensure placeholder exists
            .into(holder.diseaseImage)
    }

    override fun getItemCount(): Int = schedules.size
}
