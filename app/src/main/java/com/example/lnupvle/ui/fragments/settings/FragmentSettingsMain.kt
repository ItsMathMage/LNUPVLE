package com.example.lnupvle.ui.fragments.settings

import android.app.Activity.RESULT_OK
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lnupvle.R
import com.example.lnupvle.dataClass.Lecture
import com.example.lnupvle.dataClass.User
import com.example.lnupvle.utilits.navigate
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
    private lateinit var userPref: SharedPreferences

    private lateinit var profileImageView: ImageView

    private lateinit var uidField: EditText
    private lateinit var firstnameField: EditText
    private lateinit var lastnameField: EditText
    private lateinit var emailField: EditText
    private lateinit var phoneField: EditText

    private lateinit var uid: String
    private lateinit var userId: String
    private lateinit var firstname: String
    private lateinit var lastname: String
    private lateinit var email: String
    private lateinit var phone: String
    private lateinit var image: String

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

        userPref = requireActivity().getSharedPreferences("UserPref", android.content.Context.MODE_PRIVATE)
        uid = userPref.getString("UID", "").toString()

        getUserData()

        try {
            getImage()
        } catch (e: Exception) {
            showToast(e.message.toString())
        }

        val pickFile = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == RESULT_OK && it.data != null){
                val uri = it.data!!.data
                uploadFile(uri!!)
            }
        }

        val changeImageButton = view.findViewById<Button>(R.id.button_change_image)
        val copyUidButton = view.findViewById<Button>(R.id.button_copy_uid)
        val changeNameButton = view.findViewById<Button>(R.id.button_change_name)
        val changePasswordButton = view.findViewById<Button>(R.id.button_change_password)
        val changeEmailButton = view.findViewById<Button>(R.id.button_change_email)
        val changePhoneButton = view.findViewById<Button>(R.id.button_change_phone)

        changeImageButton.setOnClickListener() {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            pickFile.launch(intent)
        }

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

    private fun getImage() {
        val userRef = databaseRef.child("users").child(uid)
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val user = dataSnapshot.getValue(User::class.java)
                    if (user != null) {
                        setImage(user.image)
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

    private fun setImage (image: String) {
        val storageReference = FirebaseStorage.getInstance().getReference("app/profile_images/${image}")

        storageReference.getBytes(Long.MAX_VALUE).addOnSuccessListener { bytes ->
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            val minSide = minOf(bitmap.width, bitmap.height)
            val x = (bitmap.width - minSide) / 2
            val y = (bitmap.height - minSide) / 2
            val croppedBitmap = Bitmap.createBitmap(bitmap, x, y, minSide, minSide)

            profileImageView.setImageBitmap(croppedBitmap)
            profileImageView.setBackgroundResource(R.drawable.circle_shape)
            profileImageView.clipToOutline = true
        }.addOnFailureListener { exception ->
            showToast("Firebase Error getting data " + exception.message.toString())
        }
    }

    private fun uploadFile (uri: Uri) {
        FirebaseStorage.getInstance().getReference("app")
            .child("profile_images/$uid")
            .putFile(uri)
            .addOnSuccessListener {
                val userRef = databaseRef.child("users").child(uid).child("image")
                userRef.setValue(uid)
                showToast("Фото завантажено успішно")
                getImage()
            }
            .addOnFailureListener {
                showToast("Не вдалося завантажити фото")
            }

    }


    private fun getUserData() {
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
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}