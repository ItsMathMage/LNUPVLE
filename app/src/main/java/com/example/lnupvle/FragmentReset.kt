package com.example.lnupvle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth

class FragmentReset : Fragment() {

    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_reset, container, false)

        navController = findNavController()

        val emailField = view.findViewById<EditText>(R.id.lesson_id_field)

        val buttonToBack = view.findViewById<Button>(R.id.to_back_button)
        val buttonReset = view.findViewById<Button>(R.id.reset_button)

        buttonToBack.setOnClickListener() {
            navController.navigate(R.id.action_Reset_to_Login)
        }

        buttonReset.setOnClickListener() {
            auth = FirebaseAuth.getInstance()
            auth.sendPasswordResetEmail(emailField.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful)
                    {
                        showToast("Лист для скидання паролю відправлено")
                        navController.navigate(R.id.action_Reset_to_Login)

                    } else {
                        showToast("Виникла помилка при відправці листа")
                    }
                }
        }

        return view
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}