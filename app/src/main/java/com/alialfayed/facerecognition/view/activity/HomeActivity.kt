package com.alialfayed.facerecognition.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.alialfayed.deersms.utils.CheckReceiver
import com.alialfayed.facerecognition.R
import com.alialfayed.facerecognition.model.PatientModel
import com.alialfayed.facerecognition.model.PhoneModel
import com.alialfayed.facerecognition.viewmodel.HomeActivityViewModel
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home.*
import java.text.DateFormat
import java.util.*

class HomeActivity : AppCompatActivity(), View.OnClickListener {

    // References of Model class -> this for connection to server and activity
    private lateinit var homeActivityViewModel: HomeActivityViewModel
    private lateinit var mdatabaseReference: DatabaseReference
    private lateinit var mPhoneDatabaseReference: DatabaseReference
    private lateinit var patientData: PatientModel

    private lateinit var calendarAlarm: Calendar
    private lateinit var currentDate: String
    private lateinit var currentTime: String

    companion object {
        lateinit var phoneListData: PhoneModel
        var phone: Boolean = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
//        Reference of Model class
        homeActivityViewModel = ViewModelProviders.of(this, MyViewModelFactory(this))
            .get(HomeActivityViewModel::class.java)
        mdatabaseReference = FirebaseDatabase.getInstance().getReference("Database")
        mPhoneDatabaseReference = FirebaseDatabase.getInstance().getReference("PhoneList")
        mdatabaseReference.keepSynced(true)
        patientData = PatientModel()
        phoneListData = PhoneModel()

        // this is work when Activity started
        start()

        navbuttons()

        // start service
        CheckReceiver.startAlarm(this)


        // reference of time
        calendarAlarm = Calendar.getInstance()
        currentDate = DateFormat.getDateInstance().format(calendarAlarm.time)
        currentTime = DateFormat.getTimeInstance().format(calendarAlarm.time)

    }



