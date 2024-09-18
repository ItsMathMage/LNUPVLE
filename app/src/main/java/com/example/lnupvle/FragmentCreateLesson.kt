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

class FragmentCreateLesson : Fragment() {

    private lateinit var frameNav: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_lesson, container, false)

        frameNav = findNavController()

        val lessonNameField = view.findViewById<EditText>(R.id.lesson_name_field)
        val lessonInfoField = view.findViewById<EditText>(R.id.lesson_info_field)
        val lessonIdField = view.findViewById<EditText>(R.id.lesson_id_field)
        val lessonGroupField = view.findViewById<EditText>(R.id.group_name_field)
        val lessonPasswordField = view.findViewById<EditText>(R.id.password_lesson_field)
        val lessonRepeatField = view.findViewById<EditText>(R.id.repeat_lesson_field)

        val toBackButton = view.findViewById<Button>(R.id.to_back_button)
        val createLessonButton = view.findViewById<Button>(R.id.create_lesson_button)

        toBackButton.setOnClickListener() {
            frameNav.navigate(R.id.action_CreateLesson_to_Start)
        }

        createLessonButton.setOnClickListener() {

            val lessonName = lessonNameField.text.toString()
            val lessonInfo = lessonInfoField.text.toString()
            val lessonId = lessonIdField.text.toString()
            val lessonGroup = lessonGroupField.text.toString()
            val lessonPassword = lessonPasswordField.text.toString()
            val lessonRepeat = lessonRepeatField.text.toString()

            if ((listOf(lessonName, lessonInfo, lessonId, lessonGroup, lessonPassword, lessonRepeat).any { it.isEmpty() })) {
                showToast("Заповніть усі поля")
            } else if (lessonPassword != lessonRepeat) {
                showToast("Паролі не співпадають")
            } else {
                createLesson(lessonName, lessonInfo, lessonId, lessonGroup, lessonPassword)
            }
        }

        return view
    }

    private fun createLesson (lessonName: String, lessonInfo: String, lessonId: String, lessonGroup: String, lessonPassword: String) {
        try {
            val userPref = requireActivity().getSharedPreferences("UserPref", android.content.Context.MODE_PRIVATE)
            val uid = userPref.getString("UID", "").toString()

            val databaseRef = FirebaseDatabase.getInstance().getReference("app")
            val userRef = databaseRef.child("users").child(uid)
            val lessonRef = databaseRef.child("lessons").child(lessonId)

            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val user = dataSnapshot.getValue(User::class.java)

                        if (user != null) {
                            val lessonTeacher = "${user.firstname} ${user.lastname}"
                            val lessonData = Lesson(lessonName, lessonInfo, lessonId, lessonGroup, lessonPassword, lessonTeacher)

                            lessonRef.setValue(lessonData)

                            showToast("Предмет успішно створено")
                            frameNav.navigate(R.id.action_CreateLesson_to_Start)

                        }
                    } else {

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