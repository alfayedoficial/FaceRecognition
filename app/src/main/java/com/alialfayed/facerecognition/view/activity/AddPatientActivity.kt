package com.alialfayed.facerecognition.view.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.alialfayed.facerecognition.R
import com.alialfayed.facerecognition.model.PatientModel
import com.alialfayed.facerecognition.viewmodel.AddPatientViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_add_patient.*
import kotlinx.coroutines.NonCancellable.start

class AddPatientActivity : AppCompatActivity() , View.OnClickListener {


    private lateinit var addPatientViewModel: AddPatientViewModel
    var selectedphotouriOne: Uri? = null
    var selectedphotouriTwo: Uri? = null
    var selectedphotouriThree: Uri? = null
    var selectedphotouriFour: Uri? = null
    var selectedphotouriFive: Uri? = null

    var flagUpdate:Boolean = false
    var flagStartUpdate:Boolean = false


    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private  var mdatabaseReference : DatabaseReference = FirebaseDatabase.getInstance().getReference("Database")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_patient)

        addPatientViewModel = ViewModelProviders.of(this,MyViewModeFactory(this)).get(AddPatientViewModel::class.java)

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        onClickButton()
        onClickNav()


        btn_Save_Add_Patient.setOnClickListener {
            val id  = edtId_Add_Patient.text.toString()
            val name = edtName_Add_Patient.text.toString()
            val age = edtAge_Add_Patient.text.toString()
            when {
                id.isEmpty() -> {
                    edtId_Add_Patient.error = "Fields must not be empty!"
                    edtId_Add_Patient.requestFocus()
                }
                name.isEmpty() -> {
                    edtName_Add_Patient.error = "Fields must not be empty!"
                    edtName_Add_Patient.requestFocus()
                }
                age.isEmpty() -> {
                    edtAge_Add_Patient.error = "Fields must not be empty!"
                    edtAge_Add_Patient.requestFocus()
                }
                else -> when {
                    flagUpdate -> addPatientViewModel.setPatientDatabase(id,name,age)

                    flagStartUpdate -> AlertDialog.Builder(this@AddPatientActivity)
                        .setTitle("Error")
                        .setMessage("Please wait few minutes until update photos ")
                        .setIcon(R.drawable.ic_cancel)
                        .setPositiveButton("ok") { _, _ ->
                        }.show()

                    else -> AlertDialog.Builder(this@AddPatientActivity)
                        .setTitle("Error")
                        .setMessage("Must Update Image First")
                        .setIcon(R.drawable.ic_cancel)
                        .setPositiveButton("Update") { _, _ ->
                            update()
                        }
                        .show()
                }
            }
        }

        btn_Update_Add_Patient.setOnClickListener {
            update()
        }


    }
    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btnSelectImage1_Add_Patient ->{
                if (addPatientViewModel.checkPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    addPatientViewModel.pickImageFromGallery()
                } else {
                    addPatientViewModel.takePermission()
                }
            }
            R.id.btnSelectImage2_Add_Patient ->{
                if (addPatientViewModel.checkPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    addPatientViewModel.pickimage2Fromgallery()
                    addPatientViewModel.uploadimage1Tofirebasestorage()

                } else {
                    addPatientViewModel.takePermission()


                }
            }
            R.id.btnSelectImage3_Add_Patient ->{
                if (addPatientViewModel.checkPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    addPatientViewModel.pickimage3Fromgallery()
                    addPatientViewModel.uploadimage2Tofirebasestorage()

                } else {
                    addPatientViewModel.takePermission()


                }
            }
            R.id.btnSelectImage4_Add_Patient ->{
                if (addPatientViewModel.checkPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    addPatientViewModel.pickimage4Fromgallery()
                    addPatientViewModel.uploadimage3Tofirebasestorage()

                } else {
                    addPatientViewModel.takePermission()


                }
            }
            R.id.btnSelectImage5_Add_Patient ->{
                if (addPatientViewModel.checkPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    addPatientViewModel.pickimage5Fromgallery()
                    addPatientViewModel.uploadimage4Tofirebasestorage()

                } else {
                    addPatientViewModel.takePermission()
                }
            }
        }

    }
    @Suppress("UNCHECKED_CAST")
    internal class MyViewModeFactory(private val addPatientActivity: AddPatientActivity):ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return AddPatientViewModel(addPatientActivity) as T
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            addPatientViewModel.PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    addPatientViewModel.pickImageFromGallery()
                    addPatientViewModel.pickimage2Fromgallery()
                    addPatientViewModel.pickimage3Fromgallery()
                    addPatientViewModel.pickimage4Fromgallery()
                    addPatientViewModel.pickimage5Fromgallery()

                } else {
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == addPatientViewModel.REQUEST_IMAGE_CAPTURE_1 && resultCode == Activity.RESULT_OK && data != null) {
            selectedphotouriOne = data.data
        }
        else if (requestCode == addPatientViewModel.REQUEST_IMAGE_CAPTURE_2 && resultCode == Activity.RESULT_OK && data != null) {
            selectedphotouriTwo = data.data
        }else if (requestCode == addPatientViewModel.REQUEST_IMAGE_CAPTURE_3 && resultCode == Activity.RESULT_OK && data != null) {
            selectedphotouriThree = data.data
        }
        else if (requestCode == addPatientViewModel.REQUEST_IMAGE_CAPTURE_4 && resultCode == Activity.RESULT_OK && data != null) {
            selectedphotouriFour = data.data
        }else if (requestCode == addPatientViewModel.REQUEST_IMAGE_CAPTURE_5 && resultCode == Activity.RESULT_OK && data != null) {
            selectedphotouriFive = data.data
        }

    }

    private fun update(){
        addPatientViewModel.uploadimage5Tofirebasestorage()
    }

    private fun onClickButton(){
        btnSelectImage1_Add_Patient.setOnClickListener(this)
        btnSelectImage2_Add_Patient.setOnClickListener(this)
        btnSelectImage3_Add_Patient.setOnClickListener(this)
        btnSelectImage4_Add_Patient.setOnClickListener(this)
        btnSelectImage5_Add_Patient.setOnClickListener(this)
        btn_Update_Add_Patient.setOnClickListener(this)
        btn_Save_Add_Patient.setOnClickListener(this)
    }

    private fun onClickNav(){
        /**
         * Actions
         */
        btnHome_AddPatient.setOnClickListener {
            val startActivity = Intent(this,HomeActivity::class.java)
            startActivity(startActivity)
        }
        btnPatient_AddPatient.setOnClickListener {
            val startActivity = Intent(this,AddPatientActivity::class.java)
            startActivity(startActivity)
        }
        btnAddNumber_AddPatient.setOnClickListener {
            val startActivity = Intent(this,AddNumberCallActivity::class.java)
            startActivity(startActivity)
        }
        btnProfile_AddPatient.setOnClickListener {
            val startActivity = Intent(this,ProfileActivity::class.java)
            startActivity(startActivity)
        }
        btnPolicy_AddPatient.setOnClickListener {
            val startActivity = Intent(this,PolicyActivity::class.java)
            startActivity(startActivity)
        }

    }

    override fun onStart() {
        super.onStart()

        mdatabaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.hasChild("")){
                    for (patSnapshot in p0.children){
                        val patPatient = patSnapshot.getValue(PatientModel::class.java)
                        if (patPatient!!.getuserId() == FirebaseAuth.getInstance().currentUser!!.uid){
                            btnSelectImage1_Add_Patient.isEnabled = false
                            btnSelectImage2_Add_Patient.isEnabled = false
                            btnSelectImage3_Add_Patient.isEnabled = false
                            btnSelectImage4_Add_Patient.isEnabled = false
                            btnSelectImage5_Add_Patient.isEnabled = false
                            btn_Update_Add_Patient.isEnabled = false
                            btn_Save_Add_Patient.isEnabled = false
                            edtId_Add_Patient.isEnabled = false
                            edtName_Add_Patient.isEnabled = false
                            edtAge_Add_Patient.isEnabled = false
                            textView8.visibility = View.VISIBLE
                        }else{
                            btnSelectImage1_Add_Patient.isEnabled = true
                            btnSelectImage2_Add_Patient.isEnabled = true
                            btnSelectImage3_Add_Patient.isEnabled = true
                            btnSelectImage4_Add_Patient.isEnabled = true
                            btnSelectImage5_Add_Patient.isEnabled = true
                            btn_Update_Add_Patient.isEnabled = true
                            btn_Save_Add_Patient.isEnabled = true
                            edtId_Add_Patient.isEnabled = true
                            edtName_Add_Patient.isEnabled = true
                            edtAge_Add_Patient.isEnabled = true
                            textView8.visibility = View.GONE

                        }
                    }
                }
            }
        })
    }


}