    fun navbuttons() {
        btnHome.setOnClickListener(this)
        btnPatient.setOnClickListener(this)
        btnAddNumber.setOnClickListener(this)
        btnProfile.setOnClickListener(this)
        btnPolicy.setOnClickListener(this)
        imageButton.setOnClickListener(this)
        imageBtn_Delete.setOnClickListener(this)
        btnCallHome.setOnClickListener(this)
        btnCallPolice.setOnClickListener(this)
        btnCallDoctor.setOnClickListener(this)
        btnCallHospital.setOnClickListener(this)
        btnResetSafety.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnHome -> {
                val start = Intent(this, HomeActivity::class.java)
                startActivity(start)
                finish()
            }
            R.id.btnProfile -> {
                val start = Intent(this, ProfileActivity::class.java)
                startActivity(start)
                finish()

            }
            R.id.btnPatient -> {
                val start = Intent(this, AddPatientActivity::class.java)
                startActivity(start)
                finish()

            }
            R.id.btnAddNumber -> {
                val startActivity = Intent(this, AddNumberCallActivity::class.java)
                startActivity(startActivity)
                finish()

            }
            R.id.btnPolicy -> {
                val startActivity = Intent(this, PolicyActivity::class.java)
                startActivity(startActivity)
                finish()

            }
            R.id.imageButton -> {
                homeActivityViewModel.signout()
                CheckReceiver.cancelAlarm(this)
            }

            R.id.btnCallHome -> {
                Log.i("TAG", "Onclick Create")
                if (!phone) {
                    homeActivityViewModel.gotoAddNumber("Home Number")
                } else {
                    makeCall(phoneListData.getpatientNumberHome())
                }
            }
            R.id.btnCallPolice -> {
                if (!phone) {
                    homeActivityViewModel.gotoAddNumber("Police Number")
                } else {
                    makeCall(phoneListData.getpatientNumberPolice())
                }

            }
            R.id.btnCallDoctor -> {
                if (!phone) {
                    homeActivityViewModel.gotoAddNumber("Doctor Number")
                } else {
                    makeCall(phoneListData.getpatientNumberDoctor())
                }
            }
            R.id.btnCallHospital -> {
                if (!phone) {
                    homeActivityViewModel.gotoAddNumber("Hospital Number")
                } else {
                    makeCall(phoneListData.getpatientNumberHospital())
                }
            }
            R.id.imageBtn_Delete -> {
                AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("Delete Patient")
                    .setMessage("Do You Want To Delete Patient ?")
                    .setIcon(R.drawable.ic_cancel)
                    .setPositiveButton("Yes") { _, _ ->
                        delete()
                    }
                    .setNegativeButton("No") { _, _ -> }
                    .show()
            }

            R.id.btnResetSafety -> {
                resetSafety()
            }


        }
    }

    // call number
    fun makeCall(homeNumber: String) {
        if (homeActivityViewModel.checkPermission(android.Manifest.permission.CALL_PHONE)) {
            Log.i("TAG", "Check Permission")
            val intent = Intent(Intent.ACTION_CALL);
            intent.data = Uri.parse("tel:$homeNumber")
            startActivity(intent)
        } else {
            homeActivityViewModel.takePermission()
            Log.i("TAG", "take Permission")

        }
    }

    // this is attached by Home Activity and Homel ViewModel
    @Suppress("UNCHECKED_CAST")
    internal class MyViewModelFactory(private val homeActivity: HomeActivity) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return HomeActivityViewModel(homeActivity) as T
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            homeActivityViewModel.REQUEST_CALL_PHONE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    makeCall(phoneListData.getpatientNumberHome())
                    makeCall(phoneListData.getpatientNumberDoctor())
                    makeCall(phoneListData.getpatientNumberHospital())
                    makeCall(phoneListData.getpatientNumberPolice())
                } else {
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    fun service() {
        if (!homeActivityViewModel.isNetworkConnected()) {
            AlertDialog.Builder(this@HomeActivity)
                .setTitle("Error")
                .setMessage("Your Data transfer or Wifi connection closed!\nOpen Internet Connection and try again!")
                .setIcon(R.drawable.ic_cancel)
                .setCancelable(false)
                .setPositiveButton("open Wifi") { _, _ ->
                    startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                }
                .show()
        } else {

        }
    }
    // when user go back button finish Activity
    override fun onBackPressed() {
        finish()
    }

    // delete patient
    fun delete() {
        mdatabaseReference.child(patientData.getpatientCode()).removeValue()
        mPhoneDatabaseReference.child((phoneListData.getPhoneID())).removeValue()
    }

    // back status safety
    fun resetSafety() {
        val patientId = patientData.getpatientCode()
        // Log to check that  patientData has data before update status to safety
        Log.i("Databaseinfo", patientId)
        homeActivityViewModel.updateSafety(patientId)
        val restart= Intent(this,HomeActivity::class.java)
        startActivity(restart)

    }

    fun start(){
        mdatabaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.i("homeActivity", "1 - Start DataChange")
                for (patientSnapshot in dataSnapshot.children) {
                    // Start Loop
                    Log.i("homeActivity", " 2 - Start Loop")
                    val patSnapShot = patientSnapshot.getValue(PatientModel::class.java)
                    if (patSnapShot!!.getuserId() == FirebaseAuth.getInstance().currentUser!!.uid) {
                        // if condition of user == true
                        Log.i("homeActivity", " 3 - user == true")
                        if (patSnapShot!!.getvisibleData() == "true") {
                            // if condition of data == true
                            Log.i("homeActivity", " 4 - data == true")
                            patientData = patSnapShot
                            scrollViewHome.visibility = View.VISIBLE
                            LinerLayoutHome.visibility = View.GONE
                            if (patSnapShot!!.getpatientStatus() == "Dangerous") {
                                // if condition of status == Dangerous
                                Log.i("homeActivity", " 5 - status == Dangerous")
                                txt_Status_Home.setTextColor(Color.parseColor("#E85A5A"))
                                btnStatus_Home?.setBackgroundResource(R.drawable.btn_red)
                                btnCallHome.visibility = View.GONE
                                btnCallPolice.visibility = View.VISIBLE
                                btnCallDoctor.visibility = View.VISIBLE
                                btnCallHospital.visibility = View.VISIBLE
                                btnResetSafety.visibility = View.VISIBLE
                            } else if (patSnapShot!!.getpatientStatus() == "Safety") {
                                // if condition of status == safety
                                Log.i("homeActivity", " 6 - status == Safety")
                                txt_Status_Home.setTextColor(Color.parseColor("#5CBA47"))
                                btnStatus_Home?.setBackgroundResource(R.drawable.btn_green)
                                btnCallHome.visibility = View.VISIBLE
                                btnCallPolice.visibility = View.GONE
                                btnCallDoctor.visibility = View.GONE
                                btnCallHospital.visibility = View.GONE
                                btnResetSafety.visibility = View.GONE
                            }
                            // Do Action if you have data
                            Log.i("homeActivity", " 7 - Action if you have data")
                            homeActivityViewModel.setData(patientData)
                            service()



                        } else {
                            // if condition of data == false
                            Log.i("homeActivity", " 8 - data == false")
                            scrollViewHome.visibility = View.GONE
                            LinerLayoutHome.visibility = View.VISIBLE
                        }

                    } else {
                        // if condition of user == false
                        Log.i("homeActivity", " 9 - user == false")
                        scrollViewHome.visibility = View.GONE
                        LinerLayoutHome.visibility = View.VISIBLE
                    }
                } // End Loop
                Log.i("homeActivity", " 10 - End Loop ")

            }

        })
        mPhoneDatabaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (phoneSnapshot in dataSnapshot.children) {
                    if (phoneSnapshot.hasChildren()) {
                        val phoneData = phoneSnapshot.getValue(PhoneModel::class.java)
                        if (phoneData!!.getuserId() == FirebaseAuth.getInstance().currentUser!!.uid) {
                            if (phoneData!!.getvisibleData() == "true") {
                                phoneListData = phoneData
                                phone = true
                            } else {
                                // no have childern user
                                phone = false
                            }
                        } else {
                            // no have currentUser
                            phone = false
                        }
                    }
                    // no have childern all data
                }
            }
        })

    }


}
