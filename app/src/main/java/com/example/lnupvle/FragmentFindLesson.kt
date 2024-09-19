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

class FragmentFindLesson : Fragment() {

    private lateinit var frameNav: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_find_lesson, container, false)

        frameNav = findNavController()

        val lessonIdField = view.findViewById<EditText>(R.id.lesson_id_field)
        val lessonPasswordField = view.findViewById<EditText>(R.id.lesson_password_field)

        val toBackButton = view.findViewById<Button>(R.id.to_back_button)
        val findLessonButton = view.findViewById<Button>(R.id.find_lesson_button)

        toBackButton.setOnClickListener() {
            frameNav.navigate(R.id.action_FindLesson_to_Start)
        }

        findLessonButton.setOnClickListener() {
            val lessonId = lessonIdField.text.toString()
            val lessonPassword = lessonPasswordField.text.toString()

            if (lessonId.isEmpty() || lessonPassword.isEmpty()) {
                showToast("Введіть усі дані")
            } else {
                addLesson(lessonId, lessonPassword)
            }

        }

        return view
    }

    private fun addLesson (lessonId: String, lessonPassword: String) {
        try {
            val userPref = requireActivity().getSharedPreferences("UserPref", android.content.Context.MODE_PRIVATE)
            val uid = userPref.getString("UID", "").toString()

            val databaseRef = FirebaseDatabase.getInstance().getReference("app")
            val lessonRef = databaseRef.child("lessons").child(lessonId)

            lessonRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val lesson = dataSnapshot.getValue(Lesson::class.java)

                        if (lesson != null) {
                            if (lessonPassword == lesson.lessonPassword) {
                                val userAccessRef = databaseRef.child("access").child(uid).child(lessonId)
                                val access = Access(uid, lessonId, lesson.lessonName, lesson.lessonTeacher)
                                userAccessRef.setValue(access)
                                showToast("Предмет додано успішно")
                                frameNav.navigate(R.id.action_FindLesson_to_Start)
                            }
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