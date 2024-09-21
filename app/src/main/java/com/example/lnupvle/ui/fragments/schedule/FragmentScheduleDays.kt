package com.example.lnupvle.ui.fragments.schedule

import android.content.Context
import android.content.SharedPreferences
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
import com.example.lnupvle.adapterClass.DayAdapter
import com.example.lnupvle.dataClass.Day
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentScheduleDays : Fragment() {

    private lateinit var scheduleNav: NavController
    private lateinit var userPref : SharedPreferences
    private lateinit var databaseRef: DatabaseReference
    private lateinit var daysArrayList: ArrayList<Day>
    private lateinit var daysRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_schedule_days, container, false)

        userPref = requireActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE)

        scheduleNav = findNavController()

        daysArrayList = arrayListOf<Day>()
        daysRecyclerView = view.findViewById(R.id.days_list)
        daysRecyclerView.layoutManager = LinearLayoutManager(context)
        daysRecyclerView.setHasFixedSize(true)

        getDaysData()

        val toBackButton = view.findViewById<Button>(R.id.to_back_button)
        val toEditButton = view.findViewById<Button>(R.id.to_edit_button)

        toBackButton.setOnClickListener() {
            scheduleNav.navigate(R.id.action_ScheduleDays_to_ScheduleMain)
        }

        toEditButton.setOnClickListener() {
            scheduleNav.navigate(R.id.action_ScheduleDays_to_DaysEdit)
        }

        return view
    }

    private fun getDaysData() {
        val sid = userPref.getString("SID", "").toString()
        databaseRef = FirebaseDatabase.getInstance().getReference("app")

        val scheduleAccessRef = databaseRef.child("days").child(sid)

        scheduleAccessRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    daysArrayList.clear()
                    for (daySnapshot in dataSnapshot.children) {
                        val day = daySnapshot.getValue(Day::class.java)
                        daysArrayList.add(day!!)
                    }

                    daysRecyclerView.adapter = DayAdapter(daysArrayList, scheduleNav, requireActivity())
                } else {

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}