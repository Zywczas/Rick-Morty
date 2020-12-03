package com.zywczas.rickmorty.utilities

import com.bumptech.glide.request.RequestOptions
import com.zywczas.rickmorty.R

val glideRequestOptions = RequestOptions()
    .placeholder(R.drawable.white_background)
    .error(R.drawable.error_image)