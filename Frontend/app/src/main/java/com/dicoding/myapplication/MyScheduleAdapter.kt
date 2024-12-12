package com.dicoding.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MyScheduleAdapter(private val scheduleList: List<MyPatientSchedule>) :
    RecyclerView.Adapter<MyScheduleAdapter.MyScheduleViewHolder>() {

    inner class MyScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coassImage: ImageView = itemView.findViewById(R.id.coassImage)
        val coassName: TextView = itemView.findViewById(R.id.coassName)
        val appointmentDate: TextView = itemView.findViewById(R.id.appointmentDate)
        val appointmentPlace: TextView = itemView.findViewById(R.id.appointmentPlace)
        val diseaseName: TextView = itemView.findViewById(R.id.diseaseName)
        val description: TextView = itemView.findViewById(R.id.description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyScheduleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_my_schedule, parent, false)
        return MyScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyScheduleViewHolder, position: Int) {
        val schedule = scheduleList[position]
        holder.coassName.text = schedule.coassName
        holder.appointmentDate.text = "Date: ${schedule.appointmentDate ?: "N/A"}"
        holder.appointmentPlace.text = "Place: ${schedule.appointmentPlace ?: "N/A"}"
        holder.diseaseName.text = "Disease: ${schedule.diseaseName}"
        holder.description.text = schedule.description

        Glide.with(holder.itemView.context)
            .load(schedule.coassImg ?: R.drawable.ic_profile_placeholder) // Replace with placeholder image
            .into(holder.coassImage)
    }

    override fun getItemCount(): Int = scheduleList.size
}
