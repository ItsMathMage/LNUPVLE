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
 * Use the [FragmentReset.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentReset : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var navController: NavController

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

        }

        return view
    }
}