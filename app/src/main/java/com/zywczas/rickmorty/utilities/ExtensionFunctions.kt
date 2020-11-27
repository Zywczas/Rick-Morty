package com.zywczas.rickmorty.utilities

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.showToast(message: String) =
    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()

private const val tag = "RickAndMorty"
fun logD(msg : String) = Log.d(tag, msg)
fun logD(e : Throwable) = Log.d(tag, "${e.message}")

//todo dodac pagination w DB module
//todo dac wczytywanie zdjec z kamery
//todo podzielic wszystkie Resource na pojedyncze dla kazdego modulu