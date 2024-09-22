package com.example.lnupvle.ui.fragments.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.lnupvle.R
import com.example.lnupvle.dataClass.Access
import com.example.lnupvle.dataClass.Schedule
import com.example.lnupvle.dataClass.ScheduleTempUser
import com.example.lnupvle.utilits.navigate
import com.example.lnupvle.utilits.showToast
import com.google.android.material.color.ColorRoles
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FragmentScheduleEdit : Fragment() {
    private lateinit var databaseRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_schedule_edit, container, false)

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
                createAccess(scheduleId, scheduleGroup, "creator")
                navigate(R.id.action_ScheduleEdit_to_ScheduleMain)
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
            navigate(R.id.action_ScheduleEdit_to_ScheduleMain)
        }

        return view
    }

    private fun createAccess(scheduleId: String, scheduleGroup: String, scheduleRole: String) {
        val userPref = requireActivity().getSharedPreferences("UserPref", android.content.Context.MODE_PRIVATE)
        databaseRef = FirebaseDatabase.getInstance().getReference("app")
        val uid = userPref.getString("UID", "").toString()
        val accessRef = databaseRef.child("schedule_reference").child(uid).child(scheduleId)
        val access = ScheduleTempUser(scheduleId, scheduleGroup, scheduleRole)
        accessRef.setValue(access)
    }

    private fun createSchedule(scheduleId: String, scheduleGroup: String) {
        try {
            databaseRef = FirebaseDatabase.getInstance().getReference("app")
            val scheduleRef = databaseRef.child("schedule").child(scheduleId)

            val scheduleData = Schedule(scheduleId, scheduleGroup)
            scheduleRef.setValue(scheduleData)

            showToast("Розклад успішно створено")

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
            navigate(R.id.action_ScheduleEdit_to_ScheduleMain)

        } catch (e: Exception) {
            showToast("Помилка: ${e.message}")
        }
    }
}