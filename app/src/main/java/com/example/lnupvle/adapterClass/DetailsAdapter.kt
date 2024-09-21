package com.example.lnupvle.adapterClass

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.lnupvle.R
import com.example.lnupvle.dataClass.Details

class DetailsAdapter (
    private val detailsList: List<Details>,
    private val scheduleNav: NavController,
    private val context: Context
) : RecyclerView.Adapter<DetailsAdapter.DetailsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.day_lecture_item, parent, false)

        return DetailsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return detailsList.size
    }

    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {
        val details = detailsList[position]
        holder.detailsName.text = "Предмет: ${details.detailsName}"
        holder.detailsTeacher.text = "Викладач: ${details.detailsTeacher}"
        holder.detailsTime.text = "Час: ${details.detailsTime}"
        holder.detailsPlace.text = "Аудиторія: ${details.detailsPlace}"
        holder.detailsLink.text = "Посилання: ${details.detailsLink}"
    }

    class DetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val detailsName: TextView = itemView.findViewById(R.id.lecture_name)
        val detailsTeacher: TextView = itemView.findViewById(R.id.lecture_teacher)
        val detailsTime: TextView = itemView.findViewById(R.id.lecture_time)
        val detailsPlace: TextView = itemView.findViewById(R.id.lecture_place)
        val detailsLink: TextView = itemView.findViewById(R.id.lecture_link)
    }
}