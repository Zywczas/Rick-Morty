package com.zywczas.rickmorty.utilities

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class SingleLiveData<T> : MutableLiveData<T>() {
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner) { genericItem ->
            genericItem?.let {
                observer.onChanged(it)
                postValue(null)
            }
        }
    }
}