package com.example.lnupvle.ui.fragments.schedule

import android.content.SharedPreferences
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
import com.example.lnupvle.dataClass.Details
import com.example.lnupvle.utilits.showToast
import com.google.firebase.database.FirebaseDatabase

class FragmentDetailsEdit : Fragment() {

    private lateinit var scheduleNav: NavController
    private lateinit var userPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_details_edit, container, false)

        scheduleNav = findNavController()

        val detailsNameField = view.findViewById<EditText>(R.id.details_name)
        val detailsTeacherField = view.findViewById<EditText>(R.id.details_teacher)
        val detailsTimeField = view.findViewById<EditText>(R.id.details_time)
        val detailsPlaceField = view.findViewById<EditText>(R.id.details_place)
        val detailsLinkField = view.findViewById<EditText>(R.id.details_link)
        val deleteNameField = view.findViewById<EditText>(R.id.delete_name)

        val createDetailsButton = view.findViewById<Button>(R.id.button_create_details)
        val deleteDetailsButton = view.findViewById<Button>(R.id.button_delete_details)
        val toBackButton = view.findViewById<Button>(R.id.to_back_button)

        createDetailsButton.setOnClickListener() {
            val detailsName = detailsNameField.text.toString()
            val detailsTeacher = detailsTeacherField.text.toString()
            val detailsTime = detailsTimeField.text.toString()
            val detailsPlace = detailsPlaceField.text.toString()
            var detailsLink = detailsLinkField.text.toString()

            if (detailsName.isEmpty() or detailsTeacher.isEmpty() or detailsTime.isEmpty() or detailsPlace.isEmpty()) {
                showToast("Введіть усі обов'язкові дані")
            } else {
                if (detailsLink.isEmpty()) {
                    detailsLink = "-"
                }
                createDetails(detailsName, detailsTeacher, detailsTime, detailsPlace, detailsLink)
            }
        }

        deleteDetailsButton.setOnClickListener() {
            val deleteName = deleteNameField.text.toString()

            if (deleteName.isEmpty()) {
                showToast("Введіть дані для видалення")
            } else {
                deleteDetail(deleteName)
            }
        }

        toBackButton.setOnClickListener() {
            scheduleNav.navigate(R.id.action_DetailsEdit_to_DayLectures)
        }

        return view
    }

    private fun createDetails(
        detailsName: String,
        detailsTeacher: String,
        detailsTime: String,
        detailsPlace: String,
        detailsLink: String) {

        try {
            userPref = requireActivity().getSharedPreferences("UserPref", android.content.Context.MODE_PRIVATE)
            val dayName = userPref.getString("DN", "").toString()
            val scheduleId = userPref.getString("SID", "").toString()

            val databaseRef = FirebaseDatabase.getInstance().getReference("app")
            val scheduleRef = databaseRef.child("details").child(scheduleId).child(dayName).child(detailsName)

            val details = Details(detailsName.toString(), detailsTeacher, detailsTime, detailsPlace, detailsLink)
            scheduleRef.setValue(details)

            showToast("Деталі розкладу успішно додані")

        } catch (e: Exception) {
            showToast("Помилка: ${e.message}")
        }
    }

    private fun deleteDetail(deleteName: String) {
        userPref = requireActivity().getSharedPreferences("UserPref", android.content.Context.MODE_PRIVATE)
        val dayName = userPref.getString("DN", "").toString()
        val scheduleId = userPref.getString("SID", "").toString()

        val databaseRef = FirebaseDatabase.getInstance().getReference("app")
        val scheduleRef = databaseRef.child("details").child(scheduleId).child(dayName).child(deleteName)

        scheduleRef.removeValue()
    }
}