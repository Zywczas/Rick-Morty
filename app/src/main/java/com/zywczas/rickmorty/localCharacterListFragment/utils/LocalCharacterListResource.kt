package com.zywczas.rickmorty.localCharacterListFragment.utils

import androidx.annotation.StringRes
import com.zywczas.rickmorty.model.Character

data class LocalCharacterListResource(val status: LocalCharacterListStatus, val data: List<Character>?, val message: LocalCharacterListMessageEvent?) {

    companion object {
        fun success(data: List<Character>): LocalCharacterListResource {
            return LocalCharacterListResource(LocalCharacterListStatus.SUCCESS, data, null)
        }

        fun error(@StringRes msg: Int, data: List<Character>? = null): LocalCharacterListResource {
            return LocalCharacterListResource(LocalCharacterListStatus.ERROR, data, LocalCharacterListMessageEvent(msg))
        }
    }

}