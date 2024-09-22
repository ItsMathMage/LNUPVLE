package com.example.lnupvle.ui.fragments.chat

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.lnupvle.R
import com.example.lnupvle.dataClass.ChatsAccess
import com.example.lnupvle.dataClass.Message
import com.example.lnupvle.dataClass.User
import com.example.lnupvle.utilits.navigate
import com.example.lnupvle.utilits.showToast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date

class FragmentChatsAdd : Fragment() {
    private lateinit var userPref: SharedPreferences
    private lateinit var databaseRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chats_add, container, false)

        val useridField = view.findViewById<EditText>(R.id.uid_field)

        val createChatButton = view.findViewById<Button>(R.id.button_add_chat)
        val toBackButton = view.findViewById<Button>(R.id.to_back_button)

        createChatButton.setOnClickListener() {
            databaseRef = FirebaseDatabase.getInstance().getReference("app")
            userPref = requireActivity().getSharedPreferences("UserPref", android.content.Context.MODE_PRIVATE)
            val myId = userPref.getString("UID", "").toString()
            val otherId = useridField.text.toString()
            val chatId = myId+otherId

            getUserName(chatId, otherId, myId)

            navigate(R.id.action_ChatsAdd_to_ChatsMain)
        }

        toBackButton.setOnClickListener() {
            navigate(R.id.action_ChatsAdd_to_ChatsMain)
        }

        return view
    }

    private fun getUserName(chatId: String, myId: String, otherId: String) {
        databaseRef = FirebaseDatabase.getInstance().getReference("app")
        val myUserRef = databaseRef.child("users").child(myId)
        myUserRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val user =  dataSnapshot.getValue(User::class.java)
                    if (user != null) {
                        val name = user.lastname + " " + user.firstname
                        addAccess(chatId, otherId, myId, name)
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        val otherUserRef = databaseRef.child("users").child(otherId)
        otherUserRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val user =  dataSnapshot.getValue(User::class.java)
                    if (user != null) {
                        val name = user.lastname + " " + user.firstname
                        addAccess(chatId, myId, otherId, name)
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        addChat(chatId)
    }

    private fun addAccess(chatId: String, yourId: String, userId: String, chatName: String) {
        val chatAccessRef = databaseRef.child("chat_access").child(yourId).child(chatId)

        val chatsAccess = ChatsAccess(chatId, userId, chatName)
        chatAccessRef.setValue(chatsAccess)
            .addOnSuccessListener {
                showToast("Чат доданий")
            }
            .addOnFailureListener() {
                showToast("Помилка")
            }
    }

    private fun addChat(chatId: String) {
        val dateFormat  = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        val date = Date()
        val format = dateFormat.format(date)
        val time = format.toString()

        val chatRef = databaseRef.child("chat_messages").child(chatId)
        val message = Message("Система", "Розпочато новий чат", "system", time)
        chatRef.push().setValue(message)
    }
}