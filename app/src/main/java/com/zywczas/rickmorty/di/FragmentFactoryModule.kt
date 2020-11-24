package com.zywczas.rickmorty.di

import androidx.fragment.app.Fragment
import com.zywczas.rickmorty.views.ApiFragment
import com.zywczas.rickmorty.views.DBFragment
import com.zywczas.rickmorty.views.DetailsFragment
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