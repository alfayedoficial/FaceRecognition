package com.alialfayed.facerecognition.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.alialfayed.facerecognition.R
import com.alialfayed.facerecognition.model.PatientModel
import com.alialfayed.facerecognition.repository.FirebaseHandler
import com.alialfayed.facerecognition.view.activity.HomeActivity
import com.alialfayed.facerecognition.view.activity.SignInActivity
import kotlinx.android.synthetic.main.activity_home.*
import java.text.DateFormat
import java.util.*

/**
 * Class do :
 * Created by ( Eng Ali)
 */
class HomeActivityViewModel(private val homeActivity: HomeActivity):ViewModel() {
    private var firebaseHandler: FirebaseHandler = FirebaseHandler(homeActivity, this)
    private lateinit var calendarTime: Calendar
    private lateinit var currentDate: String
    private lateinit var currentTime: String
    val REQUEST_CALL_PHONE = 100


    @SuppressLint("SetTextI18n")
    fun setData(patientDatabase : PatientModel){
        Log.i("TAG","ViewMode Done")
        calendarTime = Calendar.getInstance()
        currentDate = DateFormat.getDateInstance().format(calendarTime.time)
        currentTime = DateFormat.getTimeInstance().format(calendarTime.time)
        Log.i("TAG","Time Done")

        homeActivity.txtId_Home.text = patientDatabase.getpatientId()
        Log.i("TAG","txtId_Home Done")

        homeActivity.txtPatient_Home.text = patientDatabase.getpatientName()
        Log.i("TAG","txtPatient_Home Done")

        homeActivity.txtAge_Home.text = patientDatabase.getpatientAge()
        Log.i("TAG","txtAge_Home Done")

        homeActivity.txt_Status_Home.text = patientDatabase.getpatientStatus()
        Log.i("TAG","txt_Status_Home Done")

        homeActivity.txt_Time_Home.text = "$currentDate $currentTime"
        Log.i("TAG","txt_Time_Home Done")


    }

    /**
     * method Sign Out
     */
    fun signout(){
        val meg = "Are you sure you want to Log Out "
        AlertDialog.Builder(homeActivity)
            .setTitle("Log Out")
            .setMessage(meg)
            .setIcon(R.drawable.ic_successful)
            .setPositiveButton("Log Out") { _, _ ->
                firebaseHandler.logout()
                val start = Intent(homeActivity, SignInActivity::class.java)
                homeActivity.startActivity(start)
            }
            .show()
    }

    fun checkPermission(permission: String): Boolean {
        val check: Int = ContextCompat.checkSelfPermission(homeActivity, permission)
        return (check == PackageManager.PERMISSION_GRANTED)
    }

    fun takePermission(){
        val arrayPermission = arrayOf<String>(
            Manifest.permission.CALL_PHONE
        )
        ActivityCompat.requestPermissions(homeActivity, arrayPermission, REQUEST_CALL_PHONE)
    }

}