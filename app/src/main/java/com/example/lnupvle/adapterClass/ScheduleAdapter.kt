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
import com.example.lnupvle.dataClass.ScheduleTempUser

class ScheduleAdapter (
    private val scheduleList: List<ScheduleTempUser>,
    private val scheduleNav: NavController,
    private val context: Context
    ) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.schedule_item, parent, false)

            return ScheduleViewHolder(view)
        }

        override fun getItemCount(): Int {
            return scheduleList.size
        }

        override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
            val schedule = scheduleList[position]
            holder.scheduleIdText.text = "Ідентифікатор розкладу: ${schedule.scheduleId}"
            holder.scheduleGroupText.text = "Група: ${schedule.scheduleGroup}"

            holder.scheduleCardLayout.setOnClickListener() {
                val userPref : SharedPreferences = context.getSharedPreferences("UserPref", Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor = userPref.edit()
                editor.putString("SID", schedule.scheduleId)
                editor.apply()
                scheduleNav.navigate(R.id.action_ScheduleMain_to_ScheduleDays)
            }
        }

        class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val scheduleIdText: TextView = itemView.findViewById(R.id.schedule_id_text)
            val scheduleGroupText: TextView = itemView.findViewById(R.id.schedule_group_text)
            val scheduleCardLayout: LinearLayout = itemView.findViewById(R.id.schedule_card_layout)
        }
}