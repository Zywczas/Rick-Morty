package com.zywczas.rickmorty.viewmodels

import androidx.lifecycle.ViewModel
import com.zywczas.rickmorty.SessionManager
import com.zywczas.rickmorty.model.repositories.ApiRepository
import javax.inject.Inject

class ApiVM @Inject constructor(
    private val repo : ApiRepository,
    private val session : SessionManager
) : ViewModel() {



}