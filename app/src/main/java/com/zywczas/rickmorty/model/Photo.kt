package com.zywczas.rickmorty.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class Photo (

    val id: Long = 0,

    val name: String,

    val timeStamp: String,

    val photoByteArray: ByteArray

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Photo

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}