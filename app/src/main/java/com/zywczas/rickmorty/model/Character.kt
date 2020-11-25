package com.zywczas.rickmorty.model

import android.os.Parcelable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.RequestManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.zywczas.rickmorty.R
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Character (
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: String,
    val location: String,
    val imageUrl: String?,
    val created: String
) : Parcelable