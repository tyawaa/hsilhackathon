package com.example.hsilhackathon

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hsilhackathon.data.entity.JournalEntity
import com.google.android.material.chip.Chip

class JournalAdapter(private var journals: List<JournalEntity>) :
    RecyclerView.Adapter<JournalAdapter.JournalViewHolder>() {

    class JournalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvSourceDate: TextView = itemView.findViewById(R.id.tvSourceDate)
        val chipCategoryLabel: Chip = itemView.findViewById(R.id.chipCategoryLabel)
        val tvSyncMarker: TextView = itemView.findViewById(R.id.tvSyncMarker)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JournalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_journal, parent, false)
        return JournalViewHolder(view)
    }

    override fun onBindViewHolder(holder: JournalViewHolder, position: Int) {
        val journal = journals[position]

        holder.tvTitle.text = journal.title
        holder.tvSourceDate.text = "${journal.source} • ${journal.date}"
        holder.chipCategoryLabel.text = journal.category

        if (journal.isFromSync) {
            holder.tvSyncMarker.visibility = View.VISIBLE
        } else {
            holder.tvSyncMarker.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, JournalDetailActivity::class.java).apply {
                putExtra("EXTRA_TITLE", journal.title)
                putExtra("EXTRA_SOURCE", journal.source)
                putExtra("EXTRA_DATE", journal.date)
                putExtra("EXTRA_CATEGORY", journal.category)
                putExtra("EXTRA_CONTENT", journal.content)
                putExtra("EXTRA_URL", journal.contentUrl)
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return journals.size
    }

    fun updateData(newJournals: List<JournalEntity>) {
        journals = newJournals
        notifyDataSetChanged()
    }
}
