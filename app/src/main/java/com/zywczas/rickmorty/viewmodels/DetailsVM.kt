package com.zywczas.rickmorty.viewmodels

import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.zywczas.rickmorty.R
import com.zywczas.rickmorty.model.Character
import com.zywczas.rickmorty.model.repositories.DetailsRepository
import com.zywczas.rickmorty.utilities.Event
import com.zywczas.rickmorty.utilities.logD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DetailsVM @Inject constructor(
    private val repo: DetailsRepository,
    private val dispatchers : Dispatchers
) : ViewModel() {

    private val _isCharacterInFavourites = MediatorLiveData<Boolean>()
    val isCharacterInFavourites : LiveData<Boolean> = _isCharacterInFavourites

    private val _message = MutableLiveData<Event<@StringRes Int>>()
    val message : LiveData<Event<Int>> = _message

    //todo pytanie do Michala: czy to powinno byc w try {...} i catch {..}?
    suspend fun checkIfIsInList(charId : Int) {
        withContext(dispatchers.IO){
            _isCharacterInFavourites.postValue(repo.isCharacterInDb(charId))
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