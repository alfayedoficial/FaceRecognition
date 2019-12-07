package com.alialfayed.facerecognition.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.alialfayed.facerecognition.R
import com.alialfayed.facerecognition.repository.FirebaseHandler
import com.alialfayed.facerecognition.view.activity.AddNumberCallActivity
import com.alialfayed.facerecognition.view.activity.AddPatientActivity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_add_patient.*
import java.util.*

@Suppress("DEPRECATION")
class AddPatientViewModel(val addPatientActivity: AddPatientActivity) : ViewModel() {
    val REQUEST_IMAGE_CAPTURE_1 = 100
    val REQUEST_IMAGE_CAPTURE_2 = 101
    val REQUEST_IMAGE_CAPTURE_3 = 102
    val REQUEST_IMAGE_CAPTURE_4 = 103
    val REQUEST_IMAGE_CAPTURE_5 = 104

    val PERMISSION_CODE = 1001
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    var image_1 = "null"
    var image_2 = "null"
    var image_3 = "null"
    var image_4 = "null"
    var image_5 = "null"

    private  var firebaseHandler:FirebaseHandler = FirebaseHandler(addPatientActivity , this)


    fun checkPermission(permission: String): Boolean {
        val check: Int = ContextCompat.checkSelfPermission(addPatientActivity, permission)
        return (check == PackageManager.PERMISSION_GRANTED)
    }

