package com.zywczas.rickmorty.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.zywczas.rickmorty.R
import com.zywczas.rickmorty.SessionManager
import com.zywczas.rickmorty.utilities.lazyAndroid
import kotlinx.android.synthetic.main.fragment_db.*
import javax.inject.Inject

class DBFragment @Inject constructor (private val session: SessionManager) : Fragment(R.layout.fragment_db) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        val appBarConfig =
            AppBarConfiguration(setOf(R.id.destination_Db, R.id.destination_Api), drawerLayout_Db)
        navDrawer_Db.setupWithNavController(navController)
        toolbar_Db.setupWithNavController(navController, appBarConfig)
    }

}