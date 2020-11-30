package com.zywczas.rickmorty.di

import androidx.lifecycle.ViewModel
import com.zywczas.rickmorty.onlineCharacterListFragment.presentation.OnlineCharacterListViewModel
import com.zywczas.rickmorty.localCharacterListFragment.presentation.LocalCharacterListViewModel
import com.zywczas.rickmorty.detailsFragment.presentation.DetailsVM
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelFactoryModule {

    @Binds
    @IntoMap
    @ViewModelKey(DetailsVM::class)
    abstract fun bindDetailsVM(vm: DetailsVM) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OnlineCharacterListViewModel::class)
    abstract fun bindApiVM(vm: OnlineCharacterListViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LocalCharacterListViewModel::class)
    abstract fun bindDbVM(vm: LocalCharacterListViewModel) : ViewModel
}