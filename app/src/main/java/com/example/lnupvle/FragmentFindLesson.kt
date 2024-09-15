package com.example.lnupvle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.credentials.PasswordCredential
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentFindLesson.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentFindLesson : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var frameNav: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
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

    fun addLesson (lessonId: String, lessonPassword: String) {
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
                            frameNav = findNavController()
                            frameNav.navigate(R.id.action_FindLesson_to_Start)
                        }
                    }
                } else {

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}