package com.zywczas.rickmorty.model.repositories

import androidx.annotation.StringRes
import com.zywczas.rickmorty.utilities.Status

data class ApiResource<out T>(val status: Status, val data: T?, val message: ApiEvent<Int>?) {

    companion object {
        fun <T> success(data: T): ApiResource<T> {
            return ApiResource(Status.SUCCESS, data, null)
        }

        fun <T> error(@StringRes msg: Int, data: T? = null): ApiResource<T> {
            return ApiResource(Status.ERROR, data, ApiEvent(msg))
        }
    }

}