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
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentReset.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentReset : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var navController: NavController
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_reset, container, false)

        navController = findNavController()

        val emailField = view.findViewById<EditText>(R.id.email_field)

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

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}