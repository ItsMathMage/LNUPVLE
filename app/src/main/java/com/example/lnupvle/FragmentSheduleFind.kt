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

class FragmentSheduleFind : Fragment() {

    private lateinit var scheduleNav: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shedule_find, container, false)

        scheduleNav = findNavController()

        val scheduleIdField = view.findViewById<EditText>(R.id.schedule_id_field)

        val toBackButton = view.findViewById<Button>(R.id.to_back_button)
        val findScheduleButton = view.findViewById<Button>(R.id.find_schedule_button)

        toBackButton.setOnClickListener() {
            scheduleNav.navigate(R.id.action_ScheduleFind_to_ScheduleMain)
        }

        findScheduleButton.setOnClickListener() {
            val scheduleId = scheduleIdField.text.toString()

            if (scheduleId.isEmpty()) {
                showToast("Заповніть поле")
            } else {
                addSchedule(scheduleId)
            }
        }

        return view
    }

    private fun addSchedule(sheduleId: String) {
        try {
            val databaseRef = FirebaseDatabase.getInstance().getReference("app")
            val scheduleRef = databaseRef.child("schedule").child(sheduleId)

            scheduleRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val schedule = dataSnapshot.getValue(Schedule::class.java)

                        if (schedule != null) {
                            val userPref = requireActivity().getSharedPreferences("UserPref", android.content.Context.MODE_PRIVATE)
                            val uid = userPref.getString("UID", "").toString()
                            val userScheduleRef = databaseRef.child("schedule_reference").child(uid).child(sheduleId)
                            val scheduleTemp = ScheduleTempUser(sheduleId, schedule.scheduleGroup)
                            userScheduleRef.setValue(scheduleTemp)
                            showToast("Розклад додано успішно")
                            scheduleNav.navigate(R.id.action_ScheduleFind_to_ScheduleMain)
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
        } catch (e: Exception) {
            showToast("Помилка: ${e.message}")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}