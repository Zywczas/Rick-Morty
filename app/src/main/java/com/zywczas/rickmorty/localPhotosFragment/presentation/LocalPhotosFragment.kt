package com.zywczas.rickmorty.localPhotosFragment.presentation

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.zywczas.rickmorty.R
import com.zywczas.rickmorty.factories.UniversalViewModelFactory
import com.zywczas.rickmorty.localCharacterListFragment.adapter.LocalCharacterListItem
import com.zywczas.rickmorty.localPhotosFragment.adapter.LocalPhotosItem
import com.zywczas.rickmorty.model.Photo
import com.zywczas.rickmorty.utilities.attachAppBarConfiguration
import com.zywczas.rickmorty.utilities.logD
import com.zywczas.rickmorty.utilities.showSnackbar
import kotlinx.android.synthetic.main.fragment_local_character_list.*
import kotlinx.android.synthetic.main.fragment_local_photos.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class LocalPhotosFragment @Inject constructor(
    private val viewModelFactory : UniversalViewModelFactory
) : Fragment(R.layout.fragment_local_photos) {

    private val viewModel : LocalPhotosViewModel by viewModels { viewModelFactory }
    private val itemAdapter by lazy { ItemAdapter<LocalPhotosItem>() }
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
        setupObservers()
        setupOnClickListeners()
    }

    private fun setupNavigationUI(){
        val navController = findNavController()
        val appBarConfig = drawerLayout_localPhotos.attachAppBarConfiguration()
        navDrawer_localPhotos.setupWithNavController(navController)
        toolbar_localPhotos.setupWithNavController(navController, appBarConfig)
    }

    private fun setupRecyclerView(){
        setupRvAdapter()
        setupRvLayoutManager()
        recyclerView_localPhotos.setHasFixedSize(true)
    }

    private fun setupRvAdapter(){
        val fastAdapter = FastAdapter.with(itemAdapter)
        recyclerView_localPhotos.adapter = fastAdapter
    }

    private fun setupRvLayoutManager(){
        var spanCount = 2
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            spanCount = 4
        }
        val layoutManager = GridLayoutManager(requireContext(), spanCount)
        recyclerView_localPhotos.layoutManager = layoutManager
    }

    private fun setupObservers(){
        setupMessageObserver()
        setupPhotosObserver()
    }

    private fun setupMessageObserver(){
        viewModel.message.observe(viewLifecycleOwner) {
            showSnackbar(it)
            showProgressBar(false)
        }
    }

    private fun showProgressBar(visible : Boolean){
        progressBar_LocalPhotos.isVisible = visible
    }

    private fun setupPhotosObserver(){
        viewModel.photos.observe(viewLifecycleOwner){
            updateUI(it)
            showProgressBar(false)
        }
    }

    private fun updateUI(photos : List<Photo>){
        addToRecyclerView(photos)
    }

    private fun addToRecyclerView(photos : List<Photo>){
        val items = mutableListOf<LocalPhotosItem>()
        photos.forEach {
            val item = LocalPhotosItem(it)
            items.add(item)
        }
        itemAdapter.add(items)
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

    //todo to chyba trzeba zamienic na intent ktory pozwala wybrac kamere
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

    //todo obraca mi zdjecie z galerii po zaladowanu - nie powinno
    private fun getImageFromGalleryAndDisplayOnActivityResult(){
        val storageIntent = Intent(Intent.ACTION_GET_CONTENT)
        storageIntent.type = "image/*"
        startActivityForResult(storageIntent, storageRequestCode)
    }

    private fun getStoragePermissionAndDisplayImageOnPermissionResult(){
        requestPermissions(storagePermissions, storageRequestCode)
    }

    private fun resizeImageAndAddToList(){
        lifecycleScope.launch {
            if (imageBitmap != null) {
                showProgressBar(true)
                resizeBitmapAndAddToList(imageBitmap!!)
            } else if (imageUri != null){
                showProgressBar(true)
                resizeImageFromUriAndAddToList(imageUri!!)
            } else {
                showSnackbar(R.string.no_photo_to_save)
            }
        }
    }

    private suspend fun resizeBitmapAndAddToList(bitmap: Bitmap){
        withContext(Dispatchers.IO) {
            val resized = resizeBitmapAndToByteArray(bitmap)
            resized?.let { saveToList(it) }
        }
    }

    private fun resizeBitmapAndToByteArray(bitmap: Bitmap) : ByteArray?{
        var bytes : ByteArray? = null
            for (i in 1..10) {
                val quality = 100 - (i*10)
                bytes = toByteArray(bitmap, quality)
                if (bytes.size/kB < kBThreshold) {
                     break
                } else if (i == 10){
                    showSavingImageError(R.string.too_big_photo)
                    bytes = null
                }
            }
        return bytes
    }

    private fun toByteArray(bitmap: Bitmap, quality: Int) : ByteArray{
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
        return stream.toByteArray()
    }

    private fun showSavingImageError(@StringRes msg: Int){
        showProgressBar(false)
        showSnackbar(msg)
    }

    private suspend fun saveToList(image : ByteArray){
        val name = "some name for now"
        val timeStamp = "some time"
        val photo = Photo(0, name, timeStamp, image)
        viewModel.addPhotoToList(photo)
    }

    private suspend fun resizeImageFromUriAndAddToList(uri: Uri){
        withContext(Dispatchers.IO){
            val bitmap = toBitmap(uri)
            if (bitmap != null){
                val resized = resizeBitmapAndToByteArray(bitmap)
                resized?.let { saveToList(it) }
            } else {
                showSavingImageError(R.string.operation_error)
            }
        }
    }

    private fun toBitmap(uri: Uri) : Bitmap? {
        return try {
            val parcelFileDescriptor = requireContext().contentResolver.openFileDescriptor(uri, "r")
            val fileDescriptor = parcelFileDescriptor?.fileDescriptor
            BitmapFactory.decodeFileDescriptor(fileDescriptor)
        } catch (e: Exception) {
            logD(e)
            null
        }
    }





    //todo dac to w message observer progressBar_LocalPhotos.isVisible = false

}
