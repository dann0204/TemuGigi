package com.dicoding.myapplication

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
class PendingRequestsAdapter(
    private val requests: List<PatientRequest>,
    private val context: Context
) : RecyclerView.Adapter<PendingRequestsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val patientName: TextView = itemView.findViewById(R.id.patientName)
        val patientDisease: TextView = itemView.findViewById(R.id.patientDisease)
        val requestedAt: TextView = itemView.findViewById(R.id.requestedAt)
        val diseaseImage: ImageView = itemView.findViewById(R.id.diseaseImage)
        val processButton: Button = itemView.findViewById(R.id.processButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pending_request, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val patientRequest = requests[position]

        holder.patientName.text = patientRequest.name
        holder.patientDisease.text = patientRequest.diseaseName
        holder.requestedAt.text = "Requested At: ${patientRequest.requestedAt}"

        Glide.with(context)
            .load(patientRequest.imageUrl)
            .placeholder(R.drawable.ic_profile_placeholder) // Replace with your drawable
            .into(holder.diseaseImage)

        holder.processButton.setOnClickListener {
            val intent = Intent(context, RequestProcessingActivity::class.java)
            intent.putExtra("REQUEST_ID", patientRequest.requestId.toString())
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = requests.size
}

