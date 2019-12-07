package com.alialfayed.facerecognition.viewmodel

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import com.alialfayed.facerecognition.R
import com.alialfayed.facerecognition.repository.FirebaseHandler
import com.alialfayed.facerecognition.view.activity.InfoActivity
import com.alialfayed.facerecognition.view.activity.SignInActivity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_info.*
import java.util.*

/**
 * Class do :
 * Created by ( Eng Ali)
 */
class InfoViewModel(val infoActivity: InfoActivity):ViewModel() {

    val REQUEST_IMAGE_CAPTURE = 100
    val PERMISSION_CODE = 1001
    var updateStatus : Boolean = false

    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    private var firebaseHandler: FirebaseHandler = FirebaseHandler(infoActivity, this)


    @Suppress("DEPRECATION")
    fun pickImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        infoActivity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_CAPTURE)
    }

    fun checkPermission(permission: String): Boolean {
        val check: Int = ContextCompat.checkSelfPermission(infoActivity, permission)
        return (check == PackageManager.PERMISSION_GRANTED)
    }
    fun takePermission(){
        val arrayPermission = arrayOf<String>(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        ActivityCompat.requestPermissions(infoActivity, arrayPermission, PERMISSION_CODE)
    }

    // take Photo
    private fun takePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { pictureIntent ->
            pictureIntent.resolveActivity(infoActivity.packageManager!!)?.also {
                infoActivity.startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    fun log(){
        firebaseHandler.logout()
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun uploadImageToFirebaseStorage() {
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        if (infoActivity.selectedPhotoUri == null){
            Toast.makeText(infoActivity,"Please select photo ",Toast.LENGTH_SHORT).show()
            return
        }

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(infoActivity.selectedPhotoUri!!)
            .addOnSuccessListener {
//                Log.d(TAG, "Successfully uploaded image: ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
//                    Log.d(TAG, "File Location: $it")
                    infoActivity.disableLayout(false)
                    val name = infoActivity.edtName_Info.text.toString()
                    val age = infoActivity.edtAge_Info.text.toString()
                    val phone = infoActivity.edtPhone_Info.text.toString()
                    val image = it.toString()
                    Log.i("TAG","Update data")
                    firebaseHandler.setUser(name,age,phone,image)
                    gotoSignIn()
                }
            }
            .addOnFailureListener {
                infoActivity.disableLayout(true)
                Toast.makeText(infoActivity,"Failed to upload image to storage: ${it.message}",Toast.LENGTH_SHORT).show()
                Log.d("TAG", "Failed to upload image to storage: ${it.message}")
            }
    }

    fun infoSuccessful(){
        uploadImageToFirebaseStorage()
    }

    fun gotoSignIn(){
        val meg = "Sign Up Successful , Please confirm the account and Sign In"
        AlertDialog.Builder(infoActivity)
            .setTitle("SignUp Successful")
            .setMessage(meg)
            .setIcon(R.drawable.ic_successful)
            .setPositiveButton("Ok") { _, _ ->
                val start = Intent(infoActivity, SignInActivity::class.java)
                infoActivity.startActivity(start)
                firebaseHandler.logout()
                infoActivity.finish()
            }
            .show()
    }

}