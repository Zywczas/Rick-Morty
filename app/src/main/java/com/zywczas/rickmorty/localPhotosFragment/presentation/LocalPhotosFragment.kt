package com.zywczas.rickmorty.localPhotosFragment.presentation

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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.zywczas.rickmorty.R
import com.zywczas.rickmorty.utilities.logD
import com.zywczas.rickmorty.utilities.mainAppBarConfiguration
import com.zywczas.rickmorty.utilities.showSnackbar
import kotlinx.android.synthetic.main.fragment_local_photos.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class LocalPhotosFragment @Inject constructor() : Fragment(R.layout.fragment_local_photos) {

    private val storageRequestCode by lazy { 1234 }
    private val cameraRequestCode by lazy { 4321 }
    private val cameraPermission by lazy { arrayOf(Manifest.permission.CAMERA) }
    private val storagePermissions by lazy { arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE) }
    private var imageBitmap : Bitmap? = null
    private var imageUri : Uri? = null
    private val kBThreshold by lazy { 1024 }
    private val kB by lazy { 1024 }

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

    }

    private fun setupOnClickListeners(){
        photo_LocalPhotos.setOnClickListener{ choosePhotoSource() }
        saveToList_LocalPhotos.setOnClickListener { resizeImageAndAddToList() }
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
            takePhotoAndDisplayOnActivityResult()
        } else {
            getCameraPermissionAndTakePhotoOnPermissionResult()
        }
    }

    private fun takePhotoAndDisplayOnActivityResult(){
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, cameraRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == cameraRequestCode && resultCode == Activity.RESULT_OK){
            val bitmap = data?.extras?.get("data") as? Bitmap
            bitmap?.let {setImageSource(it) }
        } else if (requestCode == storageRequestCode && resultCode == Activity.RESULT_OK){
            val selectedImageUri = data?.data
            selectedImageUri?.let { setImageSource(it) }
        }
    }

    private fun setImageSource(bitmap: Bitmap){
        imageBitmap = bitmap
        imageUri = null
        photo_LocalPhotos.setImageBitmap(bitmap)
        infoAddPhoto_LocalPhotos.isVisible = false
    }

    private fun setImageSource(uri: Uri){
        imageUri = uri
        imageBitmap = null
        photo_LocalPhotos.setImageURI(uri)
        infoAddPhoto_LocalPhotos.isVisible = false
    }

    private fun getCameraPermissionAndTakePhotoOnPermissionResult(){
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
                    takePhotoAndDisplayOnActivityResult()
                } else {
                    showSnackbar(R.string.permission_warning)
                }
            }
            storageRequestCode -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getImageFromGalleryAndDisplayOnActivityResult()
                } else {
                    showSnackbar(R.string.permission_warning)
                }
            }
        }
    }

    private fun checkStoragePermissionAndGetImage(){
        if (ContextCompat.checkSelfPermission(requireContext(),storagePermissions[0]) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(requireContext(),storagePermissions[1]) == PackageManager.PERMISSION_GRANTED
        ){
            getImageFromGalleryAndDisplayOnActivityResult()
        } else {
            getStoragePermissionAndDisplayImageOnPermissionResult()
        }
    }

    private fun getImageFromGalleryAndDisplayOnActivityResult(){
        val storageIntent = Intent(Intent.ACTION_GET_CONTENT)
        storageIntent.type = "image/*"
        startActivityForResult(storageIntent, storageRequestCode)
    }

    private fun getStoragePermissionAndDisplayImageOnPermissionResult(){
        requestPermissions(storagePermissions, storageRequestCode)
    }

    private fun resizeImageAndAddToList(){
        if (imageBitmap != null) {
            progressBar_LocalPhotos.isVisible = true
            resizeBitmapAndAddToList(imageBitmap!!)
        } else if (imageUri != null){
            progressBar_LocalPhotos.isVisible = true
//            resizeImageFromUriAndAddToList(imageUri)
        } else {
            showSnackbar(R.string.no_photo_to_save)
        }
    }

    private fun resizeBitmapAndAddToList(bitmap: Bitmap){
        lifecycleScope.launch {
            val resized = resizeBitmapToByteArray(bitmap)

            progressBar_LocalPhotos.isVisible = false
        }

    }

    private suspend fun resizeBitmapToByteArray(bitmap: Bitmap) : ByteArray?{
        return withContext(Dispatchers.IO){
            var bytes : ByteArray? = null
            for (i in 1..10) {
                val quality = 100 - (i*10)
                bytes = getBytesFromBitmap(bitmap, quality)
                logD("rozmiar konwertowanego zdjecia: ($quality%): ${bytes.size/kB} kB")
                if (bytes.size/kB < kBThreshold) {
                    break
                }
                if (i == 10){
                    bytes = null
                    showSnackbar(R.string.too_big_photo)
                }
            }
            logD("ostateczny rozmiar zdjecia: ${bytes?.size?.div(kB)}")
            bytes
        }
    }

    private fun getBytesFromBitmap(bitmap: Bitmap, quality: Int) : ByteArray{
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
        return stream.toByteArray()
    }

}
