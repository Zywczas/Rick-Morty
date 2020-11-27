package com.zywczas.rickmorty.onlineCharacterListFragment.utils

import androidx.annotation.StringRes
import com.zywczas.rickmorty.utilities.Event

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 */
data class MessageEvent (@StringRes private val message: Int) : Event<Int>(message)