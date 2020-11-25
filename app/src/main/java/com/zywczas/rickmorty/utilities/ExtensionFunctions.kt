package com.zywczas.rickmorty.utilities

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.showToast(message: String) =
    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()

fun <T> lazyAndroid(initializer: () -> T) : Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)

private const val tag = "RickAndMorty"
fun logD(msg : String) = Log.d(tag, msg)
fun logD(e : Throwable) = Log.d(tag, "${e.message}")

//todo dac room
//todo dodac pagination
//todo prawdopodobnie zamienic Api modul na przesylanie tylko po 20 postaci bo w ApiFragment moge dodawac kolejne
//todo dac wczytywanie zdjec z kamery
//todo dac details fragment