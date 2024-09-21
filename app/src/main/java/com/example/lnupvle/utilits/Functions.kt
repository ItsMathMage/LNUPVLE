package com.example.lnupvle.utilits

import android.widget.Toast
import androidx.fragment.app.Fragment

private lateinit var TEST: String

fun Fragment.showToast(message: String) {
    Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
}