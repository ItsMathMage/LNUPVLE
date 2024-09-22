package com.example.lnupvle.ui.fragments.settings

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.example.lnupvle.R
import com.example.lnupvle.dataClass.User
import com.example.lnupvle.utilits.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class FragmentSettingsMain : Fragment() {

    private lateinit var databaseRef: DatabaseReference

    private lateinit var profileImageView: ImageView

    private lateinit var uidField: EditText
    private lateinit var firstnameField: EditText
    private lateinit var lastnameField: EditText
    private lateinit var emailField: EditText
    private lateinit var phoneField: EditText

    private lateinit var userId: String
    private lateinit var firstname: String
    private lateinit var lastname: String
    private lateinit var email: String
    private lateinit var phone: String

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings_main, container, false)

        profileImageView = view.findViewById(R.id.profile_image)

        uidField = view.findViewById(R.id.uid_field)
        firstnameField = view.findViewById(R.id.setting_firstname)
        lastnameField = view.findViewById(R.id.setting_lastname)
        emailField = view.findViewById(R.id.email_field)
        phoneField = view.findViewById(R.id.phone_field)

        getUserData()

        val storageReference = FirebaseStorage.getInstance().getReference("app/Icons/profile.png")

        storageReference.getBytes(Long.MAX_VALUE).addOnSuccessListener { bytes ->
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

            profileImageView.setImageBitmap(bitmap)
        }.addOnFailureListener { exception ->
            showToast("Firebase Error getting data " + exception.message.toString())
        }

        val copyUidButton = view.findViewById<Button>(R.id.button_copy_uid)
        val changeNameButton = view.findViewById<Button>(R.id.button_change_name)
        val changePasswordButton = view.findViewById<Button>(R.id.button_change_password)
        val changeEmailButton = view.findViewById<Button>(R.id.button_change_email)
        val changePhoneButton = view.findViewById<Button>(R.id.button_change_phone)

        copyUidButton.setOnClickListener() {
            val clipboard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", userId)
            clipboard.setPrimaryClip(clip)
            showToast("Персональний ID скопійовано в буфер обміну")
        }

        changeNameButton.setOnClickListener() {
            val firstnameChange = firstnameField.text.toString()
            val lastnameChange = lastnameField.text.toString()

            if (firstnameChange.isEmpty() or lastnameChange.isEmpty()) {
                showToast("Заповніть дані")
            } else {
                val userPref = requireActivity().getSharedPreferences("UserPref", android.content.Context.MODE_PRIVATE)
                val uid = userPref.getString("UID", "").toString()

                databaseRef = FirebaseDatabase.getInstance().getReference("app")
                val userFirstnameRef = databaseRef.child("users").child(uid).child("firstname")
                val userLastnameRef = databaseRef.child("users").child(uid).child("lastname")

                userFirstnameRef.setValue(firstnameChange)
                userLastnameRef.setValue(lastnameChange)
                    .addOnSuccessListener() {
                        showToast("Дані успішно змінено")
                    }
                    .addOnFailureListener() {
                        showToast("Помилка зміни даних")
                    }
            }
        }

        changePasswordButton.setOnClickListener() {
            auth = FirebaseAuth.getInstance()
            auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful)
                {
                    showToast("Лист для скидання паролю відправлено")
                } else {
                    showToast("Виникла помилка при відправці листа")
                }
            }
        }

        return view
    }

    private fun getUserData() {
        val userPref = requireActivity().getSharedPreferences("UserPref", android.content.Context.MODE_PRIVATE)
        val uid = userPref.getString("UID", "").toString()
        databaseRef = FirebaseDatabase.getInstance().getReference("app")

        val usersRef = databaseRef.child("users").child(uid)

        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val user = dataSnapshot.getValue(User::class.java)
                    if (user != null) {
                        userId = user.uid
                        firstname = user.firstname
                        lastname = user.lastname
                        email = user.email
                        phone = user.phone

                        uidField.setText(userId)
                        firstnameField.setText(firstname)
                        lastnameField.setText(lastname)
                        emailField.setText(email)
                        phoneField.setText(phone)
                    } else {
                        showToast("Не вдалося отримати дані користувача")
                    }
                } else {
                    showToast("Помилка отримання даних користувача")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}