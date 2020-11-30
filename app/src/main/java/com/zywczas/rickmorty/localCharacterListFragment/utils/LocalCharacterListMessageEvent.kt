package com.zywczas.rickmorty.localCharacterListFragment.utils

import androidx.annotation.StringRes
import com.zywczas.rickmorty.utilities.Event

data class LocalCharacterListMessageEvent constructor(@StringRes private val message: Int) : Event<Int>(message)