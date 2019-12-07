package com.alialfayed.facerecognition.view.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.alialfayed.facerecognition.R
import com.alialfayed.facerecognition.model.PatientModel
import com.alialfayed.facerecognition.model.PhoneModel
import com.alialfayed.facerecognition.viewmodel.HomeActivityViewModel
import com.alialfayed.utils.ServiceOnBackground
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() , View.OnClickListener{


    private lateinit var homeActivityViewModel: HomeActivityViewModel
    private lateinit var mdatabaseReference : DatabaseReference
    private lateinit var mPhoneDatabaseReference: DatabaseReference
    private lateinit var patientData :PatientModel
    private lateinit var phoneListData: PhoneModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        homeActivityViewModel = ViewModelProviders.of(this,MyViewModelFactory(this)).get(HomeActivityViewModel::class.java)
        mdatabaseReference = FirebaseDatabase.getInstance().getReference("Database")
        mPhoneDatabaseReference = FirebaseDatabase.getInstance().getReference("PhoneList")
        mdatabaseReference.keepSynced(true)
        patientData = PatientModel()
        phoneListData = PhoneModel()
        navbuttons()

    }

    override fun onStart() {
        super.onStart()
        val serviceIntent = Intent(this , ServiceOnBackground::class.java)
        startService(serviceIntent)
//        ContextCompat.startForegroundService(this,serviceIntent)
        mdatabaseReference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.hasChild("")){
                    for (patSnapshot in p0.children){
                        val patPatient = patSnapshot.getValue(PatientModel::class.java)
                        if (patPatient!!.getuserId() ==FirebaseAuth.getInstance().currentUser!!.uid){
                            patientData = patPatient
                            Log.i("TAG","Get Model")
                            if (patientData.getpatientStatus() == "Dangerous"){
                                txt_Status_Home.text = "Dangerous"
                                txt_Status_Home.setTextColor(Color.parseColor("#E85A5A"))
                                btnStatus_Home?.setBackgroundResource(R.drawable.btn_red)
                                btnCallHome.visibility = View.GONE
                                btnCallPolice.visibility = View.VISIBLE
                                btnCallDoctor.visibility = View.VISIBLE
                                btnCallHospital.visibility = View.VISIBLE
                            }else{
                                txt_Status_Home.text = "Safety"
                                txt_Status_Home.setTextColor(Color.parseColor("#5CBA47"))
                                btnStatus_Home?.setBackgroundResource(R.drawable.btn_green)
                                btnCallHome.visibility = View.VISIBLE
                                btnCallPolice.visibility = View.GONE
                                btnCallDoctor.visibility = View.GONE
                                btnCallHospital.visibility = View.GONE
                            }
                        }
                    }
                    homeActivityViewModel.setData(patientData)
                    scrollViewHome.visibility = View.VISIBLE
                    LinerLayoutHome.visibility = View.GONE
                }else{
                    scrollViewHome.visibility = View.GONE
                    LinerLayoutHome.visibility = View.VISIBLE
                }
            }

        })
        mPhoneDatabaseReference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.hasChild("")){
                    for (phoneSnapshot in p0.children){
                        val phoneData = phoneSnapshot.getValue(PhoneModel::class.java)
                        if (phoneData!!.getuserId() == FirebaseAuth.getInstance().currentUser!!.uid){
                            phoneListData = phoneData
                            Log.i("TAG","phoneListData Done")

                        }
                    }
                }
            }

        })

    }


    fun navbuttons(){
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

    }

    override fun onClick(view: View?) {
        when(view?.id){
           R.id.btnHome -> {
                val start = Intent(this,HomeActivity::class.java)
               startActivity(start)
            }
            R.id.btnProfile -> {
                val start = Intent(this,ProfileActivity::class.java)
                startActivity(start)
            }
            R.id.btnPatient ->{
                val start = Intent(this,AddPatientActivity::class.java)
                startActivity(start)
            }
            R.id.btnAddNumber ->{
                val startActivity = Intent(this,AddNumberCallActivity::class.java)
                startActivity(startActivity)
            }
            R.id.btnPolicy ->{
                val startActivity = Intent(this,PolicyActivity::class.java)
                startActivity(startActivity)
            }
            R.id.imageButton ->{
                homeActivityViewModel.signout()
            }

            R.id.btnCallHome ->{
                Log.i("TAG","Onclick Create")
                makeCall(phoneListData.getpatientNumberHome())
            }
            R.id.btnCallPolice ->{
                makeCall(phoneListData.getpatientNumberPolice())

            }
            R.id.btnCallDoctor ->{
                makeCall(phoneListData.getpatientNumberDoctor())

            }
            R.id.btnCallHospital ->{
                makeCall(phoneListData.getpatientNumberHospital())

            }
            R.id.imageBtn_Delete ->{
                mdatabaseReference.child(patientData.getpatientCode()).removeValue()
            }


        }
    }

    fun makeCall(homeNumber:String){
        if (homeActivityViewModel.checkPermission(android.Manifest.permission.CALL_PHONE)){
            Log.i("TAG","Check Permission")
            val intent = Intent(Intent.ACTION_CALL);
            intent.data = Uri.parse("tel:$homeNumber")
            startActivity(intent)
        }else{
            homeActivityViewModel.takePermission()
            Log.i("TAG","take Permission")

        }
    }

    @Suppress("UNCHECKED_CAST")
    internal class MyViewModelFactory(private val homeActivity: HomeActivity):ViewModelProvider.Factory{
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
}
