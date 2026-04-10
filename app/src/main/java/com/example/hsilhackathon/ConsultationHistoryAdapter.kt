package com.example.hsilhackathon

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hsilhackathon.data.entity.ConsultationEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ConsultationHistoryAdapter(private val historyList: List<ConsultationEntity>) :
    RecyclerView.Adapter<ConsultationHistoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView = view.findViewById(R.id.tvHistoryDate)
        val tvCaseId: TextView = view.findViewById(R.id.tvHistoryCaseId)
        val tvDoctorName: TextView = view.findViewById(R.id.tvHistoryDoctorName)
        val tvDiagnosis: TextView = view.findViewById(R.id.tvHistoryDiagnosis)
        val tvTreatment: TextView = view.findViewById(R.id.tvHistoryTreatment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_consultation_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = historyList[position]
        
        // Format Date
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
        val dateString = sdf.format(Date(item.dateTimestamp))
        
        holder.tvDate.text = dateString
        holder.tvCaseId.text = "#${item.caseId}"
        holder.tvDoctorName.text = "Spesialis: ${item.doctorName}"
        
        // Determine match or mismatch for display
        val matchText = if (item.feedbackStatus == "Match") "(Sesuai AI)" else "(Koreksi AI)"
        holder.tvDiagnosis.text = "Diagnosis: ${item.specialistDiagnosis} $matchText"
        
        holder.tvTreatment.text = item.treatmentRecommendation
    }

    override fun getItemCount() = historyList.size
}
