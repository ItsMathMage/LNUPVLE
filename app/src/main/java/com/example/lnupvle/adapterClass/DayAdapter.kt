package com.example.lnupvle.adapterClass

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.lnupvle.R
import com.example.lnupvle.dataClass.Day


data class DayAdapter (
    private val daysList: List<Day>,
    private val scheduleNav: NavController,
    private val context: Context
    ) : RecyclerView.Adapter<DayAdapter.DayViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.day_item, parent, false)

        return DayViewHolder(view)
    }

    override fun getItemCount(): Int {
        return daysList.size
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val day = daysList[position]
        holder.dayName.text = day.dayName

        holder.dayCardLayout.setOnClickListener() {
            val userPref : SharedPreferences = context.getSharedPreferences("UserPref", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = userPref.edit()
            editor.putString("DN", day.dayName)
            editor.apply()
            scheduleNav.navigate(R.id.action_ScheduleDays_to_DayLectures)
        }
    }

    class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayName: TextView = itemView.findViewById(R.id.day_schedule_text)
        val dayCardLayout: LinearLayout = itemView.findViewById(R.id.day_card_layout)
    }
}