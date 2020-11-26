package com.zywczas.rickmorty.viewmodels

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.zywczas.rickmorty.model.repositories.DbRepository
import javax.inject.Inject

class DbVM @Inject constructor(private val repo : DbRepository) : ViewModel() {

    val characters = LiveDataReactiveStreams.fromPublisher(repo.getCharactersFromDb())

}