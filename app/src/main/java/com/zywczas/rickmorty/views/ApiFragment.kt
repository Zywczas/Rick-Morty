package com.zywczas.rickmorty.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.zywczas.rickmorty.R
import kotlinx.android.synthetic.main.fragment_db.*
import javax.inject.Inject

class ApiFragment @Inject constructor() : Fragment(R.layout.fragment_api) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        val appBarConfig =
            AppBarConfiguration(setOf(R.id.destination_Db, R.id.destination_Db), drawerLayout_Db)
        .setupWithNavController(navController)
        .setupWithNavController(navController, appBarConfig)
    }

}