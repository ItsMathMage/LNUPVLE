package com.example.lnupvle.ui.fragments.main

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.lnupvle.R
import com.example.lnupvle.utilits.navigate
import com.example.lnupvle.utilits.showToast

class FragmentMain : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        val chatButton = view.findViewById<ImageButton>(R.id.chat_button)
        val scheduleButton = view.findViewById<ImageButton>(R.id.schedule_button)
        val settingsButton = view.findViewById<ImageButton>(R.id.settings_button)
        val logoutButton = view.findViewById<ImageButton>(R.id.logout_button)

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

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            logout()
        }

        logoutButton.setOnClickListener() {
            logout()
        }

        return view
    }

    private val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1

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