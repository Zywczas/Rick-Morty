package com.zywczas.rickmorty.model.repositories

import androidx.annotation.StringRes

data class ApiResource<out T>(val status: ApiStatus, val data: T?, val message: ApiEvent<Int>?) {

    companion object {
        fun <T> success(data: T): ApiResource<T> {
            return ApiResource(ApiStatus.SUCCESS, data, null)
        }

        fun <T> error(@StringRes msg: Int, data: T? = null): ApiResource<T> {
            return ApiResource(ApiStatus.ERROR, data, ApiEvent(msg))
        }
    }

}