package com.dicoding.myapplication

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AcceptedRequestsAdapter(
    private val requests: List<PatientRequest>,
    private val onScheduleClick: (PatientRequest) -> Unit
) : RecyclerView.Adapter<AcceptedRequestsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val patientName: TextView = itemView.findViewById(R.id.patientName)
        val diseaseName: TextView = itemView.findViewById(R.id.diseaseName)
        val requestedAt: TextView = itemView.findViewById(R.id.requestedAt)
        val scheduleButton: Button = itemView.findViewById(R.id.scheduleButton)
        val diseaseImage: ImageView = itemView.findViewById(R.id.diseaseImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_accepted_request, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val request = requests[position]
        holder.patientName.text = request.name
        holder.diseaseName.text = request.diseaseName
        holder.requestedAt.text = "Requested At: ${request.requestedAt}"

        Glide.with(holder.itemView.context)
            .load(request.imageUrl)
            .placeholder(R.drawable.ic_profile_placeholder)
            .into(holder.diseaseImage)

        holder.scheduleButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, ScheduleAcceptMeetingActivity::class.java)
            intent.putExtra("REQUEST_ID", request.requestId)
            intent.putExtra("PATIENT_NAME", request.name)
            intent.putExtra("DISEASE_NAME", request.diseaseName)
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int = requests.size
}
