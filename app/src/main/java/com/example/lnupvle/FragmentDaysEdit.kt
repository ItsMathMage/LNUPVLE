package com.example.lnupvle

import android.content.SharedPreferences
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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.PropertyName

class FragmentDaysEdit : Fragment() {

    private lateinit var scheduleNav: NavController
    private lateinit var userPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_days_edit, container, false)

        scheduleNav = findNavController()

        val dayCreateField = view.findViewById<EditText>(R.id.schedule_create_day)
        val dayDeleteField = view.findViewById<EditText>(R.id.schedule_delete_day)

        val dayCreateButton = view.findViewById<Button>(R.id.button_create_day)
        val dayDeleteButton = view.findViewById<Button>(R.id.button_delete_day)
        val toBackButton = view.findViewById<Button>(R.id.to_back_button)

        dayCreateButton.setOnClickListener() {
            val dayName = dayCreateField.text.toString()

            if (dayName.isEmpty()) {
                showToast("Введіть день для створення")
            } else {
                createDay(dayName)
            }
        }

        dayDeleteButton.setOnClickListener() {
            val dayName = dayDeleteField.text.toString()

            if (dayName.isEmpty()) {
                showToast("Введіть день для створення")
            } else {
                deleteDay(dayName)
            }
        }

        toBackButton.setOnClickListener() {
            scheduleNav.navigate(R.id.action_DaysEdit_to_ScheduleDays)
        }

        return view
    }

    private fun createDay(dayName: String) {
        try {
            userPref = requireActivity().getSharedPreferences("UserPref", android.content.Context.MODE_PRIVATE)
            val sid = userPref.getString("SID", "").toString()

            val databaseRef = FirebaseDatabase.getInstance().getReference("app")
            val scheduleRef = databaseRef.child("days").child(sid).child(dayName)

            val day = Day(dayName)
            scheduleRef.setValue(day)

            showToast("День успішно створено")

        } catch (e: Exception) {
            showToast("Помилка")
        }
    }

    private fun deleteDay(dayName: String) {
        try {
            userPref = requireActivity().getSharedPreferences("UserPref", android.content.Context.MODE_PRIVATE)
            val sid = userPref.getString("SID", "").toString()

            val databaseRef = FirebaseDatabase.getInstance().getReference("app")
            val scheduleRef = databaseRef.child("days").child(sid).child(dayName)

            scheduleRef.removeValue()

            showToast("День успішно видалено")

        } catch (e: Exception) {
            showToast("Помилка")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}