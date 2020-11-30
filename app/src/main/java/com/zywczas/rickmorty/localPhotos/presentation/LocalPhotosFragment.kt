package com.zywczas.rickmorty.localPhotos.presentation

import android.app.AlertDialog
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.zywczas.rickmorty.R
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
        val appBarConfig =
            AppBarConfiguration(setOf(R.id.destination_LocalCharacterList, R.id.destination_OnlineCharacterList,
                R.id.destination_LocalPhotosFragment), drawerLayout_localCharacterList)
        navDrawer_localPhotos.setupWithNavController(navController)
        toolbar_localPhotos.setupWithNavController(navController, appBarConfig)
    }

    private fun setupRecyclerView(){
        //todo
    }

    private fun setupOnClickListeners(){
        photoImageView_LocalPhotos.setOnClickListener(saveImageOnClickListener)
    }

    private val saveImageOnClickListener = View.OnClickListener {
        showSaveConfirmationDialog()
    }

    private fun showSaveConfirmationDialog(){

    }

    override fun getImagePath(path: Uri) {
        TODO("Not yet implemented")
    }

    override fun getImageBitmap(bitmap: Bitmap) {
        TODO("Not yet implemented")
    }

//    private fun showSaveConfirmationDialog(){
//        val builder = AlertDialog.Builder(requireContext()).create()
//        val dialogView = layoutInflater.inflate(R.layout.dialog_add_photo, null)
//        val camera = dialogView.findViewById<TextView>(R.id.camera_txtView_saveImageDialog)
//        val gallery = dialogView.findViewById<TextView>(R.id.gallery_txtView_saveImageDialog)
//        val cancel = dialogView.findViewById<TextView>(R.id.cancel_txtView_dialog)
//        camera.setOnClickListener {
//            checkCameraPermissionAndSaveImageOnPermissionResult()
//            builder.dismiss()
//        }
//        gallery.setOnClickListener {
//            checkStoragePermissionAndSaveImageOnPermissionResult()
//            builder.dismiss()
//        }
//        cancel.setOnClickListener { builder.dismiss() }
//        builder.setView(dialogView)
//        builder.show()
//    }

}
