package com.example.lnupvle.ui.fragments.lesson

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lnupvle.R
import com.example.lnupvle.adapterClass.LessonAdapter
import com.example.lnupvle.dataClass.Access
import com.example.lnupvle.utilits.showToast
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.TimeUnit

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

    private fun test() {
        val phoneNumber = "+380663393878"
        val smsCode = "123456"

        val firebaseAuth = Firebase.auth
        val firebaseAuthSettings = firebaseAuth.firebaseAuthSettings

        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(phoneNumber, smsCode)

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    showToast("comp")
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    showToast(e.message.toString())
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    showToast("good")
                }

            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}