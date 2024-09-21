package com.example.lnupvle.ui.fragments.chat

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
import com.example.lnupvle.adapterClass.ChatsAdapter
import com.example.lnupvle.dataClass.ChatsAccess
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentChatsMain : Fragment() {

    private lateinit var chatsNav: NavController
    private lateinit var userPref : SharedPreferences
    private lateinit var databaseRef: DatabaseReference
    private lateinit var chatsArrayList: ArrayList<ChatsAccess>
    private lateinit var chatsRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chats_main, container, false)

        chatsNav = findNavController()

        userPref = requireActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE)

        chatsArrayList = arrayListOf<ChatsAccess>()
        chatsRecyclerView = view.findViewById(R.id.chats_list)
        chatsRecyclerView.layoutManager = LinearLayoutManager(context)
        chatsRecyclerView.setHasFixedSize(true)

        getChatsData()

        val toAddChatButton = view.findViewById<Button>(R.id.to_add_chat)

        toAddChatButton.setOnClickListener() {
            chatsNav.navigate(R.id.action_ChatsMain_to_ChatsAdd)
        }

        return view
    }

    private fun getChatsData() {
        val uid = userPref.getString("UID", "").toString()
        databaseRef = FirebaseDatabase.getInstance().getReference("app")

        val chatsRef = databaseRef.child("chat_access").child(uid)
        chatsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    chatsArrayList.clear()
                    for ( detailsSnapshot in dataSnapshot.children) {
                        val chats =  detailsSnapshot.getValue(ChatsAccess::class.java)
                        chatsArrayList.add(chats!!)
                    }

                    chatsRecyclerView.adapter = ChatsAdapter(chatsArrayList, chatsNav, requireActivity())
                } else {

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}