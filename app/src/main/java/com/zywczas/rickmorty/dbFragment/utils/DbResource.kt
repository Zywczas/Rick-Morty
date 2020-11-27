package com.zywczas.rickmorty.dbFragment.utils

import androidx.annotation.StringRes
//todo tu nie dawac generic
data class DbResource<out T>(val status: DbStatus, val data: T?, val message: DbEvent<Int>?) {

    companion object {
        fun <T> success(data: T): DbResource<T> {
            return DbResource(DbStatus.SUCCESS, data, null)
        }

        fun <T> error(@StringRes msg: Int, data: T? = null): DbResource<T> {
            return DbResource(DbStatus.ERROR, data, DbEvent(msg))
        }
    }

}