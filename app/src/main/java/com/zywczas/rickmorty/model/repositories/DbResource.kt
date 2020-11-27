package com.zywczas.rickmorty.model.repositories

import androidx.annotation.StringRes
import com.zywczas.rickmorty.utilities.Status

data class DbResource<out T>(val status: Status, val data: T?, val message: DbEvent<Int>?) {

    companion object {
        fun <T> success(data: T): DbResource<T> {
            return DbResource(Status.SUCCESS, data, null)
        }

        fun <T> error(@StringRes msg: Int, data: T? = null): DbResource<T> {
            return DbResource(Status.ERROR, data, DbEvent(msg))
        }
    }

}