package com.zywczas.rickmorty.di

import androidx.lifecycle.ViewModel
import com.zywczas.rickmorty.apimodule.ApiVM
import com.zywczas.rickmorty.dbmodule.DbVM
import com.zywczas.rickmorty.detailsmodule.DetailsVM
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelFactoryModule {

    @Binds
    @IntoMap
    @ViewModelKey(DetailsVM::class)
    abstract fun bindDetailsVM(detailsVM: DetailsVM) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ApiVM::class)
    abstract fun bindApiVM(apiVM: ApiVM) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DbVM::class)
    abstract fun bindDbVM(dbVM: DbVM) : ViewModel
}