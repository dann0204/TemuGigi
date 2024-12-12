package com.dicoding.myapplication



import Patient
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PatientAdapter(
    private val patients: List<Patient>,
    private val onDetailsClick: (Patient) -> Unit
) : RecyclerView.Adapter<PatientAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val patientName: TextView = itemView.findViewById(R.id.patientName)
        val diseaseName: TextView = itemView.findViewById(R.id.diseaseName)
        val actionButton: Button = itemView.findViewById(R.id.detailsButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_patient, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val patient = patients[position]
        holder.patientName.text = patient.name
        holder.diseaseName.text = patient.diseaseName
        holder.actionButton.setOnClickListener { onDetailsClick(patient) }
    }

    override fun getItemCount(): Int = patients.size
}
