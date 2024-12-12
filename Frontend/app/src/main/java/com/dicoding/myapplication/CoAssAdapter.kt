package com.dicoding.myapplication

import Coass
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
class CoAssAdapter(
    private var coassList: List<Coass>,
    private val onItemClick: (Coass) -> Unit
) : RecyclerView.Adapter<CoAssAdapter.CoAssViewHolder>() {

    fun updateList(newList: List<Coass>) {
        coassList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoAssViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_coass, parent, false)
        return CoAssViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: CoAssViewHolder, position: Int) {
        val coass = coassList[position]

        // Bind data to views
        holder.coassName.text = coass.name
        holder.coassPlace.text = coass.appointmentPlace

        // Load profile image (if available)
        if (coass.imgProfile != null) {
            Glide.with(holder.itemView.context)
                .load(coass.imgProfile)
                .placeholder(R.drawable.ic_profile_placeholder)
                .into(holder.coassImage)
        } else {
            holder.coassImage.setImageResource(R.drawable.ic_profile_placeholder)
        }

        // Handle button click to trigger the callback
        holder.selectCoAssButton.setOnClickListener {
            onItemClick(coass) // Pass the selected Coass to the callback
        }
    }

    override fun getItemCount(): Int {
        return coassList.size
    }

    inner class CoAssViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coassImage: ImageView = itemView.findViewById(R.id.coassImage)
        val coassName: TextView = itemView.findViewById(R.id.coassName)
        val coassPlace: TextView = itemView.findViewById(R.id.coassPlace)
        val selectCoAssButton: Button = itemView.findViewById(R.id.selectCoAssButton)
    }
}
