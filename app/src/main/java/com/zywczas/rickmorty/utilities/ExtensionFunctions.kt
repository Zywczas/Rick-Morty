package com.zywczas.rickmorty.utilities

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.customview.widget.Openable
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.snackbar.Snackbar
import com.zywczas.rickmorty.R

@Suppress("SpellCheckingInspection")
fun Fragment.showSnackbar(@StringRes msg: Int) {
    val color = ContextCompat.getColor(requireContext(), R.color.darkGrey)
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

fun mainAppBarConfiguration(drawer : Openable? = null) = AppBarConfiguration(setOf(R.id.destination_LocalCharacterList, R.id.destination_OnlineCharacterList,
R.id.destination_LocalPhotosFragment), drawer)

//todo dodac pagination w DB module
//todo spawdzic czy diff util w LocalCharacterListFragment da sie ustawic tak zeby nie przeskakiwal na poczatek listy
