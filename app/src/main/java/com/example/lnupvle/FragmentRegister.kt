package com.example.lnupvle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentRegister.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentRegister : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        navController = findNavController()

        val firstnameField = view.findViewById<EditText>(R.id.firstname_field)
        val lastnameField = view.findViewById<EditText>(R.id.lastname_field)
        val emailField = view.findViewById<EditText>(R.id.email_field)
        val phoneField = view.findViewById<EditText>(R.id.phone_field)
        val passwordField = view.findViewById<EditText>(R.id.password_field)
        val repeatField = view.findViewById<EditText>(R.id.repeat_field)

        val buttonToLogin = view.findViewById<Button>(R.id.button_to_login)
        val registerButton = view.findViewById<Button>(R.id.register_button)

        buttonToLogin.setOnClickListener() {
            navController.navigate(R.id.action_Register_to_Login)
        }

        registerButton.setOnClickListener() {
            
        }

        return view
    }


}