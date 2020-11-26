package com.zywczas.rickmorty.utilities

import androidx.annotation.StringRes

data class Resource<out T>(val status: Status, val data: T?, val message: Event<Int>?) {

    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(@StringRes msg: Int, data: T? = null): Resource<T> {
            return Resource(Status.ERROR, data, Event(msg))
        }
    }

}