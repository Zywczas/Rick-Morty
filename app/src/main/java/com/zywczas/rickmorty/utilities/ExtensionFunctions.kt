package com.zywczas.rickmorty.utilities

import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.zywczas.rickmorty.R

fun Fragment.showToast(message: String) =
    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()

fun Fragment.showSnackbar(@StringRes msg: Int) {
    val color = ContextCompat.getColor(requireContext(), R.color.darkGrey)
    @Suppress("SpellCheckingInspection")
    val snackbar = Snackbar.make(requireView(), getString(msg), Snackbar.LENGTH_LONG)
        .setBackgroundTint(color)
    val view = snackbar.view
    val textView = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
    textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
    snackbar.show()
}

private const val tag = "RickAndMorty"
fun logD(msg : String) = Log.d(tag, msg)
fun logD(e : Throwable) = Log.d(tag, "${e.message}")

//todo dodac pagination w DB module
//todo dac wczytywanie zdjec z kamery