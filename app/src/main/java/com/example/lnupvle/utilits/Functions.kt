package com.example.lnupvle.utilits

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
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

fun Fragment.setGraph(fragment: Fragment, view: View, host: Int, path: Int) {
    val navHostFragment = fragment.childFragmentManager.findFragmentById(host) as NavHostFragment
    val navController = navHostFragment.navController

    navController.graph = Navigation.findNavController(view)
        .graph
        .apply {
            (path)
        }
}