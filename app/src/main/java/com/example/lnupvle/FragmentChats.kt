package com.example.lnupvle

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

class FragmentChats : Fragment() {

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chats, container, false)

        navController = findNavController()

        val builder = AlertDialog.Builder(context)
        val userPref = requireActivity().getSharedPreferences("UserPref", android.content.Context.MODE_PRIVATE)

        val scheduleButton = view.findViewById<ImageButton>(R.id.schedule_button)
        val lessonButton = view.findViewById<ImageButton>(R.id.lessons_button)
        val settingsButton = view.findViewById<ImageButton>(R.id.settings_button)
        val logoutButton = view.findViewById<ImageButton>(R.id.logout_button)

        scheduleButton.setOnClickListener() {
            navController.navigate(R.id.action_Chats_to_Schedule)
        }

        lessonButton.setOnClickListener() {
            navController.navigate(R.id.action_Chats_to_Main)
        }

        settingsButton.setOnClickListener() {
            navController.navigate((R.id.action_Chats_to_Settings))
        }

        logoutButton.setOnClickListener() {
            builder.setTitle("Підтвердження")
                .setMessage("Ви дійсно бажаєте вийти?")
                .setPositiveButton("Так") { dialog, which ->
                    val editor = userPref.edit()
                    editor.putBoolean("ISLOGGEDIN", false)
                    editor.apply()
                    val temp = userPref.getBoolean("ISLOGGEDIN", false)
                    navController.navigate(R.id.action_Chats_to_Login)
                }
                .setNegativeButton("Ні") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }

        return view
    }
}