package com.example.lnupvle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentStart : Fragment() {
    private lateinit var frameNav: NavController

    private lateinit var databaseRef: DatabaseReference
    private lateinit var lessonsRecyclerView: RecyclerView
    private lateinit var lessonsArrayList: ArrayList<Access>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_start, container, false)

        frameNav = findNavController()

        val toFindLessonButton = view.findViewById<Button>(R.id.to_find_lesson_button)
        val toCreateLessonButton = view.findViewById<Button>(R.id.to_create_lesson_button)

        lessonsArrayList = arrayListOf<Access>()
        lessonsRecyclerView = view.findViewById(R.id.lessons_list)
        lessonsRecyclerView.layoutManager = LinearLayoutManager(context)
        lessonsRecyclerView.setHasFixedSize(true)

        getLessonData()

        toFindLessonButton.setOnClickListener() {
            frameNav.navigate(R.id.action_Start_to_FindLesson)
        }

        toCreateLessonButton.setOnClickListener() {
            frameNav.navigate(R.id.action_Start_to_CreateLesson)
        }

        return view
    }

    private fun getLessonData() {
        val userPref = requireActivity().getSharedPreferences("UserPref", android.content.Context.MODE_PRIVATE)
        val uid = userPref.getString("UID", "").toString()

        databaseRef = FirebaseDatabase.getInstance().getReference("app")
        val lessonAccessRef = databaseRef.child("access").child(uid)

        lessonAccessRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    lessonsArrayList.clear()
                    for (lessonSnapshot in dataSnapshot.children) {
                        val lesson = lessonSnapshot.getValue(Access::class.java)
                        lessonsArrayList.add(lesson!!)
                    }

                    lessonsRecyclerView.adapter = LessonAdapter(lessonsArrayList, frameNav, requireActivity())
                } else {

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}