package com.zywczas.rickmorty.onlineCharacterListFragment.utils

import androidx.annotation.StringRes
import com.zywczas.rickmorty.model.Character

data class OnlineCharacterListResource(val status: OnlineCharacterListStatus, val data: List<Character>?, val message: MessageEvent?) {

    companion object {
        fun success(data: List<Character>): OnlineCharacterListResource {
            return OnlineCharacterListResource(OnlineCharacterListStatus.SUCCESS, data, null)
        }

        fun error(@StringRes msg: Int, data: List<Character>? = null): OnlineCharacterListResource {
            return OnlineCharacterListResource(OnlineCharacterListStatus.ERROR, data, MessageEvent(msg))
        }
    }

}