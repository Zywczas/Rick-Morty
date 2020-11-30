package com.zywczas.rickmorty.localPhotos.presentation

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.zywczas.rickmorty.R
import com.zywczas.rickmorty.utilities.mainAppBarConfiguration
import kotlinx.android.synthetic.main.fragment_local_character_list.*
import kotlinx.android.synthetic.main.fragment_local_photos.*
import javax.inject.Inject

class LocalPhotosFragment @Inject constructor() : Fragment(R.layout.fragment_local_photos), SaveImageDialog.OnPhotoReceivedListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigationUI()
        setupRecyclerView()
        setupOnClickListeners()
    }

    private fun setupNavigationUI(){
        val navController = findNavController()
        val appBarConfig = mainAppBarConfiguration(drawerLayout_localPhotos)
        navDrawer_localPhotos.setupWithNavController(navController)
        toolbar_localPhotos.setupWithNavController(navController, appBarConfig)
    }

    private fun setupRecyclerView(){
        //todo
    }

    private fun setupOnClickListeners(){
        photoImageView_LocalPhotos.setOnClickListener{ choosePhotoSource() }
    }

    private fun choosePhotoSource(){
        SaveImageDialog().show(childFragmentManager, "")
    }

    override fun getImagePath(path: Uri) {
        photoImageView_LocalPhotos.setImageURI(path)
    }

    override fun getImageBitmap(bitmap: Bitmap) {
        photoImageView_LocalPhotos.setImageBitmap(bitmap)
    }

}
