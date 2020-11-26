package com.zywczas.rickmorty.viewmodels

import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.zywczas.rickmorty.R
import com.zywczas.rickmorty.model.Character
import com.zywczas.rickmorty.model.repositories.DetailsRepository
import com.zywczas.rickmorty.utilities.Event
import com.zywczas.rickmorty.utilities.lazyAndroid
import com.zywczas.rickmorty.utilities.logD
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.Exception

class DetailsVM @Inject constructor(private val repo: DetailsRepository) : ViewModel() {

    private val _isCharacterInFavourites by lazyAndroid { MediatorLiveData<Boolean>() }
    val isCharacterInFavourites : LiveData<Boolean> by lazyAndroid { _isCharacterInFavourites }
    private val _message by lazyAndroid { MutableLiveData<Event<@StringRes Int>>() }
    val message : LiveData<Event<Int>> by lazyAndroid { _message }
    private var isCheckInitialized = false

    fun checkIfIsInList(charId : Int) {
        if (isCheckInitialized.not()) {
            isCheckInitialized = true
            startCheckingIfIsInList(charId)
        }
    }

    private fun startCheckingIfIsInList(charId : Int) {
        viewModelScope.launch {
            try {
                val source = LiveDataReactiveStreams.fromPublisher(repo.isCharacterInDb(charId))
                _isCharacterInFavourites.addSource(source){
                    _isCharacterInFavourites.value = it
                }
            } catch (e: Exception) {
                logD(e)
                _message.value = Event(R.string.id_count_verify_error)
            }
        }
    }

    fun addOrRemoveCharacterFromFavourites(character : Character, isInFavourites : Boolean){
        viewModelScope.launch {
            try {
                when (isInFavourites){
                    true -> removeCharacterFromFavourites(character)
                    false -> addCharacterToFavourites(character)
                }
            } catch (e: Exception){
                logD(e)
                _message.value = Event(R.string.other_database_error)
            }
        }
    }

    private suspend fun removeCharacterFromFavourites(character : Character){
        val result = repo.removeCharacterFromDb(character)
        _message.value = result
    }

    private suspend fun addCharacterToFavourites(character : Character){
        val result = repo.addCharacterToDb(character)
        _message.value = result
    }



}