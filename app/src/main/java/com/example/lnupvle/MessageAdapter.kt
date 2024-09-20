package com.example.lnupvle

import android.content.Context
import android.graphics.Color
import android.graphics.Color.parseColor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MessageAdapter (
    private val messageList: List<Message>,
    private val context: Context
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)

        return MessageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val messages = messageList[position]

        val userPref = context.getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        val uid = userPref.getString("UID", "").toString()

        val databaseRef = FirebaseDatabase.getInstance().getReference("app")

        val userRef = databaseRef.child("users").child(uid)
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user =  dataSnapshot.getValue(User::class.java)

                if (user != null) {
                    val username = user.lastname + " " + user.firstname
                    if (messages.user == "Система") {
                        holder.messageLayout.setBackgroundResource(R.color.light_green)
                    }
                    if (messages.user == username) {
                        holder.messageLayout.setBackgroundResource(R.color.light_blue)
                    }

                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        holder.messageUser.text = messages.user
        holder.messageTime.text = messages.time
        holder.messageText.text = messages.message
        holder.messageLayout.setOnLongClickListener {
            showToast("Long...")
            return@setOnLongClickListener true
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageUser: TextView = itemView.findViewById(R.id.message_user)
        val messageTime: TextView = itemView.findViewById(R.id.message_time)
        val messageText: TextView = itemView.findViewById(R.id.message_text)
        val messageLayout: LinearLayout = itemView.findViewById(R.id.message_card_layout)
    }
}
