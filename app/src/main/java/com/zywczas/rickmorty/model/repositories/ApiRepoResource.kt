package com.zywczas.rickmorty.model.repositories

import androidx.annotation.StringRes
import com.zywczas.rickmorty.utilities.Status

data class ApiRepoResource<out T>(val status: Status, val data: T?, val message: ApiEvent<Int>?) {

    companion object {
        fun <T> success(data: T): ApiRepoResource<T> {
            return ApiRepoResource(Status.SUCCESS, data, null)
        }

        fun <T> error(@StringRes msg: Int, data: T? = null): ApiRepoResource<T> {
            return ApiRepoResource(Status.ERROR, data, ApiEvent(msg))
        }
    }

}