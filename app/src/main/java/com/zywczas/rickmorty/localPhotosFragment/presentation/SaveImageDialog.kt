package com.zywczas.rickmorty.localPhotosFragment.presentation

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.zywczas.rickmorty.R
import com.zywczas.rickmorty.utilities.showSnackbar
import kotlinx.android.synthetic.main.dialog_add_photo.*

//todo pytanie do Michała:
//czy mozna uzywac takiej klasy jak ta ponizej, zeby zarzadzala pobieraniem zdjecia i pozniej jakos
//sensownie przekazac to do fragmentu LocalPhotosFragment? Pewnie musialoby leciec przez activity.
//Czy lepiej cala ta logike trzymac w 1 fragmencie? Duzo linijek kodu sie robi. Jak do tego jeszcze
//doda sie zapisywanie zdjecia do Room'a to juz wogole bedzie potezny LocalPhotosFragment
class SaveImageDialog : DialogFragment(){

    companion object{


    }

    //todo jako const val request code i camera permission
    private val storageRequestCode by lazy { 1234 }
    private val cameraRequestCode by lazy { 4321 }
    private val cameraPermission by lazy { arrayOf(Manifest.permission.CAMERA) }
    private val storagePermissions by lazy { arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE) }
    private lateinit var onPhotoReceivedListener : OnPhotoReceivedListener

    interface OnPhotoReceivedListener {
        fun getImagePath(path : Uri)
        fun getImageBitmap(bitmap: Bitmap)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onPhotoReceivedListener = context as OnPhotoReceivedListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.dialog_add_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnClickListeners()
    }

    private fun setupOnClickListeners(){
        camera_txtView_addPhotoDialog.setOnClickListener {
            checkCameraPermissionAndTakePhoto()
        }
        gallery_txtView_addPhotoDialog.setOnClickListener {
            checkStoragePermissionAndGetImage()
        }
        cancel_txtView_addPhotoDialog.setOnClickListener { dismiss() }
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
            val bitmap = data?.extras?.get("data") as? Bitmap
            bitmap?.let { onPhotoReceivedListener.getImageBitmap(it) }
            dismiss()
        } else if (requestCode == storageRequestCode && resultCode == Activity.RESULT_OK){
            val selectedImageUri = data?.data
            selectedImageUri?.let { onPhotoReceivedListener.getImagePath(it) }
            dismiss()
        } else {
            dismiss()
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
                    dismiss()
                }
            }
            storageRequestCode -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getImageFromGalleryAndSendToRequestorOnActivityResult()
                } else {
                    showSnackbar(R.string.permission_warning)
                    dismiss()
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