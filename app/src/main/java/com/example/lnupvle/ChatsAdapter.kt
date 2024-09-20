package com.example.lnupvle

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView

class ChatsAdapter (
    private val chatsList: List<ChatsAccess>,
    private val chatsNav: NavController,
    private val context: Context
) : RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false)

        return ChatsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return chatsList.size
    }

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        val chats = chatsList[position]

        holder.chatName.text = chats.chatName

        holder.chatCardView.setOnClickListener() {
            val userPref : SharedPreferences = context.getSharedPreferences("UserPref", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = userPref.edit()
            editor.putString("CID", chats.chatId)
            editor.apply()
            chatsNav.navigate(R.id.action_ChatsMain_to_ChatsCurrent)
        }
    }

    class ChatsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chatName: TextView = itemView.findViewById(R.id.chat_name)
        val chatCardView: LinearLayout = itemView.findViewById(R.id.chat_card_layout)
    }
}