    @Suppress("DEPRECATION")
    fun pickImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        addPatientActivity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_CAPTURE_1)
    }

    @Suppress("DEPRECATION")
    fun pickimage2Fromgallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        addPatientActivity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_CAPTURE_2)
    }
    @Suppress("DEPRECATION")
    fun pickimage3Fromgallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        addPatientActivity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_CAPTURE_3)
    }
    @Suppress("DEPRECATION")
    fun pickimage4Fromgallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        addPatientActivity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_CAPTURE_4)
    }
    @Suppress("DEPRECATION")
    fun pickimage5Fromgallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        addPatientActivity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_CAPTURE_5)
    }

    fun takePermission(){
        val arrayPermission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(addPatientActivity, arrayPermission, PERMISSION_CODE)
    }


    @SuppressLint("ResourceAsColor")
    fun uploadimage1Tofirebasestorage() {
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        if (addPatientActivity.selectedphotouriOne == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(addPatientActivity.selectedphotouriOne!!)
            .addOnSuccessListener {
                //                Log.d(TAG, "Successfully uploaded image: ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
                    //                    Log.d(TAG, "File Location: $it")
                    image_1 = it.toString()
//                    Toast.makeText(addPatientActivity," Image one Update " ,Toast.LENGTH_SHORT).show()
                    addPatientActivity.img_successful_1.visibility = View.VISIBLE
                    addPatientActivity.img_successful_1.isEnabled = false
                    addPatientActivity.btnSelectImage1_Add_Patient.setBackgroundResource(R.drawable.btn_blue)
                    addPatientActivity.btnSelectImage1_Add_Patient.setTextColor(Color.parseColor("#F5F5FC"))
                    addPatientActivity.flagStartUpdate = true
                }
            }
            .addOnFailureListener {
                Toast.makeText(addPatientActivity," Sorry you have error " ,Toast.LENGTH_SHORT).show()
            }
    }
    @SuppressLint("ResourceAsColor")
    fun uploadimage2Tofirebasestorage() {
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        if (addPatientActivity.selectedphotouriTwo == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(addPatientActivity.selectedphotouriTwo!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    image_2 = it.toString()
//                    Toast.makeText(addPatientActivity," Image Two Update " ,Toast.LENGTH_SHORT).show()
                    addPatientActivity.img_successful_2.visibility = View.VISIBLE
                    addPatientActivity.img_successful_2.isEnabled = false
                    addPatientActivity.btnSelectImage2_Add_Patient.setBackgroundResource(R.drawable.btn_blue)
                    addPatientActivity.btnSelectImage2_Add_Patient.setTextColor(Color.parseColor("#F5F5FC"))
                }
            }
            .addOnFailureListener {
                Toast.makeText(addPatientActivity," Sorry you have error " ,Toast.LENGTH_SHORT).show()

            }
    }
    @SuppressLint("ResourceAsColor")
    fun uploadimage3Tofirebasestorage() {
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        if (addPatientActivity.selectedphotouriThree == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(addPatientActivity.selectedphotouriThree!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    image_3 = it.toString()
//                    Toast.makeText(addPatientActivity," Image Three Update " ,Toast.LENGTH_SHORT).show()
                    addPatientActivity.img_successful_3.visibility = View.VISIBLE
                    addPatientActivity.img_successful_3.isEnabled = false
                    addPatientActivity.btnSelectImage3_Add_Patient.setBackgroundResource(R.drawable.btn_blue)
                    addPatientActivity.btnSelectImage3_Add_Patient.setTextColor(Color.parseColor("#F5F5FC"))
                }
            }
            .addOnFailureListener {
                Toast.makeText(addPatientActivity," Sorry you have error " ,Toast.LENGTH_SHORT).show()

            }
    }
    @SuppressLint("ResourceAsColor")
    fun uploadimage4Tofirebasestorage() {
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        if (addPatientActivity.selectedphotouriFour == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(addPatientActivity.selectedphotouriFour!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                     image_4 = it.toString()
//                    Toast.makeText(addPatientActivity," Image Four Update " ,Toast.LENGTH_SHORT).show()
                    addPatientActivity.img_successful_4.visibility = View.VISIBLE
                    addPatientActivity.img_successful_4.isEnabled = false
                    addPatientActivity.btnSelectImage4_Add_Patient.setBackgroundResource(R.drawable.btn_blue)
                    addPatientActivity.btnSelectImage4_Add_Patient.setTextColor(Color.parseColor("#F5F5FC"))
                }
            }
            .addOnFailureListener {
                Toast.makeText(addPatientActivity," Sorry you have error " ,Toast.LENGTH_SHORT).show()

            }
    }
    @SuppressLint("ResourceAsColor")
    fun uploadimage5Tofirebasestorage() {
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        if (addPatientActivity.selectedphotouriFive == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(addPatientActivity.selectedphotouriFive!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                   if (image_1 == "null" || image_2 == "null" || image_3 == "null"
                       || image_4 == "null"
                   ){
                       Toast.makeText(addPatientActivity," Sorry , you must upload 5 photo " ,Toast.LENGTH_SHORT).show()
                   }else{
                       image_5 = it.toString()
//                       Toast.makeText(addPatientActivity," Image Five Update " ,Toast.LENGTH_SHORT).show()
                       addPatientActivity.img_successful_5.visibility = View.VISIBLE
                       addPatientActivity.img_successful_5.isEnabled = false
                       addPatientActivity.btnSelectImage5_Add_Patient.setBackgroundResource(R.drawable.btn_blue)
                       addPatientActivity.btnSelectImage5_Add_Patient.setTextColor(Color.parseColor("#F5F5FC"))
                       addPatientActivity.flagUpdate = true
                       addPatientActivity.flagStartUpdate = false
                   }

                }
            }
            .addOnFailureListener {
                Toast.makeText(addPatientActivity," Sorry you have error " ,Toast.LENGTH_SHORT).show()

            }
    }


    fun setPatientDatabase(patientId :String ,patientName :String , patientAge :String){
        firebaseHandler.setPatient(patientId,patientName,patientAge,"Safety",image_1,image_2,image_3,image_4,image_5)
    }

    fun setNumber(){
        AlertDialog.Builder(addPatientActivity)
            .setTitle("Add Emergency Numbers")
            .setMessage("You Must Add Emergency Numbers , Please Add Them ")
            .setIcon(R.drawable.ic_successful)
            .setPositiveButton("ok") { _, _ ->
                val start = Intent(addPatientActivity , AddNumberCallActivity::class.java)
                addPatientActivity.startActivity(start)
            }.show()
    }
}