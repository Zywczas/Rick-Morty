package com.zywczas.rickmorty.detailsFragment.utils

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 */
class DetailsEvent<out T> constructor(private val content: T) {

    private var hasBeenHandled = false

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

}