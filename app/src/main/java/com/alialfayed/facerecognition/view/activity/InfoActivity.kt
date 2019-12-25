package com.alialfayed.facerecognition.view.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.alialfayed.facerecognition.R
import com.alialfayed.facerecognition.viewmodel.InfoViewModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_info.*
import kotlinx.android.synthetic.main.activity_sign_in.*

@Suppress("DEPRECATION")
class InfoActivity : AppCompatActivity() {
    // References of Model class -> this for connection to server and activity
    var selectedPhotoUri: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private lateinit var infoViewModel: InfoViewModel
    private var selectImage :Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        this.infoViewModel =
            ViewModelProviders.of(this, MyViewModelFactory(this)).get(InfoViewModel::class.java)

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        // select profile image
        btnSelectImage_info.setOnClickListener {
            if (infoViewModel.checkPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                infoViewModel.pickImageFromGallery()
                selectImage = true
            } else {
                infoViewModel.takePermission()
            }
        }

        // save information
        btnSave_Info.setOnClickListener {
            if (selectImage){
                infoViewModel.infoSuccessful()
            }else{
                    if (infoViewModel.checkPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        infoViewModel.pickImageFromGallery()
                        selectImage = true
                    } else {
                        infoViewModel.takePermission()
                    }
            }

        }


    }

    // this is method to save image to upload
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            infoViewModel.PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    infoViewModel.pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // this is method to save image to upload
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == infoViewModel.REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK && data != null) {
            // proceed and check what the selected image was....
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            selectphoto_imageview_register.setImageBitmap(bitmap)
        }
    }

    fun disableLayout(status: Boolean) {
        btnSelectImage_info.isEnabled = status
        edtName_Info.isEnabled = status
        edtAge_Info.isEnabled = status
        edtPhone_Info.isEnabled = status
        btnSave_Info.isEnabled = status
        if (!status) {
            progressBar_Info.visibility = View.VISIBLE
        } else {
            progressBar_Info.visibility = View.GONE
        }
    }

    // this is attached by Add Patient Activity and Add Patient ViewModel
    @Suppress("UNCHECKED_CAST")
    internal class MyViewModelFactory(private val infoActivity: InfoActivity) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return InfoViewModel(infoActivity) as T
        }

    }

    // when user go back button show Toast " You Must Insert Data "
    override fun onBackPressed() {
        val  start = Intent(this , InfoActivity::class.java)
        startActivity(start)
        Toast.makeText(this , "You Must Insert Data" , Toast.LENGTH_SHORT).show()
    }
}
