package com.example.lnupvle.ui.fragments.schedule

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
import com.example.lnupvle.adapterClass.ScheduleAdapter
import com.example.lnupvle.dataClass.ScheduleTempUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentScheduleMain : Fragment() {

    private lateinit var scheduleNav: NavController

    private lateinit var databaseRef: DatabaseReference
    private lateinit var scheduleArrayList: ArrayList<ScheduleTempUser>
    private lateinit var scheduleRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_schedule_main, container, false)

        scheduleNav = findNavController()

        scheduleArrayList = arrayListOf<ScheduleTempUser>()
        scheduleRecyclerView = view.findViewById(R.id.schedule_list)
        scheduleRecyclerView.layoutManager = LinearLayoutManager(context)
        scheduleRecyclerView.setHasFixedSize(true)

        val findScheduleButton = view.findViewById<Button>(R.id.to_find_schedule_button)
        val editScheduleButton = view.findViewById<Button>(R.id.to_create_schedule_button)

        getScheduleData()

        findScheduleButton.setOnClickListener() {
            scheduleNav.navigate(R.id.action_ScheduleMain_to_SheduleFind)
        }

        editScheduleButton.setOnClickListener() {
            scheduleNav.navigate(R.id.action_ScheduleMain_to_ScheduleEdit)
        }

        return  view
    }

    private fun getScheduleData() {
        val userPref = requireActivity().getSharedPreferences("UserPref", android.content.Context.MODE_PRIVATE)
        val uid = userPref.getString("UID", "").toString()
        databaseRef = FirebaseDatabase.getInstance().getReference("app")

        val scheduleAccessRef = databaseRef.child("schedule_reference").child(uid)

        scheduleAccessRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    scheduleArrayList.clear()
                    for (lessonSnapshot in dataSnapshot.children) {
                        val schedule = lessonSnapshot.getValue(ScheduleTempUser::class.java)
                        scheduleArrayList.add(schedule!!)
                    }

                    scheduleRecyclerView.adapter = ScheduleAdapter(scheduleArrayList, scheduleNav, requireActivity())
                } else {

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}