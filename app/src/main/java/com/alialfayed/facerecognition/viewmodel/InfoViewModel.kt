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
import com.alialfayed.facerecognition.repository.FirebaseHandler
import com.alialfayed.facerecognition.view.activity.InfoActivity
import com.alialfayed.facerecognition.view.activity.SignInActivity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_info.*
import java.util.*
import androidx.core.content.ContextCompat.startActivity
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.R
import kotlinx.android.synthetic.main.activity_sign_up.*
import androidx.core.content.ContextCompat.startActivity
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




/**
 * Class do :
 * Created by ( Eng Ali)
 */
class InfoViewModel(val infoActivity: InfoActivity):ViewModel() {

    // References of Firebase class -> this for connection to server

    val REQUEST_IMAGE_CAPTURE = 100
    val PERMISSION_CODE = 1001
    var updateStatus : Boolean = false

    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    private var firebaseHandler: FirebaseHandler = FirebaseHandler(infoActivity, this)

    // select image
    @Suppress("DEPRECATION")
    fun pickImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        infoActivity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_CAPTURE)
    }

    // check permission
    fun checkPermission(permission: String): Boolean {
        val check: Int = ContextCompat.checkSelfPermission(infoActivity, permission)
        return (check == PackageManager.PERMISSION_GRANTED)
    }
    // take permission
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

    // log out
    fun log(){
        firebaseHandler.logout()
    }

    // update image to firebase
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun uploadImageToFirebaseStorage(name:String , age : String , phone:String) {
        infoActivity.disableLayout(false)
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

    // check if ifro successful
    fun infoSuccessful(){
        val name = infoActivity.edtName_Info.text.toString()
        val age = infoActivity.edtAge_Info.text.toString()
        val phone = infoActivity.edtPhone_Info.text.toString()
        if (name.isNullOrEmpty()){
            infoActivity.edtName_Info.error = "Name Required!\nPlease, Type your Email!"
            infoActivity.edtName_Info.requestFocus()
        }else if(name.equals(" ")){
            infoActivity.edtName_Info.error = "Name Required!\nPlease, Type your Email!"
            infoActivity.edtName_Info.requestFocus()
        }else if (age.isNullOrEmpty()){
            infoActivity.edtAge_Info.error = "Age Required!\nPlease, Type your Email!"
            infoActivity.edtAge_Info.requestFocus()
        }else if(age.equals(" ")){
            infoActivity.edtAge_Info.error = "Age Required!\nPlease, Type your Email!"
            infoActivity.edtAge_Info.requestFocus()
        }else if (phone.isNullOrEmpty()){
            infoActivity.edtPhone_Info.error = "Phone Required!\nPlease, Type your Email!"
            infoActivity.edtPhone_Info.requestFocus()
        }else if (phone.equals(" ")){
            infoActivity.edtPhone_Info.error = "Phone Required!\nPlease, Type your Email!"
            infoActivity.edtPhone_Info.requestFocus()
        }else{
            uploadImageToFirebaseStorage(name,age,phone)
        }



    }

    // go to sign in
    fun gotoSignIn(){
        val meg = "Sign Up Successful , Please confirm your account and Sign In"
        AlertDialog.Builder(infoActivity)
            .setTitle("SignUp Successful")
            .setMessage(meg)
            .setCancelable(false)
            .setIcon(com.alialfayed.facerecognition.R.drawable.ic_successful)
            .setPositiveButton("open Sign in") { _, _ ->
                val start = Intent(infoActivity, SignInActivity::class.java)
                firebaseHandler.logout()
                infoActivity.startActivity(start)
                infoActivity.finish()
            }
            .show()
    }

}