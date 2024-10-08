package com.example.lnupvle.ui.fragments.chat

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lnupvle.R
import com.example.lnupvle.adapterClass.MessageAdapter
import com.example.lnupvle.dataClass.Message
import com.example.lnupvle.dataClass.User
import com.example.lnupvle.utilits.showToast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor
import kotlin.math.roundToInt

class FragmentChatsCurrent : Fragment() {
    private lateinit var userPref : SharedPreferences
    private lateinit var databaseRef: DatabaseReference
    private lateinit var messageList: ArrayList<Message>
    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageField: EditText

    private lateinit var key: String

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chats_current, container, false)

        userPref = requireActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE)

        messageField = view.findViewById(R.id.message_field)

        val sendButton = view.findViewById<ImageButton>(R.id.button_send)

        messageList = arrayListOf<Message>()
        messageRecyclerView = view.findViewById(R.id.message_list)
        messageRecyclerView.layoutManager = LinearLayoutManager(context)
        messageRecyclerView.setHasFixedSize(true)

        getMessageData()

        getScreen(view)

        sendButton.setOnClickListener() {
            val uid = userPref.getString("UID", "").toString()

            databaseRef = FirebaseDatabase.getInstance().getReference("app")

            val userRef = databaseRef.child("users").child(uid)
            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user =  dataSnapshot.getValue(User::class.java)

                    if (user != null) {
                        val chatId = userPref.getString("CID", "").toString()
                        val username = user.lastname + " " + user.firstname
                        val messageText = messageField.text.toString()

                        val dateFormat  = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                        val date = Date()
                        val format = dateFormat.format(date)
                        val time = format.toString()

                        sendMessage(username, messageText, "user", time, chatId)
                    }

                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
        }

        return view
    }

    private fun getScreen(view: View) {
//        val displayMetrics = context?.resources?.displayMetrics ?: return
//
//        val screenHeight = displayMetrics.heightPixels.toDouble()
//        val screenWidth = displayMetrics.widthPixels.toDouble()
//
//        val aspectRatio = screenHeight / screenWidth
//        val temp = aspectRatio.toString()
//        val number = temp.substring(0..2)
//        val cof = number.toDouble()

        val screen = view.findViewById<ConstraintLayout>(R.id.chat_layout)

        val params = screen.layoutParams as ViewGroup.MarginLayoutParams
        params.setMargins(0, -50, 0, -100)
        screen.layoutParams = params
    }

    private fun sendMessage(username: String, messageText: String, type: String, time: String, chatId: String) {
        val message = Message(username, messageText, type, time)
        val userRef = databaseRef.child("chat_messages").child(chatId)
        key = userRef.push().key.toString()
        userRef.child(key).setValue(message)
        messageField.setText("")
    }

    private fun getMessageData() {
        val chatId = userPref.getString("CID", "").toString()
        databaseRef = FirebaseDatabase.getInstance().getReference("app")

        val chatsRef = databaseRef.child("chat_messages").child(chatId)
        chatsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    messageList.clear()
                    for ( detailsSnapshot in dataSnapshot.children) {
                        val message =  detailsSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }

                    messageRecyclerView.adapter = MessageAdapter(messageList, requireActivity())
                } else {

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}