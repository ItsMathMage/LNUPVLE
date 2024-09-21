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
import com.example.lnupvle.dataClass.Access

class LessonAdapter(
    private val lessonsList: List<Access>,
    private val frameNav: NavController,
    private val context: Context
) : RecyclerView.Adapter<LessonAdapter.LessonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lesson_item, parent, false)

        return LessonViewHolder(view)
    }

    override fun getItemCount(): Int {
        return lessonsList.size
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        val lesson = lessonsList[position]
        holder.lessonNameText.text = "Назва предмету: ${lesson.lessonName}"
        holder.lessonTeacherText.text = "Викладач: ${lesson.teacherName}"

        holder.cardLayout.setOnClickListener() {
            val userPref : SharedPreferences = context.getSharedPreferences("UserPref", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = userPref.edit()
            editor.putString("LID", lesson.lessonId)
            editor.apply()
            frameNav.navigate(R.id.action_Start_to_Lesson)
        }
    }

    class LessonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val lessonNameText: TextView = itemView.findViewById(R.id.lesson_name_text)
        val lessonTeacherText: TextView = itemView.findViewById(R.id.lesson_teacher_text)
        val cardLayout: LinearLayout = itemView.findViewById(R.id.card_layout)

    }

}