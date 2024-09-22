package com.example.lnupvle.ui.fragments.lesson

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lnupvle.R
import com.example.lnupvle.adapterClass.LectureAdapter
import com.example.lnupvle.dataClass.Lecture
import com.example.lnupvle.utilits.navigate
import com.example.lnupvle.utilits.showToast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FragmentLesson : Fragment() {
    private lateinit var storageRef: StorageReference
    private lateinit var databaseRef: DatabaseReference
    private lateinit var lecturesArrayList: ArrayList<Lecture>
    private lateinit var lecturesRecyclerView: RecyclerView

    private lateinit var lessonRole: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_lesson, container, false)

        storageRef = FirebaseStorage.getInstance().getReference("test")

        val toBackButton = view.findViewById<Button>(R.id.to_back_button)
        val createLectureButton = view.findViewById<Button>(R.id.create_lecture_button)

        lecturesArrayList = arrayListOf<Lecture>()
        lecturesRecyclerView = view.findViewById(R.id.lectures_list)
        lecturesRecyclerView.layoutManager = LinearLayoutManager(context)
        lecturesRecyclerView.setHasFixedSize(true)

        getLectureData()

        toBackButton.setOnClickListener() {
            navigate(R.id.action_Lesson_to_Start)
        }

        createLectureButton.setOnClickListener() {
            if (lessonRole == "student") {
                showToast("Лекціями керувати може тільки викладач")
            } else {
                navigate(R.id.action_Lesson_to_LectureEdit)
            }
        }

        return view.rootView
    }

    private fun getLectureData() {
        val userPref = requireActivity().getSharedPreferences("UserPref", android.content.Context.MODE_PRIVATE)
        val lid = userPref.getString("LID", "").toString()
        lessonRole = userPref.getString("LA", "").toString()

        databaseRef = FirebaseDatabase.getInstance().getReference("app")
        val lecturesRef = databaseRef.child("lectures").child(lid)

        lecturesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    lecturesArrayList.clear()
                    for (lectureSnapshot in dataSnapshot.children) {
                        val lecture = lectureSnapshot.getValue(Lecture::class.java)

                        lecturesArrayList.add(lecture!!)
                    }

                    lecturesRecyclerView.adapter =
                        LectureAdapter(lecturesArrayList, requireActivity())
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}