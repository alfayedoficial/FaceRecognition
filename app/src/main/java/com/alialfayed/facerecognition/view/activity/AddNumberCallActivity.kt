package com.alialfayed.facerecognition.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.alialfayed.facerecognition.R
import com.alialfayed.facerecognition.model.PatientModel
import com.alialfayed.facerecognition.view.activity.HomeActivity.Companion.phoneListData
import com.alialfayed.facerecognition.viewmodel.AddNumberCallViewModel
import com.alialfayed.facerecognition.viewmodel.InfoViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_add_number_call.*
import kotlinx.android.synthetic.main.activity_add_patient.*

class AddNumberCallActivity : AppCompatActivity() {

    private lateinit var addNumberCallViewModel: AddNumberCallViewModel
    private  var mdatabaseReference : DatabaseReference = FirebaseDatabase.getInstance().getReference("Database")
    private  var mPhoneDatabaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("PhoneList")



    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_number_call)

        addNumberCallViewModel = ViewModelProviders.of(this,MyViewModelFactory(this)
        ).get(AddNumberCallViewModel::class.java)

        onClickNav()


        btn_Save_Add_Number.setOnClickListener {
            addNumberCallViewModel.setNumber()
        }

        btn_Reset_Add_Number.setOnClickListener {
            mPhoneDatabaseReference.child(phoneListData.getPhoneID()).removeValue()
        }

    }

    private fun onClickNav(){
        /**
         * Actions
         */
        btnHome_Number_Add_Number_Call.setOnClickListener {
            val startActivity = Intent(this,HomeActivity::class.java)
            startActivity(startActivity)
        }
        btnPatient_Number_Add_Number_Call.setOnClickListener {
            val startActivity = Intent(this,AddPatientActivity::class.java)
            startActivity(startActivity)
        }
        btnAddNumber_Number_Add_Number_Call.setOnClickListener {
            val startActivity = Intent(this,AddNumberCallActivity::class.java)
            startActivity(startActivity)
        }
        btnProfile_Number_Add_Number_Call.setOnClickListener {
            val startActivity = Intent(this,ProfileActivity::class.java)
            startActivity(startActivity)
        }
        btnPolicy_Number_Add_Number_Call.setOnClickListener {
            val startActivity = Intent(this,PolicyActivity::class.java)
            startActivity(startActivity)
        }

    }

    @Suppress("UNCHECKED_CAST")
    internal class MyViewModelFactory(private val addNumberCallActivity: AddNumberCallActivity) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return AddNumberCallViewModel(addNumberCallActivity) as T
        }

    }

    override fun onStart() {
        super.onStart()

        mPhoneDatabaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.hasChild("")){
                    for (patSnapshot in p0.children){
                        val patPatient = patSnapshot.getValue(PatientModel::class.java)
                        if (patPatient!!.getuserId() == FirebaseAuth.getInstance().currentUser!!.uid){
                            btn_Save_Add_Number.isEnabled = false
                            edtHomeNumber_Add_Number_Call.isEnabled = false
                            edtPolice_Number_Add_Number_Call.isEnabled = false
                            edtDoctor_Number_Add_Number_Call.isEnabled = false
                            edtHospital_Number_Add_Number_Call.isEnabled = false
                            textView9.visibility = View.VISIBLE
                            btn_Reset_Add_Number.visibility = View.VISIBLE

                        }else{
                            btn_Save_Add_Number.isEnabled = true
                            edtHomeNumber_Add_Number_Call.isEnabled = true
                            edtPolice_Number_Add_Number_Call.isEnabled = true
                            edtDoctor_Number_Add_Number_Call.isEnabled = true
                            edtHospital_Number_Add_Number_Call.isEnabled = true
                            textView9.visibility = View.GONE
                            btn_Reset_Add_Number.visibility = View.GONE

                        }
                    }
                }
            }
        })
    }
}
