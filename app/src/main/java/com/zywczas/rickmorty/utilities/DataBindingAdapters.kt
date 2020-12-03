package com.zywczas.rickmorty.utilities

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("android:src")
fun setImage(view: ImageView, url: String) {
    Glide.with(view.context)
        .setDefaultRequestOptions(glideRequestOptions)
        .load(url)
        .into(view)
}
