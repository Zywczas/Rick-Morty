package com.zywczas.rickmorty.localCharacterListFragment.presentation

import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.lifecycle.*
import com.zywczas.rickmorty.R
import com.zywczas.rickmorty.SessionManager
import com.zywczas.rickmorty.localCharacterListFragment.domain.LocalCharacterListRepository
import com.zywczas.rickmorty.model.Character
import com.zywczas.rickmorty.utilities.SingleLiveData
import kotlinx.android.synthetic.main.fragment_local_character_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalCharacterListViewModel @Inject constructor(
    private val repo : LocalCharacterListRepository,
    private val session : SessionManager
) : ViewModel() {

    init {
        viewModelScope.launch {
            checkConnection()
        }
    }

    private val _message = SingleLiveData<@StringRes Int>()
    val message : LiveData<Int> = _message

    private val _characters = MutableLiveData<List<Character>>()
    val characters : LiveData<List<Character>> = _characters

    private val _isDataBaseEmpty = MutableLiveData<Boolean>()
    val isDataBaseEmpty : LiveData<Boolean> = _isDataBaseEmpty

    suspend fun updateUI(){
        withContext(Dispatchers.IO) {
            val result = repo.getAllCharacters()
            _characters.postValue(result)
            if (result.isEmpty()) {
                _isDataBaseEmpty.postValue(true)
            } else {
                _isDataBaseEmpty.postValue(false)
            }
        }
    }

    private suspend fun checkConnection(){
        withContext(Dispatchers.IO){
            if (session.isConnected.not()){
                _message.postValue(R.string.connection_error)
            }
        }
    }

}