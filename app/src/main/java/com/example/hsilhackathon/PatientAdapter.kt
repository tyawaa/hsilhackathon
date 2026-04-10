package com.example.hsilhackathon

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hsilhackathon.data.entity.PatientEntity

class PatientAdapter(
    private var patients: List<PatientEntity>,
    private val onItemClick: (PatientEntity) -> Unit
) : RecyclerView.Adapter<PatientAdapter.PatientViewHolder>() {

    class PatientViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvInitial: TextView = view.findViewById(R.id.tvPatientInitial)
        val tvName: TextView = view.findViewById(R.id.tvPatientName)
        val tvNik: TextView = view.findViewById(R.id.tvPatientNik)
        val tvLastDiagnosis: TextView = view.findViewById(R.id.tvPatientLastDiagnosis)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_patient, parent, false)
        return PatientViewHolder(view)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        val patient = patients[position]
        holder.tvInitial.text = patient.namaLengkap.firstOrNull()?.uppercase() ?: "?"
        holder.tvName.text = patient.namaLengkap
        holder.tvNik.text = "NIK: ${patient.nik}"

        if (patient.lastDiagnosis.isNotEmpty()) {
            holder.tvLastDiagnosis.visibility = View.VISIBLE
            holder.tvLastDiagnosis.text = "Diagnosis: ${patient.lastDiagnosis}"
        } else {
            holder.tvLastDiagnosis.visibility = View.GONE
        }

        holder.itemView.setOnClickListener { onItemClick(patient) }
    }

    override fun getItemCount() = patients.size

    fun updateData(newPatients: List<PatientEntity>) {
        patients = newPatients
        notifyDataSetChanged()
    }
}
