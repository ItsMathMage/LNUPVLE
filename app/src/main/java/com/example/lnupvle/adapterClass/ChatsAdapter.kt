package com.example.lnupvle.adapterClass

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.lnupvle.R
import com.example.lnupvle.dataClass.ChatsAccess
import com.example.lnupvle.dataClass.User
import com.example.lnupvle.utilits.showToast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

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

        val userPref : SharedPreferences = context.getSharedPreferences("UserPref", Context.MODE_PRIVATE)

        holder.chatName.text = chats.chatName

        holder.chatCardView.setOnClickListener() {
            val editor: SharedPreferences.Editor = userPref.edit()
            editor.putString("CID", chats.chatId)
            editor.apply()
            chatsNav.navigate(R.id.action_ChatsMain_to_ChatsCurrent)
        }

        val uid = userPref.getString("UID", "")
        val databaseRef = FirebaseDatabase.getInstance().getReference("app")
        val chatRef = databaseRef
            .child("chat_access").child(uid!!).child(chats.chatId)
        chatRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val chat = dataSnapshot.getValue(ChatsAccess::class.java)
                    if (chat != null) {
                        getImage(chat.userId, holder.profileImageView)
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun getImage(userId: String, imageView: ImageView) {
        val image = imageView
        val databaseRef = FirebaseDatabase.getInstance().getReference("app")
        val userRef = databaseRef.child("users").child(userId)
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val user = dataSnapshot.getValue(User::class.java)
                    if (user != null) {
                        setImage(user.image, imageView)
                    } else {
                        showToast("Не вдалося отримати дані користувача")
                    }
                } else {
                    showToast("Помилка отримання даних користувача")
                }
            }
            override fun onCancelled(databaseError: DatabaseError) { }
        })
    }

    private fun setImage (image: String, imageView: ImageView) {
        val storageReference = FirebaseStorage.getInstance().getReference("app/profile_images/${image}")

        storageReference.getBytes(Long.MAX_VALUE).addOnSuccessListener { bytes ->
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            val minSide = minOf(bitmap.width, bitmap.height)
            val x = (bitmap.width - minSide) / 2
            val y = (bitmap.height - minSide) / 2
            val croppedBitmap = Bitmap.createBitmap(bitmap, x, y, minSide, minSide)

            imageView.setImageBitmap(croppedBitmap)
            imageView.setBackgroundResource(R.drawable.circle_shape)
            imageView.clipToOutline = true
        }.addOnFailureListener { exception ->
            showToast("Firebase Error getting data " + exception.message.toString())
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    class ChatsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chatName: TextView = itemView.findViewById(R.id.chat_name)
        val chatCardView: ConstraintLayout = itemView.findViewById(R.id.chat_card_layout)
        val profileImageView: ImageView = itemView.findViewById(R.id.profile_image)
    }
}