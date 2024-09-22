package com.example.lnupvle.utilits

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

fun Fragment.showToast(message: String) {
    Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.navigate(path: Int) {
    val appNavController: NavController = findNavController()
    appNavController.navigate(path)
}

fun Fragment.getNav() : NavController {
    val appNav: NavController = findNavController()
    return appNav
}