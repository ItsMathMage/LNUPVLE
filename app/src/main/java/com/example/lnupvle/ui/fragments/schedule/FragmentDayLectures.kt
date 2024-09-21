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
import com.example.lnupvle.adapterClass.DetailsAdapter
import com.example.lnupvle.dataClass.Details
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentDayLectures : Fragment() {

    private lateinit var scheduleNav: NavController
    private lateinit var userPref : SharedPreferences
    private lateinit var databaseRef: DatabaseReference
    private lateinit var detailsArrayList: ArrayList<Details>
    private lateinit var detailsRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        scheduleNav = findNavController()

        val view = inflater.inflate(R.layout.fragment_day_lectures, container, false)

        userPref = requireActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE)

        detailsArrayList = arrayListOf<Details>()
        detailsRecyclerView = view.findViewById(R.id.lectures_list)
        detailsRecyclerView.layoutManager = LinearLayoutManager(context)
        detailsRecyclerView.setHasFixedSize(true)

        getDetailsData()

        val toBackButton = view.findViewById<Button>(R.id.to_back_button)
        val toEditButton = view.findViewById<Button>(R.id.to_edit_button)

        toBackButton.setOnClickListener() {
            scheduleNav.navigate(R.id.action_DayLectures_to_ScheduleDays)
        }

        toEditButton.setOnClickListener() {
            scheduleNav.navigate(R.id.action_DayLectures_to_DetailsEdit)
        }

        return view
    }

    private fun getDetailsData() {
        val sid = userPref.getString("SID", "").toString()
        val dayName = userPref.getString("DN", "").toString()
        databaseRef = FirebaseDatabase.getInstance().getReference("app")

        val detailsRef = databaseRef.child("details").child(sid).child(dayName)

        detailsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    detailsArrayList.clear()
                    for ( detailsSnapshot in dataSnapshot.children) {
                        val  details =  detailsSnapshot.getValue(Details::class.java)
                        detailsArrayList.add( details!!)
                    }

                    detailsRecyclerView.adapter = DetailsAdapter(detailsArrayList, scheduleNav, requireActivity())
                } else {

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}