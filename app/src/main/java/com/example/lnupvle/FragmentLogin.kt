package com.example.lnupvle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentLogin.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentLogin : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var navController: NavController

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
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        navController = findNavController()

        val emailField = view.findViewById<EditText>(R.id.email_field)
        val passwordField = view.findViewById<EditText>(R.id.password_field)

        val buttonLogin = view.findViewById<Button>(R.id.login_button)
        val buttonToRegister = view.findViewById<Button>(R.id.button_to_register)
        val toResetPassword = view.findViewById<TextView>(R.id.to_reset_password)

        buttonLogin.setOnClickListener() {
            navController.navigate(R.id.action_Login_to_Main)
        }

        buttonToRegister.setOnClickListener() {
            navController.navigate(R.id.action_Login_to_Register)
        }

        toResetPassword.setOnClickListener() {
            navController.navigate(R.id.action_Login_to_Reset)
        }

        return view
    }
}