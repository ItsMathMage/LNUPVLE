package com.example.lnupvle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FragmentLogin : Fragment() {

    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        navController = findNavController()

        val userPref = requireContext().getSharedPreferences("UserPref", android.content.Context.MODE_PRIVATE)
        var isLoggedIn = userPref.getBoolean("ISLOGGEDIN", false)

        if (isLoggedIn) {
            val email = userPref.getString("EMAIL", "").toString()
            val password = userPref.getString("PASS", "").toString()
            authUser(email, password)
        }

        val emailField = view.findViewById<EditText>(R.id.lesson_id_field)
        val passwordField = view.findViewById<EditText>(R.id.password_lesson_field)

        val buttonLogin = view.findViewById<Button>(R.id.login_button)
        val buttonToRegister = view.findViewById<Button>(R.id.button_to_register)
        val toResetPassword = view.findViewById<TextView>(R.id.to_reset_password)

        buttonLogin.setOnClickListener() {
            val email = emailField.text.toString()
            val password = passwordField.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                showToast("Введіть усі дані")
            } else {
                authUser(email, password)
            }
        }

        buttonToRegister.setOnClickListener() {
            navController.navigate(R.id.action_Login_to_Register)
        }

        toResetPassword.setOnClickListener() {
            navController.navigate(R.id.action_Login_to_Reset)
        }

        return view
    }

    private fun authUser(email: String, password: String) {
        val userPref = requireContext().getSharedPreferences("UserPref", android.content.Context.MODE_PRIVATE)
        val editor = userPref.edit()
        auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user:
                            FirebaseUser = task.result!!.user!!
                    if (user.isEmailVerified) {
                        editor.putString("UID", user.uid)
                        editor.putString("EMAIL", email)
                        editor.putString("PASS", password)
                        editor.putBoolean("ISLOGGEDIN", true)
                        editor.apply()
                        navController.navigate(R.id.action_Login_to_Main)
                    } else {
                        showToast("Обліковий запис не підтверджений")
                    }
                }
            }
            .addOnFailureListener() { error ->
                showToast("Помилка ${error.message}")
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}