package com.example.lnupvle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentScheduleEdit : Fragment() {

    private lateinit var scheduleNav: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_schedule_edit, container, false)

        scheduleNav = findNavController()

        val scheduleIdField = view.findViewById<EditText>(R.id.schedule_create_id)
        val scheduleGroupField = view.findViewById<EditText>(R.id.schedule_create_group)
        val scheduleIdDelete = view.findViewById<EditText>(R.id.schedule_delete_id)

        val createIdButton = view.findViewById<Button>(R.id.button_create_schedule)
        val deleteIdButton = view.findViewById<Button>(R.id.button_delete_schedule)
        val toBackButton = view.findViewById<Button>(R.id.to_back_button)

        createIdButton.setOnClickListener() {
            val scheduleId = scheduleIdField.text.toString()
            val scheduleGroup = scheduleGroupField.text.toString()

            if (scheduleId.isEmpty() or scheduleGroup.isEmpty()) {
                showToast("Заповніть поля для створення")
            } else {
                createSchedule(scheduleId, scheduleGroup)
            }
        }

        deleteIdButton.setOnClickListener() {
            val scheduleID = scheduleIdDelete.text.toString()

            if (scheduleID.isEmpty()) {
                showToast("Заповніть полe для видалення")
            } else {
                deleteSchedule(scheduleID)
            }
        }

        toBackButton.setOnClickListener() {
            scheduleNav.navigate(R.id.action_ScheduleEdit_to_ScheduleMain)
        }

        return view
    }

    private fun createSchedule(scheduleId: String, scheduleGroup: String) {
        try {
            val databaseRef = FirebaseDatabase.getInstance().getReference("app")
            val scheduleRef = databaseRef.child("schedule").child(scheduleId)

            val scheduleData = Schedule(scheduleId, scheduleGroup)
            scheduleRef.setValue(scheduleData)

            showToast("Розклад успішно створено")
            scheduleNav.navigate(R.id.action_ScheduleEdit_to_ScheduleMain)

        } catch (e: Exception) {
            showToast("Помилка")
        }
    }

    private fun deleteSchedule (scheduleId: String) {
        try {
            val databaseRef = FirebaseDatabase.getInstance().getReference("app")
            val scheduleRef = databaseRef.child("schedule").child(scheduleId)

            scheduleRef.removeValue()

            showToast("Розклад успішно видалено")
            scheduleNav.navigate(R.id.action_ScheduleEdit_to_ScheduleMain)

        } catch (e: Exception) {
            showToast("Помилка: ${e.message}")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}