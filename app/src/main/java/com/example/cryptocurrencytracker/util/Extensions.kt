package com.example.cryptocurrencytracker.util

import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import kotlin.math.round

fun View.show(){
    this.visibility = View.VISIBLE
}
fun View.hide(){
    this.visibility = View.GONE
}
fun Fragment.findNavControllerSafely(id: Int): NavController? {
    return if (findNavController().currentDestination?.id == id) {
        findNavController()
    } else {
        null
    }
}

fun EditText.isNumber(): Boolean {
    val inputText = this.text.toString()

    val intValue = inputText.toIntOrNull()
    if (intValue != null) {
        return true
    }
    return false
}

fun EditText.isEmailValid(): Boolean {
    val inputText = this.text.toString()
    val emailRegex = Regex("^([A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,})$")
    return emailRegex.matches(inputText)
}

fun String.showToast(context : Context) {
    Toast.makeText(context, this, Toast.LENGTH_LONG).show()
}

fun Double.roundToTwoDecimal(): Double =  round(this * 100) / 100


