package com.zywczas.rickmorty.di

import androidx.fragment.app.Fragment
import com.zywczas.rickmorty.apiFragment.presentation.ApiFragment
import com.zywczas.rickmorty.dbFragment.presentation.DBFragment
import com.zywczas.rickmorty.detailsFragment.presentation.DetailsFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FragmentFactoryModule {

    @Binds
    @IntoMap
    @FragmentKey(DBFragment::class)
    abstract fun bindDBFragment(dbFragment: DBFragment) : Fragment

    @Binds
    @IntoMap
    @FragmentKey(ApiFragment::class)
    abstract fun bindApiFragment(apiFragment: ApiFragment) : Fragment

    @Binds
    @IntoMap
    @FragmentKey(DetailsFragment::class)
    abstract fun bindDetailsFragment(detailsFragment: DetailsFragment) : Fragment

}