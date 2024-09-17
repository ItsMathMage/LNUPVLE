package com.example.lnupvle

import android.content.pm.PackageManager
import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FragmentLesson : Fragment() {
    private lateinit var frameNav: NavController
    private lateinit var storageRef: StorageReference

    private lateinit var databaseRef: DatabaseReference
    private lateinit var lecturesArrayList: ArrayList<Lecture>
    private lateinit var lecturesRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_lesson, container, false)

        frameNav = findNavController()

        storageRef = FirebaseStorage.getInstance().getReference("test")

        val toBackButton = view.findViewById<Button>(R.id.to_back_button)
        val createLectureButton = view.findViewById<Button>(R.id.create_lecture_button)

        val userPref =
            requireActivity().getSharedPreferences("UserPref", android.content.Context.MODE_PRIVATE)
        val lessonId = userPref.getString("LID", "").toString()

        lecturesArrayList = arrayListOf<Lecture>()
        lecturesRecyclerView = view.findViewById(R.id.lectures_list)
        lecturesRecyclerView.layoutManager = LinearLayoutManager(context)
        lecturesRecyclerView.setHasFixedSize(true)

        getLectureData()

        startPerm()

        toBackButton.setOnClickListener() {
            frameNav.navigate(R.id.action_Lesson_to_Start)
        }

        createLectureButton.setOnClickListener() {

            frameNav.navigate(R.id.action_Lesson_to_LectureEdit)
        }

        return view.rootView
    }

    private fun getLectureData() {
        val userPref =
            requireActivity().getSharedPreferences("UserPref", android.content.Context.MODE_PRIVATE)
        val lid = userPref.getString("LID", "").toString()

        databaseRef = FirebaseDatabase.getInstance().getReference("app")
        val lecturesRef = databaseRef.child("lectures").child(lid)

        lecturesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (lectureSnapshot in dataSnapshot.children) {
                        val lecture = lectureSnapshot.getValue(Lecture::class.java)
                        lecturesArrayList.add(lecture!!)
                    }

                    lecturesRecyclerView.adapter =
                        LectureAdapter(lecturesArrayList, requireActivity())
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

    private val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1

    private fun startPerm() {
        if (checkWriteExternalStoragePermission()) {

        } else {
            requestWriteExternalStoragePermission()
            showToast("Дозволу нема")
        }
    }

    private fun checkWriteExternalStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    }

    private fun requestWriteExternalStoragePermission() {
        ActivityCompat.requestPermissions(requireActivity(),
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                showToast("Дозвіл отримано")
            } else {
                showToast("Дозвіл невдався")
            }
        }
    }
}