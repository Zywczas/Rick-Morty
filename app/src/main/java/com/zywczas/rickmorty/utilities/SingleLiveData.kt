package com.zywczas.rickmorty.utilities

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

//todo sprawdzic to
open class SingleLiveData<T> : MediatorLiveData<T>() {
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner) { genericItem ->
            genericItem?.let {
                observer.onChanged(it)
                postValue(null)
            }
        }
    }
}