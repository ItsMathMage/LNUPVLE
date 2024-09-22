package com.example.lnupvle.ui.fragments.main

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentContainerView
import com.example.lnupvle.R
import com.example.lnupvle.utilits.navigate
import com.example.lnupvle.utilits.showToast

class FragmentMain : Fragment() {

    private lateinit var chatButton: ImageButton
    private lateinit var scheduleButton: ImageButton
    private lateinit var lessonButton: ImageButton
    private lateinit var settingsButton: ImageButton
    private lateinit var logoutButton: ImageButton

    private lateinit var frame: FragmentContainerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        frame = view.findViewById(R.id.fragmentFrameView)

        chatButton = view.findViewById(R.id.chat_button)
        scheduleButton = view.findViewById(R.id.schedule_button)
        lessonButton = view.findViewById(R.id.lessons_button)
        settingsButton = view.findViewById(R.id.settings_button)
        logoutButton = view.findViewById(R.id.logout_button)

        startPerm()

        chatButton.setOnClickListener() {
            navigate(R.id.action_Main_to_Chats)

        }

        scheduleButton.setOnClickListener() {
            navigate(R.id.action_Main_to_Schedule)
        }

        settingsButton.setOnClickListener() {
            navigate(R.id.action_Main_to_Settings)
        }

        logoutButton.setOnClickListener() {
            logout()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            logout()
        }

        return view
    }

    private val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1

    private fun changeButtons(button: ImageButton) {
        chatButton.setBackgroundResource(R.drawable.transparent_button)
        scheduleButton.setBackgroundResource(R.drawable.transparent_button)
        lessonButton.setBackgroundResource(R.drawable.transparent_button)
        settingsButton.setBackgroundResource(R.drawable.transparent_button)
        logoutButton.setBackgroundResource(R.drawable.transparent_button)

        button.setBackgroundResource(R.drawable.shaded_button)
    }

    private fun startPerm() {
        if (checkWriteExternalStoragePermission()) {

        } else {
            requestWriteExternalStoragePermission()
        }
    }

    private fun logout() {
        val userPref = requireActivity().getSharedPreferences("UserPref", android.content.Context.MODE_PRIVATE)

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Підтвердження")
            .setMessage("Ви дійсно бажаєте вийти?")
            .setPositiveButton("Так") { dialog, which ->
                val editor = userPref.edit()
                editor.putBoolean("ISLOGGEDIN", false)
                editor.apply()
                val temp = userPref.getBoolean("ISLOGGEDIN", false)
                navigate(R.id.action_Main_to_Login)
            }
            .setNegativeButton("Ні") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    private fun checkWriteExternalStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    }

    private fun requestWriteExternalStoragePermission() {
        ActivityCompat.requestPermissions(requireActivity(),
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {

            } else {
                showToast("Дозвіл не отримано")
            }
        }
    }
}