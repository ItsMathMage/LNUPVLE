package com.example.lnupvle.ui.fragments.schedule

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.lnupvle.R
import com.example.lnupvle.utilits.navigate

class FragmentSchedule : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_schedule, container, false)

        val builder = AlertDialog.Builder(context)
        val userPref = requireActivity().getSharedPreferences("UserPref", android.content.Context.MODE_PRIVATE)

        val chatButton = view.findViewById<ImageButton>(R.id.chat_button)
        val lessonButton = view.findViewById<ImageButton>(R.id.lessons_button)
        val settingsButton = view.findViewById<ImageButton>(R.id.settings_button)
        val logoutButton = view.findViewById<ImageButton>(R.id.logout_button)

        chatButton.setOnClickListener() {
            navigate(R.id.action_Schedule_to_Chats)
        }

        lessonButton.setOnClickListener() {
            navigate(R.id.action_Schedule_to_Main)
        }

        settingsButton.setOnClickListener() {
            navigate((R.id.action_Schedule_to_Settings))
        }

        logoutButton.setOnClickListener() {
            builder.setTitle("Підтвердження")
                .setMessage("Ви дійсно бажаєте вийти?")
                .setPositiveButton("Так") { dialog, which ->
                    val editor = userPref.edit()
                    editor.putBoolean("ISLOGGEDIN", false)
                    editor.apply()
                    val temp = userPref.getBoolean("ISLOGGEDIN", false)
                    navigate(R.id.action_Schedule_to_Login)
                }
                .setNegativeButton("Ні") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
        return view
    }
}