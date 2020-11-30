package com.zywczas.rickmorty.localPhotos.presentation

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.zywczas.rickmorty.R
import com.zywczas.rickmorty.utilities.mainAppBarConfiguration
import com.zywczas.rickmorty.utilities.showSnackbar
import kotlinx.android.synthetic.main.fragment_local_character_list.*
import kotlinx.android.synthetic.main.fragment_local_photos.*
import javax.inject.Inject

class LocalPhotosFragment @Inject constructor() : Fragment(R.layout.fragment_local_photos) {

    private val storageRequestCode by lazy { 1234 }
    private val cameraRequestCode by lazy { 4321 }
    private val cameraPermission by lazy { arrayOf(Manifest.permission.CAMERA) }
    private val storagePermissions by lazy { arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE) }

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
        val builder = AlertDialog.Builder(requireContext()).create()
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_photo, null)
        val camera = dialogView.findViewById<TextView>(R.id.camera_txtView_addPhotoDialog)
        val gallery = dialogView.findViewById<TextView>(R.id.gallery_txtView_addPhotoDialog)
        val cancel = dialogView.findViewById<TextView>(R.id.cancel_txtView_addPhotoDialog)
        camera.setOnClickListener {
            checkCameraPermissionAndTakePhoto()
            builder.dismiss()
        }
        gallery.setOnClickListener {
            checkStoragePermissionAndGetImage()
            builder.dismiss()
        }
        cancel.setOnClickListener { builder.dismiss() }
        builder.setView(dialogView)
        builder.show()
    }

    private fun checkCameraPermissionAndTakePhoto(){
        if (ContextCompat.checkSelfPermission(requireContext().applicationContext, cameraPermission[0]) ==
            PackageManager.PERMISSION_GRANTED){
            takePhotoAndSendToRequestorOnActivityResult()
        } else {
            askForCameraPermissionAndTakePhotoOnPermissionResult()
        }
    }

    private fun takePhotoAndSendToRequestorOnActivityResult(){
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, cameraRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == cameraRequestCode && resultCode == Activity.RESULT_OK){
            //todo pozniej zamienic na temp file i uri
            val bitmap = data?.extras?.get("data") as? Bitmap
            bitmap?.let {
                photoImageView_LocalPhotos.setImageBitmap(it)
                addPhotoTxtView_LocalPhotos.isVisible = false
            }
//            dismiss()
        } else if (requestCode == storageRequestCode && resultCode == Activity.RESULT_OK){
            val selectedImageUri = data?.data
            selectedImageUri?.let {
                photoImageView_LocalPhotos.setImageURI(it)
                addPhotoTxtView_LocalPhotos.isVisible = false
            }
        }
    }

    private fun askForCameraPermissionAndTakePhotoOnPermissionResult(){
        requestPermissions(cameraPermission, cameraRequestCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            cameraRequestCode -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    takePhotoAndSendToRequestorOnActivityResult()
                } else {
                    showSnackbar(R.string.permission_warning)
//                    dismiss()
                }
            }
            storageRequestCode -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getImageFromGalleryAndSendToRequestorOnActivityResult()
                } else {
                    showSnackbar(R.string.permission_warning)
//                    dismiss()
                }
            }
        }
    }

    private fun checkStoragePermissionAndGetImage(){
        if (ContextCompat.checkSelfPermission(requireContext(),storagePermissions[0]) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(requireContext(),storagePermissions[1]) == PackageManager.PERMISSION_GRANTED
        ){
            getImageFromGalleryAndSendToRequestorOnActivityResult()
        } else {
            askForStoragePermissionAndGetImageOnPermissionResult()
        }
    }

    private fun getImageFromGalleryAndSendToRequestorOnActivityResult(){
        val storageIntent = Intent(Intent.ACTION_GET_CONTENT)
        storageIntent.type = "image/*"
        startActivityForResult(storageIntent, storageRequestCode)
    }

    private fun askForStoragePermissionAndGetImageOnPermissionResult(){
        requestPermissions(storagePermissions, storageRequestCode)
    }

}
