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

    var selectedPhotoUri: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private lateinit var infoViewModel: InfoViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        this.infoViewModel =
            ViewModelProviders.of(this, MyViewModelFactory(this)).get(InfoViewModel::class.java)

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        btnSelectImage_info.setOnClickListener {
            if (infoViewModel.checkPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                infoViewModel.pickImageFromGallery()
            } else {
                infoViewModel.takePermission()
            }
        }

        btnSave_Info.setOnClickListener {
            infoViewModel.infoSuccessful()
        }


    }

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

//    fun uploadImageToFirebaseStorage() {
//        if (selectedPhotoUri == null) return
//
//        val filename = UUID.randomUUID().toString()
//        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
//
//        ref.putFile(selectedPhotoUri!!)
//            .addOnSuccessListener {
//                Log.d(TAG, "Successfully uploaded image: ${it.metadata?.path}")
//                ref.downloadUrl.addOnSuccessListener {
//                    Log.d(TAG, "File Location: $it")
//                    saveUserToFirebaseDatabase(it.toString())
//                }
//            }
//            .addOnFailureListener {
//                Log.d(TAG, "Failed to upload image to storage: ${it.message}")
//            }
//    }

//    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {
//        val uid = FirebaseAuth.getInstance().uid ?: ""
//        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
//
//        val user = User(uid, edtName_Info.text.toString(), profileImageUrl)
//
//        ref.setValue(user)
//            .addOnSuccessListener {
//                Log.d(TAG, "Finally we saved the user to Firebase Database")
//            }
//            .addOnFailureListener {
//                Log.d(TAG, "Failed to set value to database: ${it.message}")
//            }
//    }
//
//    class User(val uid: String, val username: String, val profileImageUrl: String)


    @Suppress("UNCHECKED_CAST")
    internal class MyViewModelFactory(private val infoActivity: InfoActivity) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return InfoViewModel(infoActivity) as T
        }

    }
}
