package com.example.lnupvle.ui.fragments.main

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
import com.example.lnupvle.dataClass.User
import com.example.lnupvle.utilits.navigate
import com.example.lnupvle.utilits.showToast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database

class FragmentRegister : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        val auth = FirebaseAuth.getInstance()

        val firstnameField = view.findViewById<EditText>(R.id.firstname_field)
        val lastnameField = view.findViewById<EditText>(R.id.lastname_field)
        val emailField = view.findViewById<EditText>(R.id.email_field)
        val phoneField = view.findViewById<EditText>(R.id.phone_field)
        val passwordField = view.findViewById<EditText>(R.id.password_field)
        val repeatField = view.findViewById<EditText>(R.id.repeat_field)

        val buttonToLogin = view.findViewById<Button>(R.id.button_to_login)
        val registerButton = view.findViewById<Button>(R.id.register_button)

        buttonToLogin.setOnClickListener() {
            navigate(R.id.action_Register_to_Login)
        }

        registerButton.setOnClickListener() {
            val firstname = firstnameField.text.toString()
            val lastname = lastnameField.text.toString()
            val email = emailField.text.toString()
            val phone = phoneField.text.toString()
            val password = passwordField.text.toString()
            val repeat = repeatField.text.toString()

            val fields = listOf(firstname, lastname, email, phone, password, repeat)

            if (fields.any() { it.isEmpty() }) {
                showToast("Паролі не співпадають")
            } else if (password != repeat) {
                showToast("Паролі не співпадають")
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            user?.sendEmailVerification()
                                ?.addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        showToast("Лист з підтвердженням надісланий на вашу електронну пошту")

                                        val database = Firebase.database
                                        val userRef = database.getReference("app")
                                            .child("users")
                                            .child(user.uid)

                                        val userData = User(user.uid, firstname, lastname, email, phone)

                                        userRef.setValue(userData)

                                        navigate(R.id.action_Register_to_Login)
                                    } else {
                                        showToast("Виникла помилка при відправленні листа на пошту")
                                    }
                                }
                        } else {
                            showToast("Помилка реєстрації")
                        }
                    }
            }

        }

        return view
    }
